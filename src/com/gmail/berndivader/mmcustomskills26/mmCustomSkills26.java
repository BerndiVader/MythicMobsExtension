package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class mmCustomSkills26 implements Listener {
	
	private String MechName;
	private SkillMechanic skill;
	
	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		MechName = e.getMechanicName().toLowerCase();
		if (MechName.equals("damagearmor")) {
			skill = new mmDamageArmorSkill(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("grenade")) {
			skill = new mmGrenadeSkill(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("setrandomlevel")) {
			skill = new mmRandomLevelSkill(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("steal")) {
			skill = new mmStealSkill(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("dropstolenitems")) {
			skill = new mmDropStolenItems(e.getContainer(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("equipskull")) {
			skill = new mmEquipFix(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
		} else if (MechName.equals("endereffect")) {
			skill = new mmEnderEffect(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
		} else if (MechName.equals("customdamage")) {
			skill = new mmCustomDamage(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
		}
	}
	
	@EventHandler
	public void onPKdamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof LivingEntity) || e.isCancelled()) return;
		LivingEntity victim = (LivingEntity) e.getEntity();
		if (!victim.hasMetadata("MythicDamage")) return;
		victim.removeMetadata("MythicDamage", Main.getPlugin());
		double damage = e.getDamage(DamageModifier.BASE);
		if (!victim.getMetadata("IgnoreAbs").get(0).asBoolean()) damage+=e.getDamage(DamageModifier.ABSORPTION);
		if (!victim.getMetadata("IgnoreArmor").get(0).asBoolean()) damage+=e.getDamage(DamageModifier.ARMOR);
		if (victim.getMetadata("PreventKnockback").get(0).asBoolean()) {
			e.setCancelled(true);
			victim.damage(damage);
		} else {
			if (victim.getHealth() - damage < 1.0) {
				victim.setHealth(0.1);
            	victim.damage(10.0, e.getDamager());
			} else {
				victim.setHealth(victim.getHealth() - damage);
			}
		}
	}
}
