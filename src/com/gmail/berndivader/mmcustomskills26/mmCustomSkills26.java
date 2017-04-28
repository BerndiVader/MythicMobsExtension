package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

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
		} else if (MechName.equals("activeplayer")) {
			skill = new mmCreateActivePlayer(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
		} else if (MechName.equals("settarget")) {
			skill = new mmSetTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onActivePlayerDeath(PlayerDeathEvent e) {
		if (!e.getEntity().hasMetadata("MythicPlayer") && !MythicMobs.inst().getAPIHelper().isMythicMob(e.getEntity())) return;
		ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(e.getEntity());
		Location l = e.getEntity().getLocation();
		l.setY(240);
		Entity d = l.getWorld().spawnEntity(l, EntityType.BAT);
		am.setEntity(BukkitAdapter.adapt(d));
		am.setDead();
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (e.getPlayer().hasMetadata("MythicPlayer")) {
			MythicMobs.inst().getMobManager().unregisterActiveMob(e.getPlayer().getUniqueId());
	    	new BukkitRunnable() {
	            public void run() {
	    			MythicMob mm = MythicMobs.inst().getAPIHelper().getMythicMob(e.getPlayer().getMetadata("MythicPlayer").get(0).asString());
	    	        CustomStuff.createActivePlayer((LivingEntity) e.getPlayer(), mm);
	            }
	        }.runTaskLater(Main.getPlugin(), 1);
		}
	}
	
	@EventHandler
	public void onActiveMobDamage(EntityDamageEvent e) {
		if (!MythicMobs.inst().getAPIHelper().isMythicMob(e.getEntity()) || e.isCancelled()) return;
		DamageCause cause = e.getCause();
        e.getEntity().setMetadata("LDC", new FixedMetadataValue(Main.getPlugin(),cause.toString()));
        if (!e.getEntity().hasMetadata("MythicPlayer")) return;
		if (!cause.equals(DamageCause.ENTITY_ATTACK) &&
			!cause.equals(DamageCause.ENTITY_EXPLOSION) &&
			!cause.equals(DamageCause.CUSTOM) &&
			!cause.equals(DamageCause.PROJECTILE)) {
			ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(e.getEntity());
            am.getEntity().getBukkitEntity().setMetadata("LDC", new FixedMetadataValue(Main.getPlugin(),cause.toString()));
			new TriggeredSkill(SkillTrigger.DAMAGED, am, null);
		}
	}
	
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
}
