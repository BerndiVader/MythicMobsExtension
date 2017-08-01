package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
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
		boolean match = false;
		Entity entity = ae.getBukkitEntity();
		EntityDamageEvent e = entity.getLastDamageCause();
		if (e == null)
			return false;
		if (e instanceof EntityDamageByEntityEvent) {
			Entity damager = CustomSkillStuff.getAttacker(((EntityDamageByEntityEvent) e).getDamager());
			if (damager != null) {
				if (Arrays.asList(this.attacker).contains(damager.getType().toString())
						|| this.attacker[0].equals("ANY")) {
					match = true;
				}
			}
		}
		match = (Arrays.asList(this.cause).contains(e.getCause().name().toUpperCase()) || this.cause[0].equals("ANY"))
				? true : false;
		return match;
	}

}
