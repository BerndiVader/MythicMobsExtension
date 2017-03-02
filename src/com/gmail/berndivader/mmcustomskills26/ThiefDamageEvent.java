package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class ThiefDamageEvent implements Listener {
	
	private MythicMobs mm = Main.mm;
	
	@EventHandler
	public void onThiefDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof ArmorStand) {
			return;
		}
        if (mm.getMobManager().isActiveMob(e.getDamager().getUniqueId())) {
        	if (e.getEntityType() == EntityType.PLAYER) {
	        	ActiveMob am = mm.getMobManager().getMythicMobInstance(e.getDamager());
	        	if (am.getStance().equalsIgnoreCase("gostealing")) {
	        		e.setCancelled(true);
	            }
	        }
		}
	}
}
