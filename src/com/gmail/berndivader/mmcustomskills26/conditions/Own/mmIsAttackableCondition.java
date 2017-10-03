package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@SuppressWarnings("deprecation")
public class mmIsAttackableCondition extends mmCustomCondition
implements
IEntityComparisonCondition {
	protected DamageCause cause;
	
	public mmIsAttackableCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		String ca = mlc.getString(new String[] { "damagecause", "cause" }, "CUSTOM").toUpperCase();
		for (DamageCause dc : DamageCause.values()) {
			if (dc.toString().equals(ca)) {
				this.cause=dc;
				break;
			}
		}
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		if (source.getUniqueId().equals(target.getUniqueId())) return true;
		EntityDamageByEntityEvent event = 
				new EntityDamageByEntityEvent(source.getBukkitEntity(),target.getBukkitEntity(), this.cause, 1);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
	}

}
