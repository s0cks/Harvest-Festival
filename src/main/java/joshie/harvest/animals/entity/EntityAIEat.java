package joshie.harvest.animals.entity;

import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEat extends EntityAIBase {
    private World worldObj;
    private EntityAnimal animal;
    private IAnimalTracked tracked;

    public EntityAIEat(IAnimalTracked animal) {
        this.worldObj = animal.getData().getAnimal().worldObj;
        this.animal = animal.getData().getAnimal();
        this.tracked = animal;
    }

    @Override
    public boolean shouldExecute() {
        return tracked.getData().isHungry();
    }

    @Override
    public boolean continueExecuting() {
        return tracked.getData().isHungry();
    }

    @Override
    public void updateTask() {
        BlockPos position = new BlockPos(animal).add(3 - worldObj.rand.nextInt(7), 0, 3 - worldObj.rand.nextInt(7));
        IBlockState state = animal.worldObj.getBlockState(position);
        Block block = state.getBlock();
        if (block instanceof IAnimalFeeder) {
            if (((IAnimalFeeder) block).canFeedAnimal(tracked, worldObj, position)) {
                tracked.getData().feed(null);
            }
        }
    }
}