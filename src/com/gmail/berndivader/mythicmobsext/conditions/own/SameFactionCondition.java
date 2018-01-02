package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import org.bukkit.entity.Entity;

public class SameFactionCondition 
extends 
AbstractCustomCondition
implements
IEntityComparisonCondition {
    private String[] factions;
    public SameFactionCondition(String line, MythicLineConfig mlc) {
        super(line,mlc);
        this.factions=mlc.getString("faction").split(",");
    }

    @Override
    public boolean check(AbstractEntity c, AbstractEntity t) {
        Entity caster=c.getBukkitEntity();
        Entity target=t.getBukkitEntity();
        if (caster.getEntityId()==target.getEntityId()) return false;
        ActiveMob am=Main.mythicmobs.getMobManager().getMythicMobInstance(caster);
        ActiveMob tam=Main.mythicmobs.getMobManager().getMythicMobInstance(target);
        if (am==null||tam==null) return false;
        return (checkFactions(am,am,t,this.factions)&&checkFactions(am,tam,t,this.factions));
    }

    public static boolean checkFactions(SkillCaster caster, ActiveMob am, AbstractEntity target, String[]factions) {
        for (int a=0;a<factions.length;a++) {
            String f=SkillString.parseMobVariables(factions[a],caster,target,null);
            if (am.hasFaction()&&am.getFaction().equals(f)) {
                return true;
            }
        }
        return false;
    }
}
