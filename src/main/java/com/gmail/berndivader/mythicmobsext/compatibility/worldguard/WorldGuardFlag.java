package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public 
class
WorldGuardFlag
implements 
Listener {
	static String pluginName="WorldGuard";

	public WorldGuardFlag() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		Main.logger.info("using "+pluginName);
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("wgstateflag")) {
			SkillCondition c = new WorldGuardStateFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("wgdenyspawnflag")) {
			SkillCondition c = new WorldGuardDenySpawnFlagCondition(e.getConditionName(), e.getConfig());
			e.register(c);
		} else if(e.getConditionName().toLowerCase().equals("worldguardflag")) {
			e.register(new WorldGuardFlagCondition(e.getConditionName(),e.getConfig()));
		} else if(e.getConditionName().toLowerCase().equals("regionname")) {
			e.register(new WorldGuardRegionCondition(e.getConditionName(),e.getConfig()));
		}
	}
}
