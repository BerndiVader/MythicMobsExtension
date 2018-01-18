package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class NoDamageTicksMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	public static String str;
	int j1,j2;
	
	static {
		str="mmenodelaydmg";
	}

	public NoDamageTicksMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		j1=mlc.getInteger("damagedelay",1);
		j2=mlc.getInteger("duration",1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if (e.isLiving()&&!e.getBukkitEntity().hasMetadata(str)) {
			final LivingEntity e1=(LivingEntity)e.getBukkitEntity();
			e1.setMetadata(str,new FixedMetadataValue(Main.getPlugin(),e1.getMaximumNoDamageTicks()));
			new BukkitRunnable() {
				int j4=j2;
				@Override
				public void run() {
					if (e1.hasMetadata(str)) {
						if (j4==0) {
							e1.setMaximumNoDamageTicks(e1.getMetadata(str).get(0).asInt());
							e1.removeMetadata(str,Main.getPlugin());
							this.cancel();
						}
					}
					j1(e1);
					j4--;
				}
			}.runTaskTimer(Main.getPlugin(),1L,1L);
		}
		return false;
	}
	
	private void j1(LivingEntity e) {
		e.setNoDamageTicks(j1);
		e.setMaximumNoDamageTicks(j1);
	}

}
