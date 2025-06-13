package reforged.ic2.addons.asp.tiles;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import reforged.ic2.addons.asp.blocks.container.ContainerMolecularTransformer;
import reforged.ic2.addons.asp.blocks.gui.GuiMolecularTransformer;
import reforged.ic2.addons.asp.utils.EnergyUtils;
import reforged.ic2.addons.asp.utils.molecular.MTRecipeManager;
import reforged.ic2.addons.asp.utils.molecular.RecipeRecord;
import reforged.ic2.addons.asp.utils.molecular.MolecularInputSlot;

import java.util.List;
import java.util.Random;

public class TileEntityMolecularTransformer extends TileEntityInventory implements IHasGui, IEnergySink {

    public static Random randomizer = new Random();
    public int ticker;
    public boolean addedToEnergyNet;
    public boolean doWork;
    public boolean waitOutputSlot;
    public ItemStack lastRecipeInput;
    public ItemStack lastRecipeOutput;
    public int lastRecipeEnergyUsed;
    public int lastRecipeEnergyPerOperation;
    public int lastRecipeNumber;

    private int currentTickInputEU = 0;

    private boolean deactiveTimer = false;
    private int deactiveTicker = 0;

    private int energyTicker = 0;
    public short lastProgress;
    public int energyBuffer;
    public int inputEU;
    public boolean loaded = false;
    public MolecularInputSlot inputSlot;
    public InvSlotOutput outputSlot;

    public TileEntityMolecularTransformer() {
        this.ticker = randomizer.nextInt(20);
        this.inputSlot = new MolecularInputSlot(this, "transformer_in", 0, 1);
        this.outputSlot = new InvSlotOutput(this, "transformer_out", 1, 1);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        this.loaded = true;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        this.loaded = false;
    }

    @Override
    public void updateEntity() {
        if (IC2.platform.isRendering()) return;
        this.inputEU = this.currentTickInputEU;
        this.currentTickInputEU = 0;
        updateSlots();
        if (!this.doWork && this.inputSlot.get(0) != null) {
            if (canProcess()) {
                this.inputSlot.get(0).stackSize -= this.lastRecipeInput.stackSize;
                this.lastRecipeEnergyUsed = 0;
                this.waitOutputSlot = false;
                this.lastProgress = 0;
                this.doWork = true;
                this.deactiveTimer = false;
            }
        } else if (this.doWork) {
            if (this.energyBuffer > 0) {
                this.energyTicker = 0;
                if (!this.waitOutputSlot) {
                    this.setActive(true);
                }
            } else {
                this.energyTicker++;
                int energyTickRate = 60;
                if (this.energyTicker >= energyTickRate) {
                    this.energyTicker = 0;
                    if (this.getActive()) {
                        this.setActive(false);
                    }
                }
            }

            this.energyBuffer = gainFuel(this.energyBuffer);
        }
        if (this.deactiveTimer) {
            checkDeactivateMachine();
        }
    }

