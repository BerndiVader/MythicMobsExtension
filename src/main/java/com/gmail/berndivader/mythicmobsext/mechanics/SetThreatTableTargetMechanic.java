package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

@ExternalAnnotation(name="setthreattarget",author="BerndiVader")
public class SetThreatTableTargetMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	private double amount;

	public SetThreatTableTargetMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=true;
		this.amount=mlc.getDouble(new String[] { "amount", "a" },65536);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (data.getCaster() instanceof ActiveMob) {
			ActiveMob am = (ActiveMob) data.getCaster();
			if (am.getThreatTable().size()>0) {
				am.getThreatTable().clearTarget();
				am.getThreatTable().getAllThreatTargets().clear();
			}
			if (target!=null) {
				am.getThreatTable().threatGain(target,this.amount);
				am.getThreatTable().targetHighestThreat();
				new TriggeredSkill(SkillTrigger.ENTERCOMBAT,am,target);
			}
			return true;
		} else {
			return false;
		}
	}

}
