package reforged.ic2.addons.asp.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.item.ElectricItem;
import ic2.core.ContainerBase;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import mods.vintage.core.helpers.ElectricHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.blocks.container.ContainerAdvancedSolarPanel;
import reforged.ic2.addons.asp.blocks.gui.GuiAdvancedSolarPanel;
import reforged.ic2.addons.asp.utils.InvSlotMultiCharge;

public class TileEntityAdvancedSolarPanel extends TileEntityBaseGenerator {

    public int maxStorage;
    public int dayGen;
    public int nightGen;
    public int tier;
    public int storage;
    public int maxOutput;
    public final InvSlotMultiCharge chargeSlot;
    public GenerationState generationState = GenerationState.NONE;
    // common production
    public int production;

    public TileEntityAdvancedSolarPanel(AdvancedSolarPanelsConfig.SolarConfig energyConfig) {
        this(energyConfig.getGenDay(), energyConfig.getGenNight(), energyConfig.getStorage(), energyConfig.getTier());
    }

    public TileEntityAdvancedSolarPanel(int dayGen, int nightGen, int maxStorage, int tier) {
        super(0, maxStorage);
        this.dayGen = dayGen;
        this.nightGen = nightGen;
        this.maxStorage = maxStorage;
        this.tier = tier;
        this.maxOutput = ElectricHelper.getMaxInputFromTier(tier);
        this.chargeSlot = new InvSlotMultiCharge(this, 0, tier, 4);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("myStorage", this.storage);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.storage = compound.getInteger("myStorage");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.skyBlockCheck()) {
            if (isSunVisible()) {
                this.production = this.dayGen;
            } else {
                this.production = this.nightGen;
            }
        } else {
            this.production = 0;
        }
    }

    @Override
    public void updateEntity() {
        boolean markDirty = false;
        int oldEnergy = this.storage;
        // generate power
        if (this.production > 0) {
            if (this.storage + this.production <= this.maxStorage) {
                this.storage += this.production;
            } else {
                this.storage = this.maxStorage;
            }
        }

        boolean active = this.gainEnergy();
        if (this.storage > 0) {
            // handler charging slots
            int charged = 0;
            for (int i = 0; i < this.chargeSlot.size(); i++) {
                ItemStack stack = this.chargeSlot.get(i);
                if (this.storage <= 0) break;
                if (stack != null) {
                    charged = ElectricItem.manager.charge(stack, this.storage, this.tier, false, false);
                    if (charged > 0) markDirty = true;
                }
            }
            this.storage -= charged;

            // output energy to the world
            if (this.storage - maxOutput > 0) {
                this.storage = this.storage - (maxOutput - sendEnergy(maxOutput));
            }
        }

        // ticking and updating
        if (!this.delayActiveUpdate()) {
            this.setActive(active);
        } else {
            if (this.ticksSinceLastActiveUpdate % 128 == 0) {
                this.setActive(this.activityMeter > 0);
                this.activityMeter = 0;
            }

            if (active) {
                ++this.activityMeter;
            } else {
                --this.activityMeter;
            }

            ++this.ticksSinceLastActiveUpdate;
        }

        if (oldEnergy != this.storage) {
            markDirty = true;
        }

        if (markDirty) {
            this.onInventoryChanged();
        }
    }

    @Override
    public int getMaxEnergyOutput() {
        return this.production;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
        return direction.toForgeDirection() != ForgeDirection.UP;
    }

    @Override
    public boolean isConverting() {
        if (this.skyBlockCheck()) {
            if (isSunVisible()) {
                return this.storage + this.dayGen <= this.maxStorage;
            } else {
                return this.storage + this.nightGen <= this.maxStorage;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean gainEnergy() {
        if (this.isConverting()) {
            if (this.skyBlockCheck()) {
                if (isSunVisible()) {
                    this.generationState = GenerationState.DAY;
                } else {
                    this.generationState = GenerationState.NIGHT;
                }
            }
            return true;
        } else {
            this.generationState = GenerationState.NONE;
            return false;
        }
    }


    @Override
    public ContainerBase getGuiContainer(EntityPlayer player) {
        return new ContainerAdvancedSolarPanel(player.inventory, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiAdvancedSolarPanel(player.inventory, this);
    }

    @Override
    public int gaugeStorageScaled(int i) {
        return (int)((long)this.storage * i / this.maxStorage);
    }

    @Override
    public float getWrenchDropRate() {
        return 1F;
    }

    public boolean skyBlockCheck() {
        return this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord) && !this.worldObj.provider.hasNoSky;
    }

    public boolean isSunVisible() {
        return isSunVisible(this.worldObj, this.xCoord, this.zCoord);
    }

    public static boolean isSunVisible(World world, int x, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        long time = world.getWorldTime() % 24000;
        if (time < 12600) {
            boolean isHot = biome.temperature > 1.0F;
            boolean canRain = biome.canSpawnLightningBolt();
            return (isHot && !canRain) || (!world.isRaining() && !world.isThundering());
        }

        return false;
    }

    public enum GenerationState {
        NONE, NIGHT, DAY;
    }

    /// ------------ UNUSED
    @Override
    public boolean gainFuel() {
        return false;
    }

    @Override
    public String getInvName() {
        return "";
    }

    @Override
    public int gaugeFuelScaled(int fuel) {
        return fuel;
    }
}
