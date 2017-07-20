package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

public class mmSetThreatTableTarget extends SkillMechanic implements ITargetedEntitySkill {
	protected double amount;
	
	public mmSetThreatTableTarget(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=true;
		this.amount=mlc.getDouble(new String[]{"amount","a"},65536);
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
				am.getThreatTable().threatGain(target, (double)this.amount);
				am.getThreatTable().targetHighestThreat();
				new TriggeredSkill(SkillTrigger.ENTERCOMBAT, am, target);
			}
			return true;
		} else {
			return false;
		}
	}

}
