package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class ThiefDamageEvent implements Listener {
	
	private MobManager mm = new MobManager(MythicMobs.inst());
	
	@EventHandler
	public void onThiefDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof ArmorStand) {
			return;
		}
        if (mm.isActiveMob(e.getDamager().getUniqueId())) {
        	if (e.getEntityType() == EntityType.PLAYER) {
	        	ActiveMob am = mm.getMythicMobInstance(e.getDamager());
	        	if (am.getStance().equalsIgnoreCase("gostealing")) {
	        		e.setCancelled(true);
	            }
	        }
		}
	}
}
