package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.ItemHFBase;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;

public class ItemCrop extends ItemHFBase implements IShippable, ICropProvider, ICreativeSorted {
    private ICrop crop;

    public ItemCrop(ICrop crop) {
        setCreativeTab(HFTab.FARMING);
        this.crop = crop;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (crop == HFCrops.grass) return 0;
        else {
            return crop.getSellValue(stack);
        }
    }

    @Override
    public ICrop getCrop(ItemStack stack) {
        return crop;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return crop.getLocalizedName(true);
    }

    @Override
    public String getName(ItemStack stack) {
        return crop.getResource().getResourcePath();
    }
}