package joshie.harvest.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;

import java.util.Collection;
import java.util.HashMap;

public class NPCRegistry implements INPCRegistry {
    private final HashMap<String, INPC> npcs = new HashMap<>();

    @Override
    public Collection<INPC> getNPCs() {
        return npcs.values();
    }

    @Override
    public INPC get(String string) {
        return npcs.get(string);
    }

    @Override
    public INPC register(INPC npc) {
        npcs.put(npc.getUnlocalizedName(), npc);
        return npc;
    }

    @Override
    public INPC register(String unlocalised, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor) {
        return register(new NPC(unlocalised, gender, age, HFApi.calendar.newDate(dayOfBirth, seasonOfBirth, 1), insideColor, outsideColor));
    }
}