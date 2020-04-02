package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "clearthreattarget,dropcombat", author = "BerndiVader")
public class ClearThreatTableMechanic extends SkillMechanic implements INoTargetSkill {

	public ClearThreatTableMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster() instanceof ActiveMob) {
			ActiveMob am = (ActiveMob) data.getCaster();
			if (am.hasThreatTable()) {
				am.getThreatTable().getAllThreatTargets().clear();
				am.getThreatTable().dropCombat();
				return true;
			}
		}
		return false;
	}

}
