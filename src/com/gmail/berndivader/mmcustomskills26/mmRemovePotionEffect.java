package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmRemovePotionEffect extends SkillMechanic implements ITargetedEntitySkill {
	private String type;

	public mmRemovePotionEffect(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.type = mlc.getString(new String[]{"potion","p","type","t"},"INVISIBILITY").toUpperCase();
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.hasPotionEffect(this.type)) {
			LivingEntity le = (LivingEntity) target.getBukkitEntity();
			le.removePotionEffect(PotionEffectType.getByName(this.type));
			return true;
		}
		return false;
	}

}
