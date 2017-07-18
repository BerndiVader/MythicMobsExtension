package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class mmOwnConditions implements Listener {
	
	public mmOwnConditions() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		Bukkit.getLogger().info("Register CustomConditions");
	}
	
	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		String conditionName = e.getConditionName().toLowerCase();
		if (conditionName.equals("vdistance")) {
			SkillCondition c = new mmVerticalDistanceCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("hastarget")) {
			SkillCondition c = new mmHasTargetCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("mobsinradius")) {
			SkillCondition c = new mmMobsInRadiusCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("lastdamagecause")) {
			SkillCondition c = new mmLastDamageCauseCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("stunned") 
				|| conditionName.equals("isstunned")) {
			SkillCondition c = new mmIsStunnedCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("biomefix")) {
			SkillCondition c = new mmBiomeFixCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		} else if (conditionName.equals("hasmeta")) {
			SkillCondition c = new mmHasMetaTagCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		}
	}
}
