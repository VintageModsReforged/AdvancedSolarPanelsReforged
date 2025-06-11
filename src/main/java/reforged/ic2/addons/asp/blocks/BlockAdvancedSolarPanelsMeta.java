package reforged.ic2.addons.asp.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.blocks.base.BlockAdvancedBlock;
import reforged.ic2.addons.asp.items.ItemBlockAdvancedSolarPanelsMeta;
import reforged.ic2.addons.asp.tiles.*;

import java.util.List;

public class BlockAdvancedSolarPanelsMeta extends BlockAdvancedBlock {

    public static final int ADVANCED = 0;
    public static final int HYBRID = 1;
    public static final int ULTIMATE = 2;
    public static final int QUANTUM = 3;
    public static final int QUANTUM_GENERATOR = 4;
    public static final int SPECTRAL = 5;
    public static final int SINGULAR = 6;
    public static final int LIGHT_ABSORBING = 7;
    public static final int PHOTONIC = 8;

    public BlockAdvancedSolarPanelsMeta() {
        super(AdvancedSolarPanelsConfig.ASP_META_BLOCK_ID.get());
        this.metaMachinesCount = ItemBlockAdvancedSolarPanelsMeta.names.length;
        this.setCreativeTab(AdvancedSolarPanels.TAB);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        switch (meta) {
            case ADVANCED:
                return new SolarPanels.Advanced();
            case HYBRID:
                return new SolarPanels.Hybrid();
            case ULTIMATE:
                return new SolarPanels.Ultimate();
            case QUANTUM:
                return new SolarPanels.Quantum();
            case SPECTRAL:
                return new SolarPanels.Spectral();
            case SINGULAR:
                return new SolarPanels.Singular();
            case LIGHT_ABSORBING:
                return new SolarPanels.LightAbsorbing();
            case PHOTONIC:
                return new SolarPanels.Photonic();
            case QUANTUM_GENERATOR:
                return new TileEntityQuantumGenerator();
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int id, CreativeTabs tabs, List list) {
        list.add(new ItemStack(Block.stoneDoubleSlab, 1, 0));
        for (int i = 0; i < this.metaMachinesCount; ++i) {
            ItemStack stack = new ItemStack(this, 1, i);
            if (Item.itemsList[this.blockID].getUnlocalizedName(stack) != null) {
                list.add(stack);
            }
        }
    }
}
