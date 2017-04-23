package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCustomDamage extends SkillMechanic implements ITargetedEntitySkill {

	private boolean pk, pi, ia, iabs, ip;
	private double amount;
	
	public mmCustomDamage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		this.ASYNC_SAFE=false;
		this.pk = mlc.getBoolean(new String[]{"preventknockback", "pkb", "pk"}, false);
        this.amount = mlc.getDouble(new String[]{"amount", "a"}, 1.0);
        this.ia = mlc.getBoolean(new String[]{"ignorearmor", "ia", "i"}, false);
        this.pi = mlc.getBoolean(new String[]{"preventimmunity", "pi"}, false);
        this.iabs = mlc.getBoolean(new String[]{"ignoreabs", "iabs"}, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        if (target.isDead() || target.getHealth() <= 0.0 || data.getCaster().isUsingDamageSkill()) return false;
        double dmg = this.ip?this.amount * (double)data.getPower():this.amount;
        CustomStuff.doDamage(data.getCaster(), target, dmg, this.ia, this.pk, this.pi, this.iabs);
        return true;
	}
}
