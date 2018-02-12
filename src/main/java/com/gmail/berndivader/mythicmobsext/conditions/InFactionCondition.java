package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.ConditionAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ConditionAnnotation(name="infaction",author="BerndiVader")
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
        ActiveMob am=Utils.mobmanager.getMythicMobInstance(target);
        if (am!=null) return SameFactionCondition.checkFactions(am,am,t,this.factions);
        return false;
    }
}
