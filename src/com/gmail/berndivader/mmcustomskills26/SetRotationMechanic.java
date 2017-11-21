package com.gmail.berndivader.mmcustomskills26;

import com.gmail.berndivader.NMS.NMSUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetRotationMechanic extends SkillMechanic
implements
ITargetedEntitySkill{
    private float yawOff,pitchOff;

    public SetRotationMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.yawOff=mlc.getFloat(new String[]{"yawoffset","yaw","yo"},5.0F);
        this.pitchOff=mlc.getFloat(new String[]{"pitchoffset","pitch","po"},0.0F);
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        float yaw=target.getBukkitEntity().getLocation().getYaw();
        float pitch=target.getBukkitEntity().getLocation().getPitch();
        yaw+=this.yawOff;
        pitch+=this.pitchOff;
        NMSUtils.setYawPitch(target.getBukkitEntity(), yaw, pitch);
        Main.getPlugin().getVolatileHandler().rotateEntityPacket(target.getBukkitEntity(),yaw,pitch);
        return true;
    }
}
