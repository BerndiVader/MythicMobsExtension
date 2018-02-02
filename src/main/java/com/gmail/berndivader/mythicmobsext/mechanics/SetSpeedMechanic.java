package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetSpeedMechanic 
extends 
SkillMechanic
implements
ITargetedEntitySkill,
INoTargetSkill {
	private String s1;

	public SetSpeedMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		s1=mlc.getString(new String[] { "amount", "a" }, "0.2D").toLowerCase();
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return d(data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return d(target);
	}
	
	private Boolean d(AbstractEntity target) {
		if (target.isLiving()) {
			((LivingEntity)target.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Utils.randomRangeDouble(s1));
			return true;
		}
		return false;
	}
}
