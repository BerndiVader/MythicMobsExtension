package com.gmail.berndivader.mythicmobsext.guardianbeam;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;

public 
class
GuardianBeam 
implements
Listener
{
	
	public GuardianBeam(Plugin plugin) {
		Main.pluginmanager.registerEvents(this, plugin);
	}
	
	public void registerMechanics(MythicMechanicLoadEvent e) {
		String mechanic=e.getMechanicName().toLowerCase();
		
		if(mechanic.equals("guardianbeam")) {
			e.register(new GuardianBeamMechanic(e.getContainer().getConfigLine(), e.getConfig()));
		}
	}

}
