package com.gmail.berndivader.mythicmobsext;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CustomDamage
extends
SkillMechanic
implements
ITargetedEntitySkill {
	boolean pk;
	boolean pi;
	boolean ia;
	boolean iabs;
	boolean ip;
	boolean p;
	boolean pcur;
	boolean debug;
	double dbd;
	DamageCause cause;
	String amount;

	public CustomDamage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		this.ASYNC_SAFE = false;
		this.pk = mlc.getBoolean(new String[] { "preventknockback", "pkb", "pk" }, false);
 		this.amount = mlc.getString(new String[] { "amount", "a" }, "1");
		if (this.amount.startsWith("-")) { this.amount = "1"; }
		this.ia = mlc.getBoolean(new String[] { "ignorearmor", "ignorearmour", "ia", "i" }, false);
		this.pi = mlc.getBoolean(new String[] { "preventimmunity", "pi" }, false);
		this.iabs = mlc.getBoolean(new String[] { "ignoreabsorbtion", "ignoreabs", "iabs" }, false);
		this.ip = mlc.getBoolean(new String[] { "ignorepower", "ip" }, false);
		this.p = mlc.getBoolean(new String[] { "percentage", "p" }, false);
		this.pcur = mlc.getBoolean(new String[] { "percentcurrent", "pcur", "pc" }, false);
		String ca = mlc.getString(new String[] { "damagecause", "cause", "dc" }, "CUSTOM").toUpperCase();
		dbd=mlc.getDouble("rdbd",-7331d);
		cause=DamageCause.CUSTOM;
		for (DamageCause dc : DamageCause.values()) {
			if (dc.toString().equals(ca)) {
				this.cause=dc;
				break;
			}
		}
		this.debug=mlc.getBoolean("debug",false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (t.isDead() || t.getHealth() <= 0.0 || data.getCaster().isUsingDamageSkill())
			return false;
		double dmg = Utils.randomRangeDouble(this.amount);
		if (this.p) {
			dmg = this.pcur ? t.getHealth() * dmg : t.getMaxHealth() * dmg;
		}
		if (!this.ip)
			dmg = dmg * data.getPower();
		if (this.dbd>-7331d) {
			int dd=(int)Math.sqrt(Utils.distance3D(data.getCaster().getEntity().getBukkitEntity().getLocation().toVector(),t.getBukkitEntity().getLocation().toVector()));
			dmg-=(dmg*(dd*dbd));
		}
		Utils.doDamage(data.getCaster(), t, dmg, this.ia, this.pk, this.pi, this.iabs, this.debug, this.cause);
		return true;
	}
}
