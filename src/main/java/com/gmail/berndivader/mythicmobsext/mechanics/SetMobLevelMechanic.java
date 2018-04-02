package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="setrandomlevel,setmoblevel",author="BerndiVader")
public class SetMobLevelMechanic
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	private String a;
	int min,max;

	public SetMobLevelMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.a=mlc.getString(new String[] { "amount", "a" },"1").toLowerCase();
		r(mlc.getInteger("min",-1),mlc.getInteger("max",-1));
	}

	private void r(int i1, int i2) {
		if (min>-1&&max>-1&&max>=min) a=Integer.toString(min)+"to"+Integer.toString(max);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (Utils.mobmanager.isActiveMob(target)) {
			ActiveMob am=Utils.mobmanager.getMythicMobInstance(target);
			am.setLevel(Utils.randomRangeInt(Utils.parseMobVariables(a,data,data.getCaster().getEntity(),target,null)));
			return true;
		}
		return false;
	}
}
