package com.gmail.berndivader.mythicmobsext.conditions;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.Internals;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class CustomConditions implements Listener {
	Internals internals=Main.getPlugin().internals;

	public CustomConditions() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicMobsConditionsLoadEvent1(MythicConditionLoadEvent e) {
		String name=e.getConditionName().toLowerCase();
		if (Internals.conditions.containsKey(name)) {
			if (registerCondition(internals.loader.loadC(name),e)) {
				Internals.cl++;
			}
		} else if (Externals.conditions.containsKey(name)) {
			if (registerCondition(Externals.conditions.get(name),e)) {
				Externals.cl++;
			}
		}
		
	}
	
	private boolean registerCondition(Class<? extends SkillCondition>cl1,MythicConditionLoadEvent e) {
		SkillCondition cond=null;
		try {
			cond=cl1.getConstructor(String.class,MythicLineConfig.class).newInstance(e.getConfig().getLine(),e.getConfig());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		if (cond!=null) {
			e.register(cond);
			return true;
		}
		return false;
	}	
}
