package joshie.harvest.core.config;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class Animals {
    public static boolean CAN_SPAWN;
    public static boolean DISABLE_SPAWN_CHICKEN;
    
    public static int MAX_LITTER_SIZE;
    public static int LITTER_EXTRA_CHANCE;
    public static int PREGNANCY_TIMER;
    public static int AGING_TIMER;
    public static int CHICKEN_TIMER;

    public static void init() {
        CAN_SPAWN = getBoolean("Enable Animal Spawning", false);
        DISABLE_SPAWN_CHICKEN = getBoolean("Disable Chickens from Eggs", false);
        PREGNANCY_TIMER = getInteger("Pregnancy: Number of days", 7);
        CHICKEN_TIMER = PREGNANCY_TIMER / 2;
        MAX_LITTER_SIZE = getInteger("Pregnancy: Max litter size", 5);
        LITTER_EXTRA_CHANCE = getInteger("Pregnancy: Chance of extra birth", 4);
        AGING_TIMER = getInteger("Maturity: Number of days", 14);
    }
}
