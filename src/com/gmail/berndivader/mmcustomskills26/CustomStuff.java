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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

public class CustomStuff implements Listener {
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onActivePlayerDeath(PlayerDeathEvent e) {
		if (!e.getEntity().hasMetadata("MythicPlayer") && !MythicMobs.inst().getAPIHelper().isMythicMob(e.getEntity())) return;
		ActiveMob am = MythicMobs.inst().getAPIHelper().getMythicMobInstance(e.getEntity());
		am.signalMob(am.getEntity(), "DEATH");
		Location l = e.getEntity().getLocation();
		l.setY(0);
		AbstractEntity d = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
		MythicMobs.inst().getMobManager().unregisterActiveMob(am.getEntity().getUniqueId());
		am.setEntity(d);
		d.remove();
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
	public void onMythicPlayerDamage(EntityDamageEvent e) {
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
	
    public static boolean createActivePlayer(LivingEntity l, MythicMob mm) {
        ActiveMob am = new ActiveMob(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, 1);
        if (mm.hasFaction()) {
            am.setFaction(mm.getFaction());
            l.setMetadata("Faction", new FixedMetadataValue(MythicMobs.inst(),mm.getFaction()));
        }
        MythicMobs.inst().getMobManager().registerActiveMob(am);
        new TriggeredSkill(SkillTrigger.SPAWN, am, null);
        l.setMetadata("MythicPlayer", new FixedMetadataValue(Main.getPlugin(),mm.getInternalName()));
        return true;
    }
}
