package com.gmail.berndivader.mythicmobsext.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.ScriptException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;

public class MythicMobsReload 
implements
Listener {
	
	public MythicMobsReload() {
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicMobsReload(MythicReloadedEvent e) {
		try {
			Nashorn.scripts=new String(Files.readAllBytes(Paths.get(Utils.str_PLUGINPATH,Nashorn.filename)));
			Nashorn.nash.eval(Nashorn.scripts);
		} catch (IOException | ScriptException ex) {
			ex.printStackTrace();
		}
		Main.logger.info("Javascripts reloaded.");
	}

}
