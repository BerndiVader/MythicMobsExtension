package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class StoreCooldownMechanic
extends
SkillMechanic
implements
INoTargetSkill {

	public StoreCooldownMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return false;
	}

}
