package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name="lastdamagecause,lastdamagecause_ext",author="BerndiVader")
public class LastDamageCauseCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {
	private String[] attackers,causes;
	RangedDouble amount;

	public LastDamageCauseCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.attackers=mlc.getString(new String[] { "attacker", "damager" }, "any").toUpperCase().split(",");
		this.causes=mlc.getString(new String[] { "cause", "c" }, "any").toUpperCase().split(",");
		this.amount=new RangedDouble(mlc.getString("amount",">0"));
	}
	
	@Override
	public boolean check(AbstractEntity ae) {
		boolean match=false;
		String damager=null,cause=null;
		double amount=-1337;
		Entity entity=ae.getBukkitEntity();
		if (entity.hasMetadata(Utils.meta_LASTDAMAGER)) damager=entity.getMetadata(Utils.meta_LASTDAMAGER).get(0).asString().toUpperCase();
		if (entity.hasMetadata(Utils.meta_LASTDAMAGECAUSE)) cause=entity.getMetadata(Utils.meta_LASTDAMAGECAUSE).get(0).asString().toUpperCase();
		if (entity.hasMetadata(Utils.meta_LASTDAMAGEAMOUNT)) amount=entity.getMetadata(Utils.meta_LASTDAMAGEAMOUNT).get(0).asDouble();
		if (!attackers[0].equals("ANY")) {
			if(damager!=null) {
				for(String s1:attackers) {
					if(s1.equals(damager)) {
						match=true;
						break;
					}
				}
			}
		} else {
			match=true;
		}
		if (match&&(cause!=null&&!causes[0].equals("ANY"))) {
			match=false;
			for(String s1:causes) {
				if (s1.equals(cause)) {
					match=true;
					break;
				}
			}
		}
		boolean debug=match&&(amount==-1337||this.amount.equals(amount));
		if(dba)Main.logger.info("outcome:"+debug+":damager:"+damager+":cause:"+cause);
		return debug;
	}
}
