package reforged.ic2.addons.asp.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.References;
import reforged.ic2.addons.asp.proxy.ClientProxy;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMolecularTransformer extends BlockContainer implements IItemBlockIDProvider {

    @SideOnly(Side.CLIENT)
    public Icon icon;

    public BlockMolecularTransformer() {
        super(AdvancedSolarPanelsConfig.ASP_TRANSFORMER.get(), Material.iron);
        this.setHardness(3.0F);
        this.setLightValue(1.0F);
        this.setCreativeTab(AdvancedSolarPanels.TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityMolecularTransformer();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon(References.MOD_ID + ":core");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta) {
        return this.icon;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ClientProxy.transformerRenderId;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> dropped = super.getBlockDropped(world, x, y, z, metadata, fortune);
        TileEntity var8 = world.getBlockTileEntity(x, y, z);
        if (var8 instanceof IInventory) {
            IInventory inventory = (IInventory) var8;

            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null) {
                    dropped.add(stack);
                    inventory.setInventorySlotContents(i, null);
                }
            }
        }

        return dropped;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int a, int b) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityBlock) {
            ((TileEntityBlock)te).onBlockBreak(a, b);
        }
        boolean firstItem = true;
        List<ItemStack> drops = this.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        for (ItemStack drop : drops) {
            if (firstItem) {
                firstItem = false;
            } else {
                StackUtil.dropAsEntity(world, x, y, z, drop);
            }
        }
        super.breakBlock(world, x, y, z, a, b);
    }

    @Override
    public int idDropped(int meta, Random random, int i) {
        return this.blockID;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c) {
        if (entityPlayer.isSneaking()) {
            return false;
        } else {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te instanceof IHasGui) {
                return IC2.platform.isRendering() || IC2.platform.launchGui(entityPlayer, (IHasGui) te);
            } else {
                return false;
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving living, ItemStack stack) {
        if (IC2.platform.isSimulating()) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (tileEntity instanceof IWrenchable) {
                IWrenchable te = (IWrenchable) tileEntity;
                if (living == null) {
                    te.setFacing((short) 2);
                }
            }
        }
    }
}
