package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class mmLastDamageCauseCondition extends mmCustomCondition implements IEntityCondition {
	private String[] attacker, cause;

	public mmLastDamageCauseCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.attacker = mlc.getString(new String[] { "attacker", "damager" }, "any").toUpperCase().split(",");
		this.cause = mlc.getString(new String[] { "cause", "c" }, "any").toUpperCase().split(",");
	}
	
	@Override
	public boolean check(AbstractEntity ae) {
		boolean match=false;
		String damager = null, cause = null;
		Entity entity = ae.getBukkitEntity();
		if (entity.hasMetadata("LastDamager")) {
			damager = entity.getMetadata("LastDamager").get(0).asString();
		}
		if (entity.hasMetadata("LastDamageCause")) {
			cause = entity.getMetadata("LastDamageCause").get(0).asString();
		}
		if (damager!=null) {
			if (Arrays.asList(this.attacker).contains(damager)
					|| this.attacker[0].equals("ANY")) {
				match = true;
			}
		} else {
			match = true;
		}
		if (match && cause!=null) {
			match = (Arrays.asList(this.cause).contains(cause.toUpperCase()) || this.cause[0].equals("ANY"))
					? true : false;
		}
		return match;
	}
	
}
