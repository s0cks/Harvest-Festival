package joshie.harvest.core.util.base;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

import java.util.HashMap;

public class FMLDefinition<E extends Impl<E>> implements ItemMeshDefinition {
    private FMLControlledNamespacedRegistry<E> registry;
    private HashMap<E, ModelResourceLocation> models = new HashMap<>();

    public FMLDefinition(FMLControlledNamespacedRegistry<E> registry) {
        this.registry = registry;
    }

    public void register(E recipe, ModelResourceLocation resource) {
        models.put(recipe, resource);
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return models.get(registry.getObjectById(stack.getItemDamage()));
    }
}
