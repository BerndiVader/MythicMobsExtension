package com.gmail.berndivader.healthbar;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.changeHealthbar;
import com.gmail.berndivader.mmcustomskills26.createHealthbar;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class HealthbarHandler implements Listener {
	protected static Plugin plugin=Main.getPlugin();
	protected static Logger logger=Main.logger;
	protected static HealthbarClock clock;
	public static ConcurrentHashMap<UUID,Healthbar> healthbars;
	
	public HealthbarHandler(Plugin plugin) {
		HealthbarHandler.plugin = plugin;
		HealthbarHandler.healthbars = new ConcurrentHashMap<UUID,Healthbar>();
		HealthbarHandler.clock = new HealthbarClock();
		Main.pluginmanager.registerEvents(this, plugin);
	}
	
	public ConcurrentHashMap<UUID,Healthbar> getHealthbars() {
		return HealthbarHandler.healthbars;
	}
	
	public class HealthbarClock implements Runnable {
		protected BukkitTask taskId;
		
		public HealthbarClock() {
			this.taskId = Bukkit.getScheduler().runTaskTimer(HealthbarHandler.plugin, () -> {
				this.run();
	        },0L,1L);
		}
		
		@Override
		public void run() {
			HealthbarHandler.healthbars.forEach((uuid,healthbar)-> {
				Entity e = NMSUtils.getEntity(healthbar.getWorld(), uuid);
				if (e==null || e.isDead()) {
					healthbar.remove();
				} else {
					healthbar.update();
				}
			});
		}
	}
	
	@EventHandler
	public void registerMythicMobsMechanics(MythicMechanicLoadEvent e) {
		String mechanic = e.getMechanicName().toLowerCase();
		SkillMechanic skill;
		switch (mechanic) {
		case "createhealthbar": {
			skill = new createHealthbar(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}
		case "changehealthbar": {
			skill = new changeHealthbar(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}
	    }
	}
	
	@EventHandler
	public void updateHealthbar(EntityDamageEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (HealthbarHandler.healthbars.containsKey(e.getEntity().getUniqueId())) {
					UUID uuid = e.getEntity().getUniqueId();
					Healthbar h = HealthbarHandler.healthbars.get(uuid);
					if (h!=null) {
						h.updateHealth();
					}
				}
			}
		}.runTaskLater(HealthbarHandler.plugin, 1L);
	}
	
}
