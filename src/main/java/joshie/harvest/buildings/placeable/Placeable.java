package joshie.harvest.buildings.placeable;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Placeable {
    protected int x, y, z;

    public Placeable init() {
        return this;
    }

    public BlockPos getOffsetPos() {
        return new BlockPos(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    public boolean place(World world, BlockPos pos, Direction direction, ConstructionStage stage) {
        if (canPlace(stage)) {
            return place(world, getTransformedPosition(pos, direction), direction);
        } else return false;
    }

    public BlockPos getTransformedPosition(BlockPos pos, Direction direction) {
        BlockPos adjusted = transformBlockPos(direction);
        return new BlockPos(pos.getX() + adjusted.getX(), pos.getY() + adjusted.getY(), pos.getZ() + adjusted.getZ());
    }

    private BlockPos transformBlockPos(Direction direction) {
        int i = getX();
        int j = getY();
        int k = getZ();
        boolean flag = true;

        switch (direction.getMirror()) {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        switch (direction.getRotation())  {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return flag ? new BlockPos(i, j, k) : getOffsetPos();
        }
    }

    public boolean place (World world, BlockPos pos, Direction direction) {
        return false;
    }

    public enum ConstructionStage {
        BUILD, PAINT, DECORATE, MOVEIN, FINISHED;
    }
}