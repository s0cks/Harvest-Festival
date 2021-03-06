package joshie.harvest.mining.data;

import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.mining.MineTrackerServer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashSet;

public class MineLevel {
    private HashSet<MineBlock> blocks = new HashSet<MineBlock>();
    private boolean isCavedIn;
    private int number;

    public MineLevel() {
    }

    public MineLevel(int number) {
        this.number = number;
    }

    public void newDay() {
        if (isCavedIn) return;
        for (MineBlock block : blocks) {
            block.newDay(number);
        }
    }

    public boolean isCavedIn() {
        return isCavedIn;
    }

    public int get() {
        return number;
    }

    public void complete(World world, BlockPos pos, Mine mine, ArrayList<PlaceableBlock> list) {
        for (PlaceableBlock block : list) {

            MineBlock location = null;
            if (block.getBlock() == Blocks.AIR) {
                location = new MineAir(world.provider.getDimension(), pos);
            } else if (block.getBlock() == HFBlocks.DIRT) {
                location = new MineFloor(world.provider.getDimension(), pos);
            } else if (block.getBlock() == HFBlocks.STONE) {
                location = new MineWall(world.provider.getDimension(), pos);
            }

            blocks.add(location); //Add to this list for iteration
            MineTrackerServer.map.put(location, new MineData(mine, this)); //Register this block to the mappings
        }
    }

    public void destroy() {
        isCavedIn = true;
        for (MineBlock block : blocks) {
            World world = DimensionManager.getWorld(block.dimension);
            if (world.rand.nextInt(64) == 0) {
                world.setBlockState(block.position, Blocks.COBBLESTONE.getDefaultState());
            } else world.setBlockState(block.position, Blocks.GRAVEL.getDefaultState());
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        isCavedIn = nbt.getBoolean("IsCavedIn");
        number = nbt.getInteger("Number");
        NBTTagList block = nbt.getTagList("Blocks", 10);
        for (int i = 0; i < block.tagCount(); i++) {
            NBTTagCompound tag = block.getCompoundTagAt(i);
            String type = tag.getString("BlockType");
            MineBlock b = null;
            if (type.equals("air")) {
                b = new MineAir();
                b.readFromNBT(tag);
            } else if (type.equals("wall")) {
                b = new MineWall();
                b.readFromNBT(tag);
            } else if (type.equals("floor")) {
                b = new MineFloor();
                b.readFromNBT(tag);
            }

            blocks.add(b);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("IsCavedIn", isCavedIn);
        nbt.setInteger("Number", number);
        NBTTagList block = new NBTTagList();
        for (MineBlock b : blocks) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("BlockType", b.getType());
            b.writeToNBT(tag);
            block.appendTag(tag);
        }

        nbt.setTag("Blocks", block);
    }
}