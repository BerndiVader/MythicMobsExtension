package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="customdamage",author="BerndiVader")
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
	boolean pcur,ploss;
	boolean debug;
	boolean rdbd;
	boolean ncp;
	boolean strict;
	double dbd;
	DamageCause cause;
	String amount;
	String ca;
	List<EntityType>pi_ignores;

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
		this.strict=mlc.getBoolean("strict",false);
		this.pcur = mlc.getBoolean(new String[] { "percentcurrent", "pcur", "pc" },false);
		if((ploss=mlc.getBoolean(new String[] {"percentloss","ploss","pl"},false))) pcur=false;
		String[]ignores=mlc.getString("ignores","").toUpperCase().split(",");
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
		pi_ignores=new ArrayList<>();
		for(int i1=0;i1<ignores.length;i1++) {
			for (EntityType type : EntityType.values()) {
				String parse=ignores[i1];
				if (type.toString().equals(parse)) {
					pi_ignores.add(EntityType.valueOf(parse));
				}
			}
		}
		ncp=mlc.getBoolean("ncp",false);
		this.debug=mlc.getBoolean("debug",false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (!t.isValid()||t.isDead()||t.getHealth()<=0.0||data.getCaster().isUsingDamageSkill()) return false;
		AbstractEntity c=data.getCaster().getEntity();
		double dmg=MathUtils.randomRangeDouble(Utils.parseMobVariables(this.amount,data,data.getCaster().getEntity(),t,null));
		if (this.p) dmg=this.pcur?uc?c.getHealth()*dmg:t.getHealth()*dmg:ploss?uc?(c.getMaxHealth()-c.getHealth())*dmg:(t.getMaxHealth()-t.getHealth())*dmg:uc?c.getMaxHealth()*dmg:t.getMaxHealth()*dmg;
		if (!this.ip) dmg=dmg*data.getPower();
		if (this.dbd>0) {
			int dd=(int)Math.sqrt(MathUtils.distance3D(data.getCaster().getEntity().getBukkitEntity().getLocation().toVector(), t.getBukkitEntity().getLocation().toVector()));
			dmg=rdbd?dmg-(dmg*(dd*dbd)):dmg+(dmg*(dd*dbd));
		}
		Utils.doDamage(data.getCaster(),t,dmg,this.ia,this.pk,this.pi,pi_ignores,this.iabs,this.debug,this.cause,this.ncp,this.strict);
		return true;
	}
}