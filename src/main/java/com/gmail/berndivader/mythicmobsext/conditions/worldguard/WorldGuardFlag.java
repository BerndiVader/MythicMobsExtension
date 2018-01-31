package com.gmail.berndivader.mythicmobsext.conditions.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class WorldGuardFlag implements Listener {

	public WorldGuardFlag() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("wgstateflag")) {
			SkillCondition c = new WorldGuardStateFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("wgdenyspawnflag")) {
			SkillCondition c = new WorldGuardDenySpawnFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		}
	}
}
