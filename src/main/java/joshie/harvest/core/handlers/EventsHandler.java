package joshie.harvest.core.handlers;

import joshie.harvest.core.config.Calendar;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventsHandler {
    //Setup the Server
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote && world.provider.getDimension() == 0) {
            HFTrackers.resetServer(world);
        }
    }

    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
            HFTrackers.resetClient();
        }
    }

    //Server tick for new day
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.END) return;
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        if (world.getTotalWorldTime() % Calendar.TICKS_PER_DAY == 0) {
            newDay(world, false);
        }
    }

    //New day
    public static void newDay(final World world, final boolean forced) {
        int daysPassed = CalendarHelper.getTotalDays(HFTrackers.getCalendar().getDate());
        int serverDays = (int) Math.floor(world.getWorldTime() / Calendar.TICKS_PER_DAY);
        if (daysPassed <= serverDays || forced) {
            HFTrackers.getCalendar().newDay(world, CalendarHelper.getTime(world));
        }
    }

    //Sync data on login
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            HFTrackers.getPlayerTracker(player).getStats().setBirthday();
            PacketHandler.sendToClient(new PacketSetCalendar(HFTrackers.getCalendar().getDate()), player);
            HFTrackers.getTownTracker().syncToPlayer(player);
            PlayerTrackerServer data = HFTrackers.getServerPlayerTracker(player);
            data.syncPlayerStats(player);
        }
    }
}