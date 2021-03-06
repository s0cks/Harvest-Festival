package joshie.harvest.api.shops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface IPurchaseable extends ISpecialPurchaseRules {
    /** Whether this item shows up in the shop gui for purchase **/
    public boolean canList(World world, EntityPlayer player);
    
    /** The cost of this product **/
    public long getCost();
    
    /** This is the itemstack that gets displayed in the shop view **/
    public ItemStack getDisplayStack();
    
    /** Called whenever this item is purchased
     *  @param      player the player purchasing the item
     *  @return     return true if the gui should close after a purchase **/
    public boolean onPurchased(EntityPlayer player);

    /** Display tooltip for this item **/
    public void addTooltip(List<String> list);
}