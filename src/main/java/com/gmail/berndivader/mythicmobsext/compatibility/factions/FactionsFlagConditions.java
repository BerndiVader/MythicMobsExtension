package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class FactionsFlagConditions implements Listener {

	public FactionsFlagConditions() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("factionsflag")) {
			SkillCondition condition = new FactionsFlagCondition(e.getConditionName(), e.getConfig());
			e.register(condition);
		}
	}

}
