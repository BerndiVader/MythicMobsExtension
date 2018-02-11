package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.SkillAnnotation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@SkillAnnotation(name="oxygen",author="BerndiVader")
public class OxygenMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private int amount;

	public OxygenMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.amount=mlc.getInteger(new String[] { "amount", "a" },1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isLiving()) return false;
		LivingEntity le=(LivingEntity)target.getBukkitEntity();
		le.setRemainingAir(Integer.min(le.getRemainingAir()+amount,le.getMaximumAir()));
		return true;
	}

}
