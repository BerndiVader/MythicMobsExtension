package com.gmail.berndivader.mythicmobsext.targeters;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class CustomTargeters
implements
Listener {
	
	public CustomTargeters() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicMobsTargetersLoad(MythicTargeterLoadEvent e) {
		String TargeterName = e.getTargeterName().toLowerCase();
		switch (TargeterName) {
		case "crosshair": {
			SkillTargeter targeter = new CrosshairTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
		case "ownertarget": {
			SkillTargeter targeter=new OwnerTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
		case "lastdamager": {
			SkillTargeter targeter=new LastDamagerTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "triggerstarget": {
			SkillTargeter targeter=new TriggerTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "targetstarget": {
			SkillTargeter targeter=new TargetsTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "eyedirection": {
			SkillTargeter targeter=new EyeDirectionTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "triggerdirection": {
			SkillTargeter targeter=new TriggerDirectionTargeter(e.getConfig());
			e.register(targeter);
			break;
		}}
	}
}
