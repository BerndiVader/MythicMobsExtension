package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
RemoveBackBag 
extends
SkillMechanic
implements
INoTargetSkill
{
	
	public RemoveBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		BackBagHelper.removeBackBag(data.getCaster().getEntity().getUniqueId());
		return true;
	}
}
