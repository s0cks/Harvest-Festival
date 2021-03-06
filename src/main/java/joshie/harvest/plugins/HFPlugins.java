package joshie.harvest.plugins;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.generic.ConfigHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;

public class HFPlugins {
    public static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
    private static ArrayList<PluginData> data = new ArrayList<PluginData>();

    public static abstract class Plugin {
        public String modid;

        public Plugin() {
        }

        public void loadConfig(Configuration config) {
        }

        public void preInit() {
        }

        public void init() {
        }

        public void postInit() {
        }
    }

    static {
        //data.add(new PluginData("harvestcraft", HarvestCraft.class));
        //data.add(new PluginData("bettersleeping", BetterSleeping.class));
        //data.add(new PluginData("AgriCraft", AgriCraft.class));
        //data.add(new PluginData("Thaumcraft", Thaumcraft.class));
    }

    private static class PluginData {
        String name;
        Class<? extends Plugin> clazz;

        public PluginData(String name, Class<? extends Plugin> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }

    private static Plugin get(PluginData p) {
        try {
            Plugin plugin = p.clazz.newInstance();
            plugin.modid = p.name;
            plugins.add(plugin);
        } catch (Exception e) {
            HarvestFestival.LOGGER.log(Level.INFO, "Failed to load plugin for modid: " + p.name);
        }

        return null;
    }

    public static void loadConfigs() {
        Configuration config = new Configuration(new File(HarvestFestival.root, "/plugins.cfg"));
        for (PluginData p : data) {
            if (Loader.isModLoaded(p.name)) { //If the mod is loaded
                try {
                    config.load();
                    ConfigHelper.setConfig(config);
                    if (config.get("Enabled", p.name, true).getBoolean(true)) {
                        Plugin plugin = get(p);
                        if (plugin != null) {
                            ConfigHelper.setCategory(p.name);
                            plugin.loadConfig(config);
                        }
                    }
                } catch (Exception e) {
                    HarvestFestival.LOGGER.log(Level.ERROR, HFModInfo.MODNAME + " failed to load in it's " + p.name + " config");
                    e.printStackTrace();
                } finally {
                    config.save();
                }
            }
        }

        //Clear out the data
        data = null;
    }

    public static void preInit() {
        for (Plugin plugin : plugins) {
            try {
                plugin.preInit();
            } catch (Exception e) {
                HarvestFestival.LOGGER.log(Level.INFO, "Failed to load plugin for modid @ PreINIT: " + plugin.modid);
                e.printStackTrace();
            }
        }
    }

    public static void init() {
        for (Plugin plugin : plugins) {
            try {
                plugin.init();
            } catch (Exception e) {
                HarvestFestival.LOGGER.log(Level.INFO, "Failed to load plugin for modid @ INIT: " + plugin.modid);
                e.printStackTrace();
            }
        }
    }

    public static void postInit() {
        for (Plugin plugin : plugins) {
            try {
                plugin.postInit();
            } catch (Exception e) {
                HarvestFestival.LOGGER.log(Level.INFO, "Failed to load plugin for modid @ PostINIT: " + plugin.modid);
                e.printStackTrace();
            }
        }

        //Clear out the plugins as we should no longer need them loaded
        plugins = null;
    }

    public static boolean AGRICRAFT_LOADED = false;
    public static boolean THAUMCRAFT_LOADED = false;
}