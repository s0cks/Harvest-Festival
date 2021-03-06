package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gift.Gifts.Quality;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class GuiNPCGift extends GuiNPCChat {
    private ItemStack gift;
    private Quality value;

    public GuiNPCGift(EntityNPC npc, EntityPlayer player, EnumHand hand) {
        super(npc, player, -1);
        gift = player.getHeldItem(hand).copy();
        value = npc.getNPC().getGiftValue(gift);
    }

    @Override
    public String getScript() {
        if (ToolHelper.isBlueFeather(gift)) {
            int relationship = HFApi.relations.getAdjustedRelationshipValue(player, npc.getNPC());
            if (relationship >= NPC.marriageRequirement && npc.getNPC().isMarriageCandidate()) {
                return npc.getNPC().getAcceptProposal();
            } else return npc.getNPC().getRejectProposal();
        }

        if (HFTrackers.getClientPlayerTracker().getRelationships().gift(player, npc.getRelatable(), value.getRelationPoints())) {
            return npc.getNPC().getThanks(value);
        } else return npc.getNPC().getNoThanks();
    }
}