package joshie.harvest.quests;

import joshie.harvest.api.quest.IQuest;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static joshie.harvest.core.helpers.QuestHelper.getCurrentQuest;

public class QuestEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        HashSet<IQuest> quests = getCurrentQuest(event.getEntityPlayer());
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onEntityInteract(event.getEntityPlayer(), event.getTarget());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        HashSet<IQuest> quests = getCurrentQuest(event.getEntityPlayer());
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onRightClickBlock(event.getEntityPlayer(), event.getPos(), event.getFace());
            }
        }
    }
}