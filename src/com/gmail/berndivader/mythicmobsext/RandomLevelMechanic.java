package com.gmail.berndivader.mythicmobsext;

import java.util.concurrent.ThreadLocalRandom;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetLevelMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	private MobManager mobmanager;
	private String a;

	public SetLevelMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.mobmanager=Main.getPlugin().getMobManager();
		this.a=mlc.getString(new String[] { "amount", "a" }, 1).toUpperCase();
		if (!a.contains("TO")) {
			Integer lvl=a;
			min=null;
		} else {
			String[]a1=a.split("TO");
			min=Integer.parseInteger(a1[0]);
			max=Integer.parseInteger(a1[1]);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!min=null) {
			Integer lvl=ThreadLocalRandom.current().nextInt(min,max+1);
		}
		if (target instanceof ActiveMob) {
			ActiveMob am=mobmanager.getMythicMobInstance(target);
			am.setLevel(lvl);
			return true;
		}
		return false;
	}
}
