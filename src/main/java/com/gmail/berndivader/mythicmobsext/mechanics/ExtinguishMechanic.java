package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="extinguish",author="BerndiVader")
public class ExtinguishMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {

	public ExtinguishMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity e=target.getBukkitEntity();
		e.setFireTicks(-1);
		return true;
	}
}
