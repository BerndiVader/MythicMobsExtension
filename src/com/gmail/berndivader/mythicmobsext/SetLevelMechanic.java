package com.gmail.berndivader.mythicmobsext;

import com.gmail.berndivader.utils.Utils;

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
	int min,max;

	public SetLevelMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.mobmanager=Main.getPlugin().getMobManager();
		r(mlc.getInteger("min",-1),mlc.getInteger("max",-1));
		this.a=mlc.getString(new String[] { "amount", "a" },"1").toLowerCase();
	}

	private void r(int i1, int i2) {
		if (min>-1&&max>-1&&max>=min) a=Integer.toString(min)+"to"+Integer.toString(max);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (mobmanager.isActiveMob(target)) {
			ActiveMob am=mobmanager.getMythicMobInstance(target);
			am.setLevel(Utils.randomRangeInt(a));
			return true;
		}
		return false;
	}
}
