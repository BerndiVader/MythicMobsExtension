package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class DamagerComesFromCondition extends mmCustomCondition
        implements
        IEntityComparisonCondition {

    public DamagerComesFromCondition(String line, MythicLineConfig mlc) {
        super(line, mlc);
    }

    @Override
    public boolean check(AbstractEntity vic, AbstractEntity atk) {
        Location Lv=vic.getBukkitEntity().getLocation();
        Location La=atk.getBukkitEntity().getLocation();
        Vector r = La.toVector().subtract(Lv.toVector()).normalize();
        Vector f = Lv.getDirection();
        Double a = Math.toDegrees(Math.acos(r.dot(f)));
        return false;
    }
}
