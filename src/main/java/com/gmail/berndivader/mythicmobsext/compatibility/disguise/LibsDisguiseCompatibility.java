package com.gmail.berndivader.mythicmobsext.compatibility.disguise;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;

public 
class 
LibsDisguiseCompatibility 
implements 
Listener {

	public LibsDisguiseCompatibility() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent e) {
		if (e.getMechanicName().toLowerCase().equals("parseddisguise")) {
			e.register(new ParsedDisguiseMechanic(e.getContainer().getConfigLine(),e.getConfig()));
		}
	}
}
