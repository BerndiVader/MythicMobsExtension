package com.gmail.berndivader.mythicmobsext.targeters;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class CustomTargeters
implements
Listener {
	
	public CustomTargeters() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicTargetersLoad(MythicTargeterLoadEvent e) {
		SkillTargeter st;
		if ((st=getCustomTargeter(e.getTargeterName(),e.getConfig()))!=null) e.register(st);
	}

	public static SkillTargeter getCustomTargeter(String s1,MythicLineConfig mlc) {
		String TargeterName = s1.toLowerCase();
		switch (TargeterName) {
		case "crosshair":
		case "crosshairentity":{
			return new CrosshairTargeter(mlc);
		}
		case "crosshairlocation":{
			return new CrosshairLocationTargeter(mlc);
		}
		case "ownertarget": {
			return new OwnerTargetTargeter(mlc);
		}
		case "lastdamager": {
			return new LastDamagerTargeter(mlc);
		}
		case "targetmotion":
		case "triggermotion":
		case "selfmotion":
		case "ownermotion":{
			return new TargetMotionTargeter(mlc);
		}
		case "triggerstarget": {
			return new TriggerTargetTargeter(mlc);
		}
		case "targetstarget": {
			return new TargetsTargetTargeter(mlc);
		}
		case "eyedirection": {
			return new EyeDirectionTargeter(mlc);
		}
		case "triggerdirection": {
			return new TriggerDirectionTargeter(mlc);
		}}
		return null;
	}
}
