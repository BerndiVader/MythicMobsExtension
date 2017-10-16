package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.Main;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import org.bukkit.entity.Entity;


public class InFactionCondition extends mmCustomCondition
implements
        IEntityCondition {
    private String[]factions;
    public InFactionCondition(String line, MythicLineConfig mlc) {
        super(line,mlc);
        this.factions=mlc.getString("faction").split(",");
    }

    @Override
    public boolean check(AbstractEntity t) {
        Entity target=t.getBukkitEntity();
        ActiveMob am= Main.mythicmobs.getMobManager().getMythicMobInstance(target);
        if (am!=null) return SameFactionCondition.checkFactions(am,am,t,this.factions);
        return false;
    }
}
