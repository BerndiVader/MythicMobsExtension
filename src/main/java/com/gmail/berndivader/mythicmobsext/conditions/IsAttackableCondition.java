package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name = "damageable,attackable", author = "BerndiVader")
public class IsAttackableCondition extends AbstractCustomCondition implements IEntityComparisonCondition {

	private DamageCause cause;
	boolean swap;
	RangedDouble damageRange;

	public IsAttackableCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		cause = DamageCause.CUSTOM;
		String ca = mlc.getString(new String[] { "damagecause", "cause" }, "CUSTOM").toUpperCase();
		damageRange = new RangedDouble(mlc.getString("range", "-1"));
		swap = mlc.getBoolean("swap", false);
		for (DamageCause dc : DamageCause.values()) {
			if (dc.toString().equals(ca)) {
				this.cause = dc;
				break;
			}
		}
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		if (swap) {
			AbstractEntity temp = source;
			source = target;
			target = temp;
		}
		if (source.getUniqueId().equals(target.getUniqueId()))
			return true;
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(source.getBukkitEntity(),
				target.getBukkitEntity(), this.cause, 1);
		Bukkit.getPluginManager().callEvent(event);
		boolean bl1 = !event.isCancelled();
		event.setCancelled(true);

		if (damageRange.equals(-1)) {
			return bl1;
		} else {
			LivingEntity livingTarget = (LivingEntity) target.getBukkitEntity();
			livingTarget.setMetadata("mme_dmg_chk", new FixedMetadataValue(Main.getPlugin(), true));
			livingTarget.damage(10, source.getBukkitEntity());
			double delta = livingTarget.getMetadata("mme_dmg_amount").get(0).asDouble() / 10;
			livingTarget.removeMetadata("mme_dmg_amount", Main.getPlugin());
			return bl1 && damageRange.equals(delta);
		}

	}

}