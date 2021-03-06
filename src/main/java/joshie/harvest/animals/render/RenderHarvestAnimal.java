package joshie.harvest.animals.render;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderHarvestAnimal extends RenderLiving<EntityLiving> {
    private ResourceLocation texture_child;
    private ResourceLocation texture_adult;
    private ResourceLocation texture_sheared;

    public RenderHarvestAnimal(RenderManager manager, ModelBase model, String animal) {
        super(manager, model, 1F);

        texture_child = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + "_child.png");
        texture_adult = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + "_adult.png");

        if (animal.equals("sheep")) {
            texture_sheared = new ResourceLocation(HFModInfo.MODID, "textures/entity/" + animal + "_adult_sheared.png");
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLiving entityLiving) {
        if (texture_sheared != null) {
            EntitySheep sheep = (EntitySheep) entityLiving;
            if (sheep.isChild()) return texture_child;
            else if (sheep.getSheared()) return texture_sheared;
            else return texture_adult;
        }

        return entityLiving.isChild() ? texture_child : texture_adult;
    }
}