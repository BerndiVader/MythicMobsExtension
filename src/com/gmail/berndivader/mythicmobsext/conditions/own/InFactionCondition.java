package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.Main;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import org.bukkit.entity.Entity;


public class InFactionCondition 
extends 
AbstractCustomCondition
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
        ActiveMob am=Main.mythicmobs.getMobManager().getMythicMobInstance(target);
        if (am!=null) return SameFactionCondition.checkFactions(am,am,t,this.factions);
        return false;
    }
}
