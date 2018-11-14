package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class 
UnExemptPlayerMechanic 
extends 
ExemptPlayerMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill {

	public UnExemptPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		return false;
	}
}
