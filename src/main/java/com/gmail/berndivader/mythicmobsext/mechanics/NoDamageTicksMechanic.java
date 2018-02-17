package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="nodamageticks",author="BerndiVader")
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
		if (e.isLiving()) {
			final LivingEntity e1=(LivingEntity)e.getBukkitEntity();
			e1.removeMetadata(str,Main.getPlugin());
			new BukkitRunnable() {
				@Override
				public void run() {
					e1.setMetadata(str,new FixedMetadataValue(Main.getPlugin(),e1.getMaximumNoDamageTicks()));
					j1(e1);
					new BukkitRunnable() {
						int j4=j2;
						@Override
						public void run() {
							if (e1==null||e1.isDead()||!e1.hasMetadata(str)) {
								this.cancel();
							} else if (j4==0) {
								e1.setMaximumNoDamageTicks(20);
								e1.setNoDamageTicks(20);
								e1.removeMetadata(str,Main.getPlugin());
							}
							j4--;
						}
					}.runTaskTimer(Main.getPlugin(),1L,1L);
				}
			}.runTaskLater(Main.getPlugin(),1L);
		}
		return false;
	}
	
	private void j1(LivingEntity e) {
		e.setNoDamageTicks(this.j1);
		e.setMaximumNoDamageTicks(this.j1);
	}

}
