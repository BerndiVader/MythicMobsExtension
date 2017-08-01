package com.gmail.berndivader.mmcustomskills26.conditions.MobArena;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class mmMobArenaConditions implements Listener {

	public mmMobArenaConditions() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		Bukkit.getLogger().info("registered MobArena conditions!");
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		String conditionName = e.getConditionName().toLowerCase();
		if (conditionName.equals("inmobarena")) {
			SkillCondition c = new inMobArenaCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		}
	}
}