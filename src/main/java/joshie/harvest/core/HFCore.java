package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.AnimalEvents;
import joshie.harvest.calendar.CalendarRender;
import joshie.harvest.core.commands.*;
import joshie.harvest.core.handlers.*;
import joshie.harvest.core.network.*;
import joshie.harvest.core.network.animals.PacketSyncDaysNotFed;
import joshie.harvest.core.network.animals.PacketSyncEverything;
import joshie.harvest.core.network.animals.PacketSyncHealthiness;
import joshie.harvest.core.network.animals.PacketSyncProductsProduced;
import joshie.harvest.core.network.quests.*;
import joshie.harvest.core.render.RenderHandler;
import joshie.harvest.core.util.WorldDestroyer;
import joshie.harvest.quests.QuestEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFCore {
    public static void preInit() {
        //Register Events
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new GoddessHandler());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        MinecraftForge.EVENT_BUS.register(new DisableHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());

        //Commands
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        CommandManager.INSTANCE.registerCommand(new HFCommandHelp());
        CommandManager.INSTANCE.registerCommand(new HFCommandGold());
        CommandManager.INSTANCE.registerCommand(new HFCommandSeason());
        CommandManager.INSTANCE.registerCommand(new HFCommandDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandYear());
        CommandManager.INSTANCE.registerCommand(new HFCommandNewDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandWeather());

        //Register Packets
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncForecast.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncRelationship.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncMarriage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGifted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncStats.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCooking.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncOrientation.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncBirthday.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketPurchaseItem.class, Side.SERVER);
        PacketHandler.registerPacket(PacketFreeze.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncMarker.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncFridge.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketWateringCan.class, Side.SERVER);
        PacketHandler.registerPacket(PacketDismount.class, Side.SERVER);

        //Animal Packets
        PacketHandler.registerPacket(PacketSyncEverything.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncHealthiness.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncDaysNotFed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncProductsProduced.class, Side.CLIENT);

        //Quest Packets
        PacketHandler.registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestStart.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.SERVER);
    }

    public static void postInit() {
        WorldDestroyer.replaceWorldProvider();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        HFTrackers.resetClient();
        MinecraftForge.EVENT_BUS.register(new CalendarRender());
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
    }
}
