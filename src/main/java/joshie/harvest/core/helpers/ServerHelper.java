package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.handlers.ServerHandler;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.mining.MineTrackerServer;
import joshie.harvest.player.PlayerDataServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ServerHelper {
    private static ServerHandler theServer;

    public static boolean isSet() {
        return theServer != null;
    }

    public static void setServer(World world) {
        theServer = (new ServerHandler(world));
    }

    public static void markDirty() {
        theServer.markDirty();
    }

    static CalendarServer getCalendar() {
        return theServer.getCalendar();
    }

    static AnimalTrackerServer getAnimalTracker() {
        return theServer.getAnimalTracker();
    }

    static CropTrackerServer getCropTracker() {
        return theServer.getCropTracker();
    }

    static MineTrackerServer getMineTracker() {
        return theServer.getMineTracker();
    }

    static PlayerDataServer getPlayerData(EntityPlayer player) {
        return theServer.getPlayerData(player);
    }

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    static PlayerDataServer getPlayerData(UUID uuid) {
        return theServer.getPlayerData(uuid);
    }

    static void removeAllRelations(EntityLivingBase entity) {
        theServer.removeAllRelations(entity);
    }
}