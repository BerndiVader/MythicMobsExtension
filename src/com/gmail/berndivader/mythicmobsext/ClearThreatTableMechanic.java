package com.gmail.berndivader.mythicmobsext;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ClearThreatTableMechanic
extends 
SkillMechanic
implements
INoTargetSkill {

	public ClearThreatTableMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster()instanceof ActiveMob) {
			ActiveMob am=(ActiveMob)data.getCaster();
			if (am.getThreatTable().size()>0) {
				am.getThreatTable().clearTarget();
				am.getThreatTable().getAllThreatTargets().clear();
			}
			return true;
		} 
		return false;
	}

}
