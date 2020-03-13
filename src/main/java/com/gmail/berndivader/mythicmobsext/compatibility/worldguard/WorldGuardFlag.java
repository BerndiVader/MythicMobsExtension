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
		switch(e.getConditionName().toLowerCase())
		{
			case "wgstateflag":
			case "wgstateflag_ext":
			{
				SkillCondition c = new WorldGuardStateFlagCondition(e.getConditionName(), e.getConfig());
				e.register(c);
			}
			case "wgdenyspawnflag":
			case "wgdenyspawnflag_ext":
			{
				SkillCondition c = new WorldGuardDenySpawnFlagCondition(e.getConditionName(), e.getConfig());
				e.register(c);
			}
			case "worldguardflag":
			case "worldguardflag_ext":
			{
				e.register(new WorldGuardFlagCondition(e.getConditionName(),e.getConfig()));
			}
			case "regionname":
			case "regionname_ext":
			{
				e.register(new WorldGuardRegionCondition(e.getConditionName(),e.getConfig()));
			}
			case "entitiesinregion":
			case "entitiesinregion_ext":
			{
				e.register(new EntitiesInRegionCondition(e.getConditionName(),e.getConfig()));
			}
			case "memberregion":
			case "memberregion_ext":
			{
				e.register(new MemberRegion(e.getConditionName(),e.getConfig()));
			}
		}
	}
}
