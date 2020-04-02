package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name = "damageable,attackable", author = "BerndiVader")
@SuppressWarnings("deprecation")
public class IsAttackableCondition extends AbstractCustomCondition implements IEntityComparisonCondition {
	private DamageCause cause;

	public IsAttackableCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		cause = DamageCause.CUSTOM;
		String ca = mlc.getString(new String[] { "damagecause", "cause" }, "CUSTOM").toUpperCase();
		for (DamageCause dc : DamageCause.values()) {
			if (dc.toString().equals(ca)) {
				this.cause = dc;
				break;
			}
		}
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		if (source.getUniqueId().equals(target.getUniqueId()))
			return true;
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(source.getBukkitEntity(),
				target.getBukkitEntity(), this.cause, 1);
		Bukkit.getPluginManager().callEvent(event);
		boolean bl1 = !event.isCancelled();
		event.setCancelled(true);
		return bl1;
	}

}
