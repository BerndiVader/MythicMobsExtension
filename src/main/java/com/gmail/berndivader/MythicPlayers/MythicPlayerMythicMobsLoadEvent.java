package main.java.com.gmail.berndivader.MythicPlayers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.com.gmail.berndivader.MythicPlayers.Mechanics.mmCreateActivePlayer;
import main.java.com.gmail.berndivader.MythicPlayers.Mechanics.mmNormalPlayer;
import main.java.com.gmail.berndivader.MythicPlayers.Mechanics.mmSetTarget;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class MythicPlayerMythicMobsLoadEvent implements Listener {

	@EventHandler
	public void onMythicMobsLoad(MythicMechanicLoadEvent e) {
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

}
