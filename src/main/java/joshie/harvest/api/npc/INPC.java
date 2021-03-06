package joshie.harvest.api.npc;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.npc.gift.Gifts.Quality;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public interface INPC extends IRelatable {
    /** Assigns a shop to this NPC **/
    public INPC setShop(IShop shop);

    /** Marks this NPC as a builder NPC **/
    public INPC setIsBuilder();

    /** Set the height of this npc for rendering purposes **/
    public INPC setHeight(float height, float offset);

    /** Marks this NPCs home as this building group, with this name 
     * @return **/
    public INPC setHome(IBuilding building, String name);

    /** Set whether this npc uses alex skin or not */
    INPC setAlexSkin();

    /** Returns this height of this NPC
     *  This is for rendering purposes **/
    public float getHeight();

    /** Returns this render offset for this NPC**/
    public float getOffset();
    
    /** Returns the unlocalised name for this NPC **/
    public String getUnlocalizedName();

    /** Returns the localised name for this NPC **/
    public String getLocalizedName();

    /** Returns the birthday of this npc **/
    public ICalendarDate getBirthday();

    /** Whether the npc is considered a child **/
    public boolean isChild();

    /** Return the bedtime for this npc, 0-24000 **/
    public int getBedtime();

    /** Returns this shop that is associated with this npc
     *  the shop can be null, if there is no shop. */
    public IShop getShop();

    /** Returns a random greeting for this npc 
     * @param player **/
    public String getGreeting(EntityPlayer player);

    /** What this NPC says when they accept a marriage proposal **/
    public String getAcceptProposal();

    /** What this NPC says when they reject a marriage proposal **/
    public String getRejectProposal();

    /** Returns a gift thank you message, based on the quality **/
    public String getThanks(Quality value);

    /** The message this npc returns when they've already been gifted **/
    String getNoThanks();

    /** Returns this quality of this gift **/
    public Quality getGiftValue(ItemStack gift);

    /** Returns the home for this npc
     *  @ResourceLocation = Building ResourceLocation
     *  @String = The string name of the location of the building**/
    public Pair<ResourceLocation, String> getHome();

    /** Returns true if this npc can be married **/
    public boolean isMarriageCandidate();

    /** Returns true if this npc is a builder **/
    public boolean isBuilder();

    /** Return the inside color **/
    public int getInsideColor();
    
    /** Return the outside color **/
    public int getOutsideColor();

    /** Returns true if this npc uses alex skin **/
    boolean isAlexSkin();
}