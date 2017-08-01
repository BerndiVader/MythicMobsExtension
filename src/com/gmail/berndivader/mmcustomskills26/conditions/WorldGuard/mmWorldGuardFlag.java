package com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class mmWorldGuardFlag implements Listener {

	public mmWorldGuardFlag() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		Bukkit.getLogger().info("registered WorldGuard conditions!");
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("wgstateflag")) {
			SkillCondition c = new mmWorldGuardStateFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("wgdenyspawnflag")) {
			SkillCondition c = new mmWorldGuardDenySpawnFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		}
	}
}
