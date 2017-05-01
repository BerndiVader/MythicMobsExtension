package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmNormalPlayer extends SkillMechanic implements ITargetedEntitySkill {

	public mmNormalPlayer(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!MythicMobs.inst().getMobManager().isActiveMob(target)) return false;
		ActiveMob am = MythicMobs.inst().getMobManager().getMythicMobInstance(target);
		ActivePlayerStuff.makeNormalPlayer(am);
		return true;
	}
	
}
