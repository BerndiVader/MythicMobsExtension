package com.gmail.berndivader.mythicmobsext.bossbars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.bossbars.mechanics.CreateBossBar;
import com.gmail.berndivader.mythicmobsext.bossbars.mechanics.ProgressBossBar;
import com.gmail.berndivader.mythicmobsext.bossbars.mechanics.RemoveBossBar;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;

public class BossBars implements Listener {
	static HashMap<UUID, List<BossBar>> bars;

	static {
		bars = new HashMap<>();
	}

	public static boolean contains(UUID uuid) {
		return bars.containsKey(uuid);
	}

	public static boolean sizeReached(UUID uuid) {
		return bars.get(uuid).size() > 3;
	}

	public static void addBar(UUID uuid, BossBar bar) {
		if (bars.containsKey(uuid)) {
			bars.get(uuid).add(bar);
		} else {
			List<BossBar> playerbars = new ArrayList<>();
			playerbars.add(bar);
			bars.put(uuid, playerbars);
		}
	}

	public static BossBar getBar(UUID uuid, String bar_name) {
		Iterator<BossBar> bar_iter = bars.get(uuid).iterator();
		while (bar_iter.hasNext()) {
			BossBar bar = bar_iter.next();
			if (bar.getTitle().equals(bar_name))
				return bar;
		}
		return null;
	}

	public static void removeBar(Entity entity, String title) {
		Iterator<BossBar> bar_iter = bars.get(entity.getUniqueId()).iterator();
		while (bar_iter.hasNext()) {
			BossBar bar = bar_iter.next();
			if (bar.getTitle().equals(title)) {
				bar.removeAll();
				bar_iter.remove();
				bar = null;
			}
		}
	}

	public BossBars() {
		Main.pluginmanager.registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void removeBarsOnQuit(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (contains(uuid)) {
			Iterator<BossBar> bar_iter = bars.get(uuid).iterator();
			while (bar_iter.hasNext()) {
				BossBar bar = bar_iter.next();
				bar.removeAll();
				bar_iter.remove();
				bar = null;
			}
		}
	}

	@EventHandler
	public void loadMechanicsEvent(MythicMechanicLoadEvent e) {
		switch (e.getMechanicName().toLowerCase()) {
		case "createbossbar":
		case "createbossbar_ext":
			e.register(new CreateBossBar(e.getContainer().getConfigLine(), e.getConfig()));
			break;
		case "removebossbar":
		case "removebossbar_ext":
			e.register(new RemoveBossBar(e.getContainer().getConfigLine(), e.getConfig()));
			break;
		case "progressbossbar":
		case "progressbossbar_ext":
			e.register(new ProgressBossBar(e.getContainer().getConfigLine(), e.getConfig()));
			break;
		}
	}

	@EventHandler
	public void loadConditionsEvent(MythicConditionLoadEvent e) {
		switch (e.getConditionName().toLowerCase()) {
		case "progressbossbar":
		case "progressbossbar_ext":
			e.register(new com.gmail.berndivader.mythicmobsext.bossbars.conditions.ProgressBossBar(
					e.getConfig().getLine(), e.getConfig()));
			break;
		}
	}

}
