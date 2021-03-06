package joshie.harvest.buildings.placeable.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceablePainting extends PlaceableHanging {
    private String painting;

    public PlaceablePainting() {}
    public PlaceablePainting(String name, EnumFacing facing, int x, int y, int z) {
        super(facing, x, y, z);
        this.painting = name;
    }

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        return new EntityPainting(world, pos, facing, painting);
    }

    @Override
    public PlaceablePainting getCopyFromEntity(Entity e, int x, int y, int z) {
        EntityPainting p = (EntityPainting) e;

        return new PlaceablePainting(p.art.title, p.facingDirection, x, y, z);
    }
}