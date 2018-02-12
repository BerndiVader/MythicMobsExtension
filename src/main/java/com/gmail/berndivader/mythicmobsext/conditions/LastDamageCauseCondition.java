package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.ConditionAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ConditionAnnotation(name="lastdamagecause",author="BerndiVader")
public class LastDamageCauseCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {
	private String[] attackers,causes;

	public LastDamageCauseCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.attackers=mlc.getString(new String[] { "attacker", "damager" }, "any").toUpperCase().split(",");
		this.causes=mlc.getString(new String[] { "cause", "c" }, "any").toUpperCase().split(",");
	}
	
	@Override
	public boolean check(AbstractEntity ae) {
		boolean match=false;
		String damager=null,cause=null;
		Entity entity=ae.getBukkitEntity();
		if (entity.hasMetadata(Utils.meta_LASTDAMAGER)) damager=entity.getMetadata(Utils.meta_LASTDAMAGER).get(0).asString().toUpperCase();
		if (entity.hasMetadata(Utils.meta_LASTDAMAGECAUSE)) cause=entity.getMetadata(Utils.meta_LASTDAMAGECAUSE).get(0).asString().toUpperCase();
		if (damager!=null&&!attackers[0].equals("ANY")) {
			for(String s1:attackers) {
				if(s1.equals(damager)) {
					match=true;
					break;
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
		return match;
	}
}
