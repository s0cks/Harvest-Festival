package joshie.harvest.player.stats;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.core.handlers.HFTrackers;

public class StatData {
    protected ICalendarDate birthday = HFApi.calendar.newDate(0, null, 0);
    protected double staminaMax = 100D;
    protected double fatigueMin = 0D;
    protected double stamina = 100D;
    protected double fatigue = 0D;
    protected long gold;

    public ICalendarDate getBirthday() {
        return birthday;
    }

    public void affectStats(double stamina, double fatigue) {
        this.stamina += stamina;
        this.fatigue += fatigue;

        if (this.stamina >= staminaMax) {
            this.stamina = staminaMax;
        }

        if (this.fatigue <= fatigueMin) {
            this.fatigue = fatigueMin;
        }
        
        HFTrackers.markDirty();
    }

    public boolean isBirthdaySet() {
        return birthday.getSeason() != null && birthday.getDay() != 0 && birthday.getYear() != 0;
    }

    public boolean setBirthday() {
        if (!isBirthdaySet()) {
            birthday = HFApi.calendar.cloneDate(HFApi.calendar.getToday());
            HFTrackers.markDirty();
            return true;
        } else return false;
    }
    
    public void setBirthday(ICalendarDate newDate) {
        birthday = newDate;
    }

    public void setStats(double stamina, double fatigue, double staminaMax, double fatigueMin) {
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.staminaMax = staminaMax;
        this.fatigueMin = fatigueMin;   
    }

    public long getGold() {
        return gold;
    }

    public double getStamina() {
        return stamina;
    }

    public double getFatigue() {
        return fatigue;
    }

    public double getStaminaMax() {
        return staminaMax;
    }

    public double getFatigueMin() {
        return fatigueMin;
    }
}
