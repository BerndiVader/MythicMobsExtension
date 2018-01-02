package com.gmail.berndivader.mythicmobsext;

import java.util.concurrent.ThreadLocalRandom;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RandomLevelMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	private MobManager mobmanager;
	private int min;
	private int max;

	public RandomLevelMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.mobmanager=Main.getPlugin().getMobManager();
		this.min=mlc.getInteger("min",1);
		this.max=mlc.getInteger("max",3);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		int lvl=ThreadLocalRandom.current().nextInt(min,max+1);
		if (target instanceof ActiveMob) {
			ActiveMob am=mobmanager.getMythicMobInstance(target);
			am.setLevel(lvl);
			return true;
		}
		return false;
	}
}
