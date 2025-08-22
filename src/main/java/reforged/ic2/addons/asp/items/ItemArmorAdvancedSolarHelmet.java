package reforged.ic2.addons.asp.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.util.StackUtil;
import mods.vintage.core.helpers.ElectricHelper;
import mods.vintage.core.helpers.StackHelper;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.References;

import java.util.*;

public class ItemArmorAdvancedSolarHelmet extends ItemArmor implements IElectricItem, IMetalArmor, ISpecialArmor, IItemBlockIDProvider {

    private final int capacity;
    private final int transfer;
    private final int tier;

    private int generating;
    private int ticker;
    private boolean sunIsUp;
    private boolean skyIsVisible;

    private final int genDay;
    private final int genNight;

    private final int energyPerDamage;
    private final double damageAbsorptionRatio;
    private final double baseAbsorptionRatio;
    AdvancedSolarPanelsConfig.SolarConfig config;
    String name;

    private static final Map<Integer, Integer> potionRemovalCost = new HashMap<Integer, Integer>();

    public ItemArmorAdvancedSolarHelmet(int id, String name, AdvancedSolarPanelsConfig.SolarConfig config) {
        super(id, EnumArmorMaterial.DIAMOND, AdvancedSolarPanels.PROXY.addArmor(name), 0);
        this.capacity = config.getStorage();
        this.transfer = ElectricHelper.getMaxInputFromTier(config.getTier()) * 20;
        this.tier = config.getTier();
        this.name = name;

        this.genDay = config.getGenDay();
        this.genNight = config.getGenNight();

        potionRemovalCost.put(Potion.poison.id, 10000);
        potionRemovalCost.put(IC2Potion.radiation.id, 10000);
        potionRemovalCost.put(Potion.wither.id, 25000);

        if (config == AdvancedSolarPanelsConfig.ADVANCED) {
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9D;
        } else {
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0D;
        }
        this.baseAbsorptionRatio = 0.15D;
        this.config = config;

        this.setCreativeTab(AdvancedSolarPanels.TAB);
        this.setMaxDamage(27);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        ElectricHelper.energyTooltip(ElectricHelper.getCharge(stack), this.getMaxCharge(stack), this.getTier(stack));
        NBTTagCompound tag = StackHelper.getOrCreateTag(stack);
        boolean nightVision = tag.getBoolean("nightVision");
        if (nightVision) {
            list.add(Translator.GOLD.format("message.info.helmet.night.vision", Translator.GREEN.format("message.info.enabled")));
        } else {
            list.add(Translator.GOLD.format("message.info.helmet.night.vision", Translator.RED.format("message.info.disabled")));
        }
        list.add(Translator.GREEN.format("message.info.helmet.line1"));
        if (AdvancedSolarPanels.PROXY.isSneakKeyDown()) {
            list.add(Translator.GREEN.format("message.info.helmet.line2"));
            list.add(Translator.GRAY.format("message.info.press.to.enable2",
                    Translator.GOLD.literal("IC2 Alt Key"),
                    Translator.GOLD.literal("IC2 Mode Switch Key"),
                    Translator.YELLOW.format("message.info.helmet.night.vision.short")));
            list.add("");
            list.add(Translator.GRAY.format("tooltip.info.solar.gen.day", Translator.AQUA.literal(this.config.getGenDay() + "")));
            list.add(Translator.GRAY.format("tooltip.info.solar.gen.night", Translator.AQUA.literal(this.config.getGenNight() + "")));
            list.add(Translator.GRAY.format("message.info.solar.max.out", Translator.AQUA.literal(ElectricHelper.getMaxInputFromTier(this.config.getTier()) * 20 + "")));
        } else {
            list.add(Translator.GRAY.format("message.info.press", Translator.GOLD.format(References.SNEAK_KEY)));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register) {
        if (this.config == AdvancedSolarPanelsConfig.ADVANCED)
            this.itemIcon = register.registerIcon(References.MOD_ID + ":helmet/advanced");
        if (this.config == AdvancedSolarPanelsConfig.HYBRID)
            this.itemIcon = register.registerIcon(References.MOD_ID + ":helmet/hybrid");
        if (this.config == AdvancedSolarPanelsConfig.ULTIMATE)
            this.itemIcon = register.registerIcon(References.MOD_ID + ":helmet/ultimate");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
        if (this.config == AdvancedSolarPanelsConfig.ADVANCED)
            return "/mods/AdvancedSolarPanels/textures/armor/helmet/advanced.png";
        if (this.config == AdvancedSolarPanelsConfig.HYBRID)
            return "/mods/AdvancedSolarPanels/textures/armor/helmet/hybrid.png";
        if (this.config == AdvancedSolarPanelsConfig.ULTIMATE)
            return "/mods/AdvancedSolarPanels/textures/armor/helmet/ultimate.png";
        return "";
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return super.getItemDisplayName(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.helmet." + this.name;
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        ElectricHelper.addChargeVariants(this, list);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = tag.getByte("toggleTimer");
        if (world.isRemote) return;

        generateEnergy(player);
        int airLevel = player.getAir();
        if (ElectricItem.manager.canUse(stack, 1000) && airLevel < 100) {
            player.setAir(airLevel + 200);
            ElectricItem.manager.use(stack, 1000, null);
        }

        Iterator<PotionEffect> potions = new LinkedList<PotionEffect>(player.getActivePotionEffects()).iterator();
        int id;
        while (potions.hasNext()) {
            PotionEffect effect = potions.next();
            id = effect.getPotionID();
            Integer cost = potionRemovalCost.get(id);
            if (cost != null) {
                cost = cost * (effect.getAmplifier() + 1);
                if (ElectricItem.manager.canUse(stack, cost)) {
                    ElectricItem.manager.use(stack, cost, null);
                    IC2.platform.removePotion(player, id);
                }
            }
        }

        boolean nightVision = tag.getBoolean("nightVision");
        if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
            toggleTimer = 10;
            nightVision = !nightVision;
            if (IC2.platform.isSimulating()) {
                tag.setBoolean("nightVision", nightVision);
                if (nightVision) {
                    IC2.platform.messagePlayer(player, Translator.GOLD.format("message.info.helmet.night.vision", Translator.GREEN.format("message.info.enabled")));
                } else {
                    IC2.platform.messagePlayer(player, Translator.GOLD.format("message.info.helmet.night.vision", Translator.RED.format("message.info.disabled")));
                }
            }
        }

        if (nightVision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1, player)) {
            int x = MathHelper.floor_double(player.posX);
            int z = MathHelper.floor_double(player.posZ);
            int y = MathHelper.floor_double(player.posY);
            int lightLevel = player.worldObj.getBlockLightValue(x, y, z);
            if (lightLevel > 8) {
                IC2.platform.removePotion(player, Potion.nightVision.id);
            } else {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, -3));
            }
        }

        if (IC2.platform.isSimulating() && toggleTimer > 0) {
            --toggleTimer;
            tag.setByte("toggleTimer", toggleTimer);
        }

        if (this.generating <= 0) return;

        int energyLeft = this.generating;

        ItemStack[] armor = player.inventory.armorInventory;
        ItemStack[] main = player.inventory.mainInventory;

        for (ItemStack[] inventory : new ItemStack[][] {armor, main}) {
            for (ItemStack invStack : inventory) {
                if (energyLeft <= 0) return;

                if (invStack != null && invStack.getItem() instanceof IElectricItem) {
                    int charged = ElectricItem.manager.charge(invStack, energyLeft, 4, false, false);
                    energyLeft -= charged;
                }
            }
        }
    }

    public void generateEnergy(EntityPlayer player) {
        if (this.ticker++ % 128 == 0)
            updateVisibility(player);
        if (this.sunIsUp && this.skyIsVisible) {
            this.generating = this.genDay;
            return;
        }
        if (this.skyIsVisible) {
            this.generating = this.genNight;
            return;
        }
        this.generating = 0;
    }

    public void updateVisibility(EntityPlayer player) {
        boolean wetBiome = (player.worldObj.getWorldChunkManager().getBiomeGenAt((int) player.posX, (int) player.posZ)
                .getIntRainfall() > 0);
        boolean noSunWorld = player.worldObj.provider.hasNoSky;
        boolean rainWeather = wetBiome && (player.worldObj.isRaining() || player.worldObj.isThundering());
        this.sunIsUp = player.worldObj.isDaytime() && !rainWeather;
        this.skyIsVisible = player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY + 1, (int) player.posZ)
                && !noSunWorld;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getChargedItemId(ItemStack stack) {
        return this.itemID;
    }

    @Override
    public int getEmptyItemId(ItemStack stack) {
        return this.itemID;
    }

    @Override
    public int getMaxCharge(ItemStack stack) {
        return this.capacity;
    }

    @Override
    public int getTier(ItemStack stack) {
        return this.tier;
    }

    @Override
    public int getTransferLimit(ItemStack stack) {
        return this.transfer;
    }

    @Override
    public boolean isMetalArmor(ItemStack stack, EntityPlayer player) {
        return true;
    }

    @Override
    public ArmorProperties getProperties(EntityLiving entityLiving, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source.isUnblockable())
            return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);
        double absorptionRatio = baseAbsorptionRatio * damageAbsorptionRatio;
        int energyDamage = energyPerDamage;
        int damageLimit = (int) ((energyDamage > 0)
                ? (25.0D * ElectricItem.manager.getCharge(armor) / energyDamage)
                : 0.0D);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int i) {
        if (ElectricItem.manager.getCharge(armor) >= energyPerDamage)
            return (int) Math.round(20.0D * baseAbsorptionRatio * damageAbsorptionRatio);
        return 0;
    }

    @Override
    public void damageArmor(EntityLiving living, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, (damage * energyPerDamage), Integer.MAX_VALUE, true, false);
    }
}
