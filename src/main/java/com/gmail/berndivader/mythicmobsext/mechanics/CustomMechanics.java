package com.gmail.berndivader.mythicmobsext.mechanics;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.Internals;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class CustomMechanics implements Listener {
	Internals internals=Main.getPlugin().internals;
	
	public CustomMechanics() {
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}
	
	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		String mech=e.getMechanicName().toLowerCase();
		if (Internals.mechanics.containsKey(mech)) {
			if (registerMechanic(internals.loader.loadM(mech),e)) {
				Internals.ml++;
			}
		} else if (Externals.mechanics.containsKey(mech)) {
			if (registerMechanic(Externals.mechanics.get(mech),e)) {
				Externals.ml++;
			}
		}
	}
	
	private boolean registerMechanic(Class<? extends SkillMechanic>cl1,MythicMechanicLoadEvent e) {
		SkillMechanic skill=null;
		try {
			skill = cl1.getConstructor(String.class,MythicLineConfig.class).newInstance(e.getContainer().getConfigLine(),e.getConfig());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		if (skill!=null) {
			e.register(skill);
			return true;
		}
		return false;
	}
}
