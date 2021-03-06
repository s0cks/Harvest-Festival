package joshie.harvest.items;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.util.Translate;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBaseSingle extends Item {
    public ItemBaseSingle() {
        setCreativeTab(HFTab.FARMING);
    }

    public ItemBaseSingle(CreativeTabs tab) {
        setCreativeTab(tab);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerItem(this, name);
        return this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Translate.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}