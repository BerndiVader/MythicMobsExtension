package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class LookingAtMeCondition extends mmCustomCondition
        implements
        IEntityComparisonCondition {
    private double yO,FOV;
    private boolean debug;

    public LookingAtMeCondition(String line, MythicLineConfig mlc) {
        super(line, mlc);
        this.yO=mlc.getDouble(new String[]{"yoffset","y","yo","o"},-0.4D);
        this.FOV=mlc.getDouble("fov",1.999D);
        this.debug=mlc.getBoolean("debug",false);
    }

    @Override
    public boolean check(AbstractEntity caster, AbstractEntity target) {
        if (caster.isLiving() && target.isLiving()) {
            Vector Vcaster = ((LivingEntity)caster.getBukkitEntity()).getEyeLocation().toVector();
            Vcaster.setY(Vcaster.getY()+this.yO);
            Vector Vtarget = ((LivingEntity)target.getBukkitEntity()).getEyeLocation().toVector();
            Vector Vdirection = target.getBukkitEntity().getLocation().getDirection();
            Vector delta = Vtarget.subtract(Vcaster);
            double angle = delta.angle(Vdirection);
            if (this.debug) {
                Main.logger.info("fov-ratio:"+Double.toString(angle));
                Main.logger.info("yVecOff:::"+Double.toString(Vcaster.getY()));
            }
            if (angle>this.FOV) return true;
        }
        return false;
    }
}
