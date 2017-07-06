package com.gmail.berndivader.MythicPlayers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.MythicPlayers.Mechanics.mmCreateActivePlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmNormalPlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmSetTarget;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class MythicPlayerMythicMobsLoadEvent implements Listener {
	
	private String MechName;
	private SkillMechanic skill;
	
	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		MechName = e.getMechanicName().toLowerCase();
		if (MechName.equals("activeplayer")) {
			skill = new mmCreateActivePlayer(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
		} else if (MechName.equals("normalplayer")) {
			skill = new mmNormalPlayer(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
		} else if (MechName.equals("settarget")) {
			skill = new mmSetTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
		}
	}
	
}
