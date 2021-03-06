package joshie.harvest.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import scala.Immutable;

public class SafeStack implements Immutable {
    public Item item;
    public int damage;

    public SafeStack(Item item, int damage) {
        this.item = item;
        this.damage = damage;
    }

    public SafeStack(ItemStack stack) {
        this.item = stack.getItem();
        this.damage = stack.getItemDamage();
    }

    public ItemStack getItem() {
        return new ItemStack(item, 1, damage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof ItemStack) {
            ItemStack stack = ((ItemStack)obj);
            if (damage != OreDictionary.WILDCARD_VALUE && damage != stack.getItemDamage()) return false;
            return item.equals(stack.getItem());
        } else if (obj instanceof SafeStack) {
            SafeStack stack = ((SafeStack)obj);
            if (damage != OreDictionary.WILDCARD_VALUE && damage != stack.damage) return false;
            return item.equals(stack.item);
        } else return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + damage;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        return result;
    }
}