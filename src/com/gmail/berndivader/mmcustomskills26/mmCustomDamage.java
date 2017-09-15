package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.MathParser;

public class mmCustomDamage extends SkillMechanic implements ITargetedEntitySkill {

	private boolean pk, pi, ia, iabs, ip, p, pcur, debug;
	private DamageCause cause=DamageCause.CUSTOM;
	private Double amount;

	public mmCustomDamage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		this.ASYNC_SAFE = false;
		this.pk = mlc.getBoolean(new String[] { "preventknockback", "pkb", "pk" }, false);
		this.amount = mlc.getDouble(new String[] { "amount", "a" }, "1");
		if (this.amount.startsWith("-"))
			this.amount = "1";
		this.ia = mlc.getBoolean(new String[] { "ignorearmor", "ia", "i" }, false);
		this.pi = mlc.getBoolean(new String[] { "preventimmunity", "pi" }, false);
		this.iabs = mlc.getBoolean(new String[] { "ignoreabs", "iabs" }, false);
		this.ip = mlc.getBoolean(new String[] { "ignorepower", "ip" }, false);
		this.p = mlc.getBoolean(new String[] { "percentage", "p" }, false);
		this.pcur = mlc.getBoolean(new String[] { "pcur", "pc" }, false);
		String ca = mlc.getString(new String[] { "damagecause", "cause" }, "CUSTOM").toUpperCase();
		for (DamageCause dc : DamageCause.values()) {
			if (dc.toString().equals(ca)) {
				this.cause=dc;
				break;
			}
		}
		this.debug = mlc.getBoolean("debug", false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (t.isDead() || t.getHealth() <= 0.0 || data.getCaster().isUsingDamageSkill())
			return false;
		double dmg = MathParser.evalRange(this.amount);
		if (this.p) {
			dmg = this.pcur ? t.getHealth() * dmg : t.getMaxHealth() * dmg;
		}
		if (!this.ip)
			dmg = dmg * data.getPower();
		CustomSkillStuff.doDamage(data.getCaster(), t, dmg, this.ia, this.pk, this.pi, this.iabs, this.debug, this.cause);
		return true;
	}
}
