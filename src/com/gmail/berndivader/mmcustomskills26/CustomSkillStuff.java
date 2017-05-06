package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.metadata.FixedMetadataValue;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;

public class CustomSkillStuff implements Listener {
	
	@EventHandler
	public void onMythicCustomDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof LivingEntity) || e.isCancelled()) return;
		LivingEntity victim = (LivingEntity) e.getEntity();
		if (!victim.hasMetadata("MythicDamage")) return;
		victim.removeMetadata("MythicDamage", Main.getPlugin());
		double damage=0D;
		double md = victim.getMetadata("DamageAmount").get(0).asDouble();
		double df = md / e.getDamage(DamageModifier.BASE);
		e.setDamage(DamageModifier.BASE, md);
		if (victim.getMetadata("IgnoreArmor").get(0).asBoolean()) {
			e.setDamage(DamageModifier.ARMOR, 0D);
		} else {
			e.setDamage(DamageModifier.ARMOR, df * e.getDamage(DamageModifier.ARMOR));
		}
		if (victim.getMetadata("IgnoreAbs").get(0).asBoolean()) {
			e.setDamage(DamageModifier.ABSORPTION, 0D);
		} else {
			e.setDamage(DamageModifier.ABSORPTION, df * e.getDamage(DamageModifier.ABSORPTION));
		}
		if (victim.getMetadata("PreventKnockback").get(0).asBoolean()) {
			e.setCancelled(true);
			for (DamageModifier modifier : DamageModifier.values()) {
				if (!e.isApplicable(modifier)) continue;
				damage+=e.getDamage(modifier);
			}
			victim.damage(damage);
		}
	}

	public static void doDamage(SkillCaster am, AbstractEntity t, double damage, 
			boolean ignorearmor, 
			boolean preventKnockback, 
			boolean preventImmunity, 
			boolean ignoreabs) {
        LivingEntity target;
        am.setUsingDamageSkill(true);
        if (am instanceof ActiveMob) ((ActiveMob)am).setLastDamageSkillAmount(damage);
        LivingEntity source = (LivingEntity)BukkitAdapter.adapt(am.getEntity());
        target = (LivingEntity)BukkitAdapter.adapt(t);
        target.setMetadata("IgnoreArmor", new FixedMetadataValue(Main.getPlugin(),ignorearmor));
        target.setMetadata("PreventKnockback", new FixedMetadataValue(Main.getPlugin(),preventKnockback));
        target.setMetadata("IgnoreAbs", new FixedMetadataValue(Main.getPlugin(),ignoreabs));
        target.setMetadata("MythicDamage", new FixedMetadataValue(Main.getPlugin(),true));
        target.setMetadata("DamageAmount", new FixedMetadataValue(Main.getPlugin(),damage));
		target.damage(damage, source);
	    if (preventImmunity) target.setNoDamageTicks(0);
	    am.setUsingDamageSkill(false);
	}
	
}
