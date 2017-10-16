package com.gmail.berndivader.mmcustomskills26.conditions.Factions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class mmFactionsFlag implements Listener {

	public mmFactionsFlag() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		Bukkit.getLogger().info("registered Factions conditions!");
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("factionsflag")) {
			SkillCondition condition = new mmFactionsFlagCondition(e.getConditionName(), e.getConfig());
			e.register(condition);
		} else if (e.getConditionName().toLowerCase().equals("hasfaction")) {
			SkillCondition c = new mmHasFactionCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		}
	}

}
