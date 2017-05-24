package com.gmail.berndivader.mmcustomskills26;


import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCustomDamage extends SkillMechanic implements ITargetedEntitySkill {

	private boolean pk, pi, ia, iabs, ip, p, pcur;
	private double amount;
	
	public mmCustomDamage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		this.ASYNC_SAFE=false;
		this.pk = mlc.getBoolean(new String[]{"preventknockback", "pkb", "pk"}, false);
        this.amount = mlc.getDouble(new String[]{"amount", "a"}, 1.0);
        this.ia = mlc.getBoolean(new String[]{"ignorearmor", "ia", "i"}, false);
        this.pi = mlc.getBoolean(new String[]{"preventimmunity", "pi"}, false);
        this.iabs = mlc.getBoolean(new String[]{"ignoreabs", "iabs"}, false);
        this.ip = mlc.getBoolean(new String[]{"ignorepower", "ip"}, false);
        this.p = mlc.getBoolean(new String[]{"percentage", "p"}, false);
        this.pcur = mlc.getBoolean(new String[]{"pcur", "pc"}, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
        if (t.isDead() || t.getHealth() <= 0.0 || data.getCaster().isUsingDamageSkill()) return false;
        double dmg = this.ip?this.amount:this.amount * (double)data.getPower();
        if (this.p) {
            dmg = this.pcur?t.getHealth()*this.amount:t.getMaxHealth()*this.amount;
        }
        CustomSkillStuff.doDamage(data.getCaster(), t, dmg, this.ia, this.pk, this.pi, this.iabs);
        return true;
	}
}
