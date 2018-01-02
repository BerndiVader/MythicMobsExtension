package com.gmail.berndivader.mythicmobsext;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetRotationMechanic extends SkillMechanic
implements
ITargetedEntitySkill{
	public static String str="mmRotate";
    private float yawOff,pitchOff;
    private long d;

    public SetRotationMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.yawOff=mlc.getFloat(new String[]{"yawoffset","yaw","yo"},5.0F);
        this.pitchOff=mlc.getFloat(new String[]{"pitchoffset","pitch","po"},0F);
        this.d=mlc.getLong(new String[]{"duration","dur"},1);
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) return false;
		if (target.getBukkitEntity().hasMetadata(str)) target.getBukkitEntity().removeMetadata(str, Main.getPlugin());
    	final float yo=this.yawOff,po=this.pitchOff;
		final long d=this.d;
		target.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
    	new BukkitRunnable() {
            float yaw=target.getBukkitEntity().getLocation().getYaw();
            float pitch=target.getBukkitEntity().getLocation().getPitch();
			long c=0;
			@Override
			public void run() {
				if (c>d||target.isDead()||!target.getBukkitEntity().hasMetadata(str)) {
			        if (!target.isDead()) {
						target.getBukkitEntity().removeMetadata(str, Main.getPlugin());
			        }
					this.cancel();
				} else {
			        yaw=CustomSkillStuff.normalise(yaw+yo,0,360);
			        pitch=CustomSkillStuff.normalise(pitch+po,0,360);
			        Main.getPlugin().getVolatileHandler().rotateEntityPacket(target.getBukkitEntity(),yaw,pitch);
				}
		        c++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);;
        return true;
    }
    
}
