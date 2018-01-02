package com.gmail.berndivader.mythicmobsext;

import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RemovePotionEffectMechanic 
extends 
SkillMechanic
implements 
ITargetedEntitySkill {
	private String[]type;

	public RemovePotionEffectMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.type=mlc.getString(new String[] { "potion", "p", "type", "t" },"ALL").toUpperCase().split(",");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		LivingEntity le=(LivingEntity) target.getBukkitEntity();
		if (this.type[0].equals("ALL")) {
			if (target.hasPotionEffect()) {
				for (Iterator<PotionEffect>i=le.getActivePotionEffects().iterator();i.hasNext();) {
					le.removePotionEffect(i.next().getType());
				}
			}
		} else {
			try {
				for (String s1:this.type) {
					if (target.hasPotionEffect(s1)) {
						le.removePotionEffect(PotionEffectType.getByName(s1));
					}
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return true;
	}

}
