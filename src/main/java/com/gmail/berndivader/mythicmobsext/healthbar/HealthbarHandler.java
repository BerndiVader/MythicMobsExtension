package com.gmail.berndivader.mythicmobsext.healthbar;

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

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class HealthbarHandler implements Listener {

	static Plugin plugin;
	static Logger logger;
	static String str_pluginName;
	public static ConcurrentHashMap<UUID, Healthbar> healthbars;
	public static ConcurrentHashMap<String, SpeechBubble> speechbubbles;

	HealthbarClock clock;

	static {
		plugin = Main.getPlugin();
		logger = Main.logger;
		str_pluginName = "HolograpicDisplays";
		healthbars = new ConcurrentHashMap<UUID, Healthbar>();
		speechbubbles = new ConcurrentHashMap<String, SpeechBubble>();
		logger.info("using " + str_pluginName);
	}

	public HealthbarHandler(Plugin plugin) {
		clock = new HealthbarClock();
		Main.pluginmanager.registerEvents(this, plugin);
	}

	public ConcurrentHashMap<UUID, Healthbar> getHealthbars() {
		return HealthbarHandler.healthbars;
	}

	public void removeHealthbars() {
		healthbars.forEach((uuid, healthbar) -> {
			healthbar.remove();
		});
	}

	public void removeSpeechBubbles() {
		speechbubbles.forEach((uuid, bubbles) -> {
			bubbles.remove();
		});
	}

	public class HealthbarClock implements Runnable {
		protected BukkitTask taskId;

		public HealthbarClock() {
			this.taskId = Bukkit.getScheduler().runTaskTimer(HealthbarHandler.plugin, () -> {
				this.run();
			}, 0L, 1L);
		}

		@Override
		public void run() {
			HealthbarHandler.healthbars.forEach((uuid, healthbar) -> {
				Entity e = healthbar.entity;
				if (e == null || e.isDead()) {
					healthbar.remove();
				} else {
					healthbar.update();
				}
			});
			HealthbarHandler.speechbubbles.forEach((idx, bubble) -> {
				Entity e = bubble.entity;
				if (e == null || e.isDead()) {
					bubble.remove();
				} else {
					bubble.update();
				}
			});
		}
	}

	@EventHandler
	public void registerMythicMobsMechanics(MythicMechanicLoadEvent e) {
		String mechanic = e.getMechanicName().toLowerCase();
		SkillMechanic skill;
		switch (mechanic) {
		case "createhealthbar":
		case "createhealthbar_ext":
			skill = new CreateHealthbar(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "changehealthbar":
		case "changehealthbar_ext":
			skill = new ChangeHealthbar(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "updatehealthbar":
		case "updatehealthbar_ext":
			skill = new UpdateHealthbar(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "speechbubble":
		case "speechbubble_ext":
			skill = new SpeechBubbleMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "removebubble":
		case "removebubble_ext":
			skill = new RemoveSpeechBubbleMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "modifybubble":
		case "modifybubble_ext":
			skill = new ModifySpeechBubbleMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		case "linebubble":
		case "linebubble_ext":
			skill = new LineSpeechBubbleMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
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
					if (h != null) {
						h.updateHealth();
					}
				}
			}
		}.runTaskLater(HealthbarHandler.plugin, 1L);
	}

}
