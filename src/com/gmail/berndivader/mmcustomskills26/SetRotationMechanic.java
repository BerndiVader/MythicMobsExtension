package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class SetRotationMechanic extends SkillMechanic
implements
ITargetedEntitySkill{
    private float yawOff;
    private float pitchOff;
    private double adjust;

    public SetRotationMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.yawOff=mlc.getFloat(new String[]{"yawoffset","yaw","yo"},0.0F);
        this.pitchOff=mlc.getFloat(new String[]{"pitchoffset","pitch","po"},0.0F);
        this.adjust=mlc.getDouble("adjust",0);

    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        LivingEntity e = (LivingEntity) data.getCaster().getEntity().getBukkitEntity();
        Location l = e.getLocation().clone();
        l.add(0,this.adjust,0);
        float yaw=target.getBukkitEntity().getLocation().getYaw();
        float pitch=target.getBukkitEntity().getLocation().getPitch();
        yaw+=this.yawOff;
        pitch+=this.pitchOff;
        Main.getPlugin().getNMSUtils().setRotationNBT(e,yaw);
        return true;
    }
}
