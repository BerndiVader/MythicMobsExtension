package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmWorldEditPyramid extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

	public mmWorldEditPyramid(CustomMechanic skill, MythicLineConfig mlc) {
		super(skill.getConfigLine(), mlc);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return true;
	}
}
