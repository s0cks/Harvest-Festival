package joshie.harvest.core.util.generic;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Library {
    public static boolean DEBUG_ON;
    private static final Logger LOGGER = LogManager.getLogger("Joshie-Lib");

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
