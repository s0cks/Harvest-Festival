package joshie.harvest.core.util;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.util.generic.IFaceable;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CodeGeneratorBuildings {

    private World world;
    private int x1, y1, z1, x2, y2, z2;

    public CodeGeneratorBuildings(World world, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        this.world = world;
        this.x1 = xStart < xEnd ? xStart : xEnd;
        this.x2 = xStart < xEnd ? xEnd : xStart;
        this.y1 = yStart < yEnd ? yStart : yEnd;
        this.y2 = yStart < yEnd ? yEnd : yStart;
        this.z1 = zStart < zEnd ? zStart : zEnd;
        this.z2 = zStart < zEnd ? zEnd : zStart;
    }

    public ArrayList<Entity> getEntities(Class clazz, int x, int y, int z) {
        return (ArrayList<Entity>) world.getEntitiesWithinAABB(clazz, new AxisAlignedBB(new BlockPos(x, y, z)));
    }

    public void getCode(boolean air) {
        if (!world.isRemote) {
            ArrayList<Placeable> ret = new ArrayList();
            Set all = new HashSet();
            int i = 0;
            for (int y = 0; y <= y2 - y1; y++) {
                for (int x = 0; x <= x2 - x1; x++) {
                    for (int z = 0; z <= z2 - z1; z++) {

                        Set<Entity> entityList = new HashSet();
                        entityList.addAll(getEntities(EntityPainting.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityItemFrame.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPC.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPCBuilder.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityNPCShopkeeper.class, x1 + x, y1 + y, z1 + z));

                        BlockPos position = new BlockPos(x1 + x, y1 + y, z1 + z);
                        IBlockState state = world.getBlockState(position);
                        Block block = state.getBlock();
                        if (block == Blocks.CHEST) {
                            TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(x1 + x, y1 + y, z1 + z));
                            String name = chest.getName();
                            String field = name;
                            if (name.startsWith("npc.")) {
                                field = name.replace("npc.", "");
                                INPC npc = HFApi.npc.get(field);
                                String npcField = npc == null ? "" : npc.getUnlocalizedName();
                                ret.add(new PlaceableNPC(field, npcField, x, y, z));
                                ret.add(new PlaceableBlock(Blocks.AIR.getDefaultState(), x, y, z));
                                continue;
                            }
                        }

                        if ((block != Blocks.AIR || air || entityList.size() > 0) && block != Blocks.END_STONE) {
                            int meta = state.getBlock().getMetaFromState(state);
                            if (block == Blocks.DOUBLE_PLANT && meta >= 8) continue;
                            TileEntity tile = world.getTileEntity(position);
                            if (tile instanceof IFaceable) {
                                //ret.add(PlaceableHelper.getPlaceableIFaceableString((IFaceable) tile, state, new BlockPos(x, y, z)));
                            } else if (tile instanceof TileEntitySign) {
                                ITextComponent[] text = ((TileEntitySign) tile).signText;
                                if (block == Blocks.STANDING_SIGN) {
                                    ret.add(PlaceableHelper.getFloorSignString(text, state, new BlockPos(x, y, z)));
                                } else ret.add(PlaceableHelper.getWallSignString(text, state, new BlockPos(x, y, z)));
                            } else {
                                Placeable text = PlaceableHelper.getPlaceableBlockString(state, x, y, z);
                                ret.add(text);
                            }

                            //Entities
                            if (entityList.size() > 0) {
                                for (Entity e : entityList) {
                                    if (!all.contains(e)) {
                                        ret.add(PlaceableHelper.getPlaceableEntityString(e, x, y, z));
                                        all.add(e);
                                    }
                                }
                            }

                            i++;
                        }
                    }
                }
            }

            Building building = new Building();
            building.components = new Placeable[ret.size()];
            for (int j = 0; j < ret.size(); j++) {
                building.components[j] = ret.get(j);
            }

            try {
                String json = BuildingRegistry.getGson().toJson(building);
                PrintWriter writer = new PrintWriter("building.json", "UTF-8");
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}