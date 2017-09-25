package com.gmail.berndivader.MythicPlayers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.MythicPlayers.Mechanics.mmCreateActivePlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmNormalPlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmSetTarget;
import com.gmail.berndivader.MythicPlayers.Targeters.mmCrosshairTargeter;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class MythicPlayerMythicMobsLoadEvent implements Listener {

	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		SkillMechanic skill;
		String mech = e.getMechanicName().toLowerCase();
		switch (mech) {
		case "activeplayer": {
			skill = new mmCreateActivePlayer(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "normalplayer": {
			skill = new mmNormalPlayer(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "settarget": {
			skill = new mmSetTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		}}
	}

	@EventHandler
	public void onMythicMobsTargetersLoad(MythicTargeterLoadEvent e) {
		String TargeterName = e.getTargeterName().toLowerCase();
		if (TargeterName.equals("targetertest") || TargeterName.equals("che")) {
			SkillTargeter targeter = new mmCrosshairTargeter(e.getConfig());
			e.register(targeter);
		}
	}
}
