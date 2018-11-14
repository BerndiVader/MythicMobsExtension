package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="stun",author="BerndiVader")
public class StunMechanic 
extends
SkillMechanic 
implements 
ITargetedEntitySkill {

	public static String str="mmStunned";
	private int duration;
	private boolean f;
	private boolean g;
	private boolean ai,useDuration;

	public StunMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.duration=mlc.getInteger(new String[] { "duration", "dur" },120);
		this.f=mlc.getBoolean(new String[] { "facing", "face", "f" },false);
		this.g=mlc.getBoolean(new String[] { "gravity", "g" },false);
		this.ai=mlc.getBoolean(new String[] { "stopai", "ai" },false);
		this.useDuration=mlc.getBoolean("useDuration", true);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity().hasMetadata(str)) target.getBukkitEntity().removeMetadata(str, Main.getPlugin());
		final AbstractEntity t = target;
		final AbstractLocation l = target.getLocation().clone();
		final int dur = this.duration;
		final boolean facing=this.f,gravity=this.g,ai=this.ai;
		target.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final boolean aai=t.isLiving()?((LivingEntity)t.getBukkitEntity()).hasAI():false;
		new BukkitRunnable() {
			long count=0;
			float yaw=l.getYaw(),pitch=l.getPitch();
			double x=l.getX(),y=l.getY(),z=l.getZ();
			@Override
			public void run() {
				if (t==null
						||t.isDead()
						||count>dur
						||!t.getBukkitEntity().hasMetadata(str)) {
					if (t!=null&&!t.isDead()) {
						t.getBukkitEntity().removeMetadata(str, Main.getPlugin());
						if (t.isLiving()) ((LivingEntity)t.getBukkitEntity()).setAI(aai);
					}
					this.cancel();
				} else {
					if (t.isLiving()&&ai&&((LivingEntity)t.getBukkitEntity()).hasAI()) {
						if (t.getBukkitEntity().isOnGround()) ((LivingEntity)t.getBukkitEntity()).setAI(false);
					}
					if (facing) {
						yaw=t.getLocation().getYaw();
						pitch=t.getLocation().getPitch();
					}
					if (gravity) y=t.getLocation().getY();
					Volatile.handler.forceSetPositionRotation(target.getBukkitEntity(),x,y,z,yaw,pitch,facing,gravity);
				}
				if(useDuration) count++;
			}
		}.runTaskTimer(Main.getPlugin(), 1L,1L);
		return true;
	}

}