    public boolean canProcess() {
        Pair<RecipeRecord, Integer> recipePair = getCurrentRecipe();
        if (recipePair == null) return false;
        ItemStack inputStack = recipePair.recipe.input;
        ItemStack outputStack = recipePair.recipe.output;
        if (inputStack.isItemEqual(this.inputSlot.get(0))) {
            if (this.outputSlot.get(0) != null) {
                if (outputStack.isItemEqual(this.outputSlot.get(0))) {
                    if (this.outputSlot.get(0).stackSize + outputStack.stackSize > this.outputSlot.get(0).getMaxStackSize()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (this.inputSlot.get(0).stackSize < inputStack.stackSize) {
                return false;
            }
            this.lastRecipeInput = inputStack;
            this.lastRecipeOutput = outputStack;
            this.lastRecipeEnergyPerOperation = recipePair.recipe.energy;
            this.lastRecipeNumber = recipePair.index;
            return true;
        }
        return false;
    }

    public int gainFuel(int energyPacket) {
        int energyLeft = energyPacket;
        if (energyLeft >= 0) {
            if (this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed > energyPacket) {
                energyLeft = 0;
                this.lastRecipeEnergyUsed += energyPacket;
            } else {
                energyLeft = energyPacket - (this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed);
                this.lastRecipeEnergyUsed = this.lastRecipeEnergyPerOperation;
                this.lastProgress = 100;
                this.waitOutputSlot = true;
                if (this.outputSlot.get(0) != null) {
                    if (this.lastRecipeOutput.isItemEqual(this.outputSlot.get(0))) {
                        if (this.outputSlot.get(0).getMaxStackSize() >= this.outputSlot.get(0).stackSize + this.lastRecipeOutput.stackSize) {
                            this.outputSlot.get(0).stackSize += this.lastRecipeOutput.stackSize;
                            this.doWork = false;
                            this.deactiveTicker = 0;
                            onInventoryChanged();
                            this.waitOutputSlot = false;
                        } else if (this.getActive()) {
                            setActive(false);
                        }
                    }
                } else {
                    this.outputSlot.put(this.lastRecipeOutput.copy());
                    this.doWork = false;
                    this.deactiveTicker = 0;
                    this.deactiveTimer = true;
                    this.waitOutputSlot = false;
                    onInventoryChanged();
                }
            }
        }
        updateProgress();
        return energyLeft;
    }

    public void updateProgress() {
        if (this.doWork) {
            this.setActive(true);
            float progress = (float) this.lastRecipeEnergyUsed / this.lastRecipeEnergyPerOperation * 100.0F;
            this.lastProgress = (short) Math.round(progress);
            if (this.lastRecipeEnergyUsed == this.lastRecipeEnergyPerOperation) {
                this.lastProgress = 100;
            }
        } else {
            this.lastProgress = 0;
            if (this.getActive()) {
                this.deactiveTicker = 0;
                this.deactiveTimer = true;
            }
        }
    }

    public void updateSlots() {
        if (this.inputSlot.get(0) != null && this.inputSlot.get(0).stackSize <= 0) {
            this.inputSlot.put(null);
            onInventoryChanged();
        }
        if (this.outputSlot.get(0) != null && this.outputSlot.get(0).stackSize <= 0) {
            this.outputSlot.put(null);
            onInventoryChanged();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.doWork = compound.getBoolean("doWork");
        this.lastRecipeEnergyUsed = compound.getInteger("lastRecipeEnergyUsed");
        this.lastRecipeEnergyPerOperation = compound.getInteger("lastRecipeEnergyPerOperation");
        this.lastProgress = compound.getShort("lastProgress");

        NBTTagList recipeTagList = compound.getTagList("Recipes");
        for (int i = 0; i < recipeTagList.tagCount(); i++) {
            NBTTagCompound recipeTag = (NBTTagCompound) recipeTagList.tagAt(i);
            if (i == 0) {
                this.lastRecipeInput = ItemStack.loadItemStackFromNBT(recipeTag);
            } else if (i == 1) {
                this.lastRecipeOutput = ItemStack.loadItemStackFromNBT(recipeTag);
            }
        }

        if (this.lastRecipeInput == null || this.lastRecipeOutput == null) {
            this.lastRecipeNumber = -1;
            this.doWork = false;
            this.lastProgress = 0;
            this.lastRecipeEnergyUsed = 0;
        } else {
            Pair<RecipeRecord, Integer> recipe = getCurrentRecipe(this.lastRecipeInput, this.lastRecipeOutput);
            if (recipe.index <= 0) {
                this.lastRecipeNumber = -1;
                this.doWork = false;
                this.lastProgress = 0;
                this.lastRecipeEnergyUsed = 0;
            } else {
                this.lastRecipeNumber = recipe.index;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList recipeTagList = new NBTTagList();
        compound.setBoolean("doWork", this.doWork);
        compound.setInteger("lastRecipeEnergyUsed", this.lastRecipeEnergyUsed);
        compound.setInteger("lastRecipeEnergyPerOperation", this.lastRecipeEnergyPerOperation);
        compound.setShort("lastProgress", this.lastProgress);
        compound.setInteger("lastRecipeNumber", this.lastRecipeNumber);

        NBTTagCompound inputTag = new NBTTagCompound();
        if (this.lastRecipeInput != null) {
            inputTag.setBoolean("lastRecipeInput", true);
            this.lastRecipeInput.writeToNBT(inputTag);
        } else {
            inputTag.setBoolean("lastRecipeInput", false);
        }
        recipeTagList.appendTag(inputTag);

        NBTTagCompound outputTag = new NBTTagCompound();
        if (this.lastRecipeOutput != null) {
            outputTag.setBoolean("lastRecipeOutput", true);
            this.lastRecipeOutput.writeToNBT(outputTag);
        } else {
            outputTag.setBoolean("lastRecipeOutput", false);
        }
        recipeTagList.appendTag(outputTag);

        compound.setTag("Recipes", recipeTagList);
    }

    @Override
    public boolean enableUpdateEntity() {
        return true;
    }

    @Override
    public ContainerBase getGuiContainer(EntityPlayer player) {
        return new ContainerMolecularTransformer(player.inventory, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiMolecularTransformer(player.inventory, this);
    }

    @Override
    public String getInvName() {
        return FormattedTranslator.AQUA.format("block.molecular.transformer.name");
    }

    @Override
    public int demandsEnergy() {
        if (!this.doWork) {
            this.inputEU = 0;
            return 0;
        }
        return Math.max(this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed, 0);
    }

    @Override
    public int injectEnergy(Direction direction, int amount) {
        this.currentTickInputEU += amount;
        if (!this.doWork) {
            return amount;
        }
        if (this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed >= amount) {
            this.energyBuffer = this.energyBuffer + amount;
            return 0;
        }
        this.energyBuffer = this.energyBuffer + amount - (this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed);
        return amount - (this.lastRecipeEnergyPerOperation - this.lastRecipeEnergyUsed);
    }

    @Override
    public int getMaxSafeInput() {
        return EnergyUtils.getPowerFromTier(6);
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, Direction direction) {
        return true;
    }

    @Override
    public boolean isAddedToEnergyNet() {
        return this.addedToEnergyNet;
    }

    public Pair<RecipeRecord, Integer> getCurrentRecipe() {
        if (this.inputSlot.get(0) == null) return null;
        ItemStack inputStack = this.inputSlot.get(0);
        List<RecipeRecord> recipes = MTRecipeManager.instance.getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            RecipeRecord recipe = recipes.get(i);
            if (recipe.input.isItemEqual(inputStack)) {
                return new Pair<RecipeRecord, Integer>(recipe, i);
            }
        }
        return null;
    }

    public Pair<RecipeRecord, Integer> getCurrentRecipe(ItemStack input, ItemStack output) {
        if (input == null || output == null) return null;
        List<RecipeRecord> recipes = MTRecipeManager.instance.getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            RecipeRecord recipe = recipes.get(i);
            if (input.isItemEqual(recipe.input) && output.isItemEqual(recipe.output)) {
                return new Pair<RecipeRecord, Integer>(recipes.get(i), i);
            }
        }
        return null;
    }

    public void checkDeactivateMachine() {
        if (this.deactiveTimer) {
            this.deactiveTicker++;
            int deactiveTickrate = 40;
            if (this.deactiveTicker == deactiveTickrate) {
                this.deactiveTicker = 0;
                this.deactiveTimer = false;
                setActive(false);
            }
        }
    }

    /// -------------- UNUSED
    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public static class Pair<RECIPE, INDEX> {
        RECIPE recipe;
        INDEX index;

        public Pair(RECIPE recipe, INDEX index) {
            this.recipe = recipe;
            this.index = index;
        }
    }
}
