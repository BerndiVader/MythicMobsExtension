package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.NMS.NMSUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmStunSkill extends SkillMechanic implements ITargetedEntitySkill {

	private Integer duration;
	private Boolean f;

	public mmStunSkill(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.duration = mlc.getInteger(new String[] { "duration", "d" }, 120);
		this.f = mlc.getBoolean(new String[] { "facing", "face", "f" }, false);
		this.ASYNC_SAFE = false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		final AbstractEntity t = target;
		final AbstractLocation l = target.getLocation().clone();
		final int dur = this.duration;
		final boolean facing=this.f;
		target.getBukkitEntity().setMetadata("mmStunned", new FixedMetadataValue(Main.getPlugin(), true));
		new BukkitRunnable() {
			int count = 0;
			float yaw=l.getYaw(),pitch=l.getPitch();
			double x=l.getX(),y=l.getY(),z=l.getZ();
			@Override
			public void run() {
				if (t == null || t.isDead() || count >= dur) {
					target.getBukkitEntity().removeMetadata("mmStunned", Main.getPlugin());
					this.cancel();
				} else {
					if (facing) {
						yaw=t.getLocation().getYaw();
						pitch=t.getLocation().getPitch();
					}
					NMSUtils.setLocation(t.getBukkitEntity(),x,y,z,yaw,pitch);
				}
				count++;
			}
		}.runTaskTimer(Main.getPlugin(), 1L, 1L);
		return true;
	}

}
