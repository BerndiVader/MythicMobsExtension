package com.gmail.berndivader.mythicmobsext.javascript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.ScriptException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;

public class NashornMythicMobsEvents 
implements
Listener {
	
	public NashornMythicMobsEvents() {
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicMobsReload(MythicReloadedEvent e) {
		try {
			Nashorn.scripts=new String(Files.readAllBytes(Paths.get(Utils.str_PLUGINPATH,Nashorn.filename)));
			Nashorn.get().nash.eval(Nashorn.scripts);
		} catch (IOException | ScriptException ex) {
			ex.printStackTrace();
		}
		Main.logger.info("Javascripts reloaded.");
	}
	
	@EventHandler
	public void onMechanicLoadEvent(MythicMechanicLoadEvent e) {
		if (e.getMechanicName().toLowerCase().equals("jsmechanic")) {
			e.register(new JavascriptMechanic(e.getContainer().getConfigLine(),e.getConfig()));
		}
	}
	
	@EventHandler
	public void onConditionLoadEvent(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("jscondition")) {
			e.register(new JavascriptCondition(e.getConfig().getLine(), e.getConfig()));
		}
	}

}
