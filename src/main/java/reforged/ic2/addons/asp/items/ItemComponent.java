package reforged.ic2.addons.asp.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import reforged.ic2.addons.asp.ASPBlocksItems;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.References;

import java.util.List;

public class ItemComponent extends Item implements IItemBlockIDProvider {

    public String[] names = new String[] {
            // parts
            "sunnarium.part",
            "irradiant.uranium",
            "spectral.sunnarium.part",
            "ender.sunnarium.part",
            // chunks
            "sunnarium",
            "enriched.sunnarium",
            "spectral.sunnarium",
            "ender.sunnarium",
            // alloys
            "sunnarium.alloy",
            "enriched.sunnarium.alloy",
            "spectral.sunnarium.alloy",
            "ender.sunnarium.alloy",
            // glass panes
            "irradiant.glass.pane",
            "spectral.glass.pane",
            // plates
            "iridium.iron.plate",
            "reinforced.iridium.iron.plate",
            "irradiant.reinforced.plate",
            "spectral.reinforced.plate",
            // cores
            "quantum.core",
            "photonic.core",
            // misc
            "ingot.iridium",
            "mt.core",

    };

    @SideOnly(Side.CLIENT)
    public Icon[] textures = new Icon[names.length];

    public ItemComponent() {
        super(AdvancedSolarPanelsConfig.ASP_ITEM_ID.get());
        this.setHasSubtypes(true);
        this.setCreativeTab(AdvancedSolarPanels.TAB);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        for (ASPBlocksItems.Component component : ASPBlocksItems.Component.VALUES) {
            list.add(component.getStack());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister registry) {
        for (int i = 0; i < names.length; i++) {
            textures[i] = registry.registerIcon(References.MOD_ID + ":" + names[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta) {
        return this.textures[meta];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = stack.getItemDamage();
        return "item." + names[damage];
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        FormattedTranslator format;
        int meta = stack.getItemDamage();
        switch (meta) {
            case 1:
            case 5:
            case 9:
            case 12:
                format = FormattedTranslator.GREEN;
                break;
            case 0:
            case 4:
            case 8:
            case 16:
                format = FormattedTranslator.YELLOW;
                break;
            case 2:
            case 6:
            case 10:
            case 13:
            case 17:
                format = FormattedTranslator.RED;
                break;
            case 3:
            case 7:
            case 11:
            case 19:
                format = FormattedTranslator.DARK_AQUA;
                break;
            case 18:
            case 21:
                format = FormattedTranslator.LIGHT_PURPLE;
                break;
            default:
                format = FormattedTranslator.GOLD;
        }
        return format.literal(super.getItemDisplayName(stack));
    }
}
