package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.stats.StatData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.Random;

public class PlayerHelper {
    private static final Random rand = new Random();

    /**
     * Should always be called client and server side
     **/
    public static void performTask(EntityPlayer player, ItemStack stack, double amount) {
        ToolHelper.levelTool(stack);

        if (player.capabilities.isCreativeMode) return; //If the player is in creative don't exhaust them
        StatData stats = HFTrackers.getPlayerTracker(player).getStats();
        double stamina = stats.getStamina();
        double fatigue = stats.getFatigue();
        boolean affectFatigue = false;
        boolean affectStamina = false;

        if (stamina >= 1) {
            affectStamina = true;
        } else if (fatigue < 255) {
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 4, true, true));
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 0, true, true));
            if (rand.nextInt((int) (Math.max(1, 255 - fatigue))) == 0) {
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 250, 1, true, true));
            }

            if (fatigue > 250) {
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 500, 1, true, true));
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 750, 1, true, true));
            }

            affectFatigue = true;
        } else {
            //If the players fatigue > 255, Make them faint
            if (!player.worldObj.isRemote) {
                // MinecraftServer.getServer().getConfigurationManager().respawnPlayer((EntityPlayerMP) player, player.worldObj.provider.dimensionId, true);
            }

            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 500, 1, true, true));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 750, 1, true, true));

            fatigue = -fatigue + 100;
            stamina = -stamina + 20;
            stats.affectStats(stamina, fatigue);
        }

        stats.affectStats(affectStamina ? -amount : 0D, affectFatigue ? amount : 0D);
    }
}