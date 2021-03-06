package joshie.harvest.blocks;

import joshie.harvest.blocks.BlockDirt.Types;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockDirt extends BlockHFBaseEnum<Types> {
    public enum Types implements IStringSerializable {
        REAL, DECORATIVE;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockDirt() {
        super(Material.GROUND, Types.class, HFTab.MINING);
        setSoundType(SoundType.GROUND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == 1) list.add(Translate.translate("tooltip.dirt"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (General.DEBUG_MODE) {
            super.getSubBlocks(item, tab, list);
        }
    }

    //TECHNICAL/
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        switch (getEnumFromState(state)) {
            case REAL:
                return -1.0F;
            case DECORATIVE:
                return 4F;
            default:
                return 4F;
        }
    }

    @Override
    public int getToolLevel(Types types) {
        return 2;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        switch (getEnumFromState(world.getBlockState(pos))) {
            case REAL:
                return 6000000.0F;
            case DECORATIVE:
                return 12.0F;
            default:
                return 5;
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<ItemStack>();
        if (getEnumFromState(world.getBlockState(pos)) == Types.DECORATIVE) {
            ret.add(new ItemStack(HFBlocks.DIRT, 1, 1));
        }

        return ret;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    //MINE STUFF
    
    /*
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int side) {
        if (world.rand.nextInt(3) == 0) {
            //MineHelper.caveIn(world, x, y, z);
        }
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
        */

    //Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors
    //private static int MAXIMUM_FLOORS = 23;

}