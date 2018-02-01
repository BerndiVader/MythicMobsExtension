package main.java.com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CustomDamageMechanic
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
	boolean uc;
	boolean pcur;
	boolean debug;
	boolean rdbd;
	double dbd;
	DamageCause cause;
	String amount;
	String ca;

	public CustomDamageMechanic(String skill, MythicLineConfig mlc) {
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
		this.uc = mlc.getBoolean(new String[] { "usecaster", "uc" }, false);
		this.pcur = mlc.getBoolean(new String[] { "percentcurrent", "pcur", "pc" }, false);
		ca = mlc.getString(new String[] { "damagecause", "cause", "dc" }, "CUSTOM").toUpperCase();
		dbd=-mlc.getDouble(new String[] { "reducedamagebydistance", "rdbd","damagebydistance","dbd" },0);
		rdbd=(dbd=mlc.getDouble(new String[] { "increasedamagebydistance","idbd" },dbd))<0;
		dbd=Math.abs(dbd);
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
		AbstractEntity c = data.getCaster().getEntity();
		double dmg = Utils.randomRangeDouble(this.amount);
		if (this.p) {
			dmg=this.pcur?uc?c.getHealth()*dmg:t.getHealth()*dmg:c.getMaxHealth()*dmg;
		}
		if (!this.ip)
			dmg=dmg*data.getPower();
		if (this.dbd>0) {
			int dd=(int)Math.sqrt(Utils.distance3D(data.getCaster().getEntity().getBukkitEntity().getLocation().toVector(), t.getBukkitEntity().getLocation().toVector()));
			dmg=rdbd?dmg-(dmg*(dd*dbd)):dmg+(dmg*(dd*dbd));
		}
		Utils.doDamage(data.getCaster(), t, dmg, this.ia, this.pk, this.pi, this.iabs, this.debug, this.cause);
		return true;
	}
}