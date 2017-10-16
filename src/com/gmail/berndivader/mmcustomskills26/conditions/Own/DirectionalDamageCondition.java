package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;
import org.bukkit.util.Vector;

public class DirectionalDamageCondition extends mmCustomCondition
        implements
        IEntityComparisonCondition {

    private RangedDouble angle;

    public DirectionalDamageCondition(String line, MythicLineConfig mlc) {
        super(line, mlc);
        String a=mlc.getString("angle",null);
        if(a!=null)this.angle=new RangedDouble(a);
    }
    @Override
    public boolean check(AbstractEntity s, AbstractEntity t) {
        if(this.angle==null)return false;
        Vector Vvp=s.getBukkitEntity().getLocation().toVector().setY(0);
        Vector Vap=t.getBukkitEntity().getLocation().toVector().setY(0);
        Vector Vvd=s.getBukkitEntity().getLocation().getDirection().setY(0);
        Vector Vd=Vap.subtract(Vvp).normalize();
        Vector VDD=Vd.clone();
        int a=(int)Math.toDegrees(Math.acos(Vd.dot(Vvd)));
        a=VDD.crossProduct(Vvd.multiply(2D).normalize()).getY()<0.0d?-a:a;
        return this.angle.equals((a+450)%360);
    }
}
