package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class mmLastDamageCauseCondition extends SkillCondition implements IEntityCondition {
	private String[] attacker, cause;
	
	public mmLastDamageCauseCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
		this.attacker = mlc.getString(new String[]{"attacker","damager"},"any").toUpperCase().split(",");
		this.cause = mlc.getString(new String[]{"cause","c"},"any").toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity ae) {
		boolean match = false;
		Entity entity = ae.getBukkitEntity();
		EntityDamageEvent e = entity.getLastDamageCause();
		if (e==null) return false;
		if (e instanceof EntityDamageByEntityEvent) {
			Entity damager = CustomSkillStuff.getAttacker(((EntityDamageByEntityEvent)e).getDamager());
			if (damager!=null) {
				if (Arrays.asList(this.attacker).contains(damager.getType().toString()) || 
						Arrays.asList(this.attacker).contains("ANY")) {
					match = true;
				}
			}
		}
		if (Arrays.asList(this.cause).contains(e.getCause().name().toUpperCase()) || 
				Arrays.asList(this.cause).contains("ANY")) {
			match = true;
		} else {
			match = false;
		}
		return match;
	}

}
