package com.gmail.berndivader.MythicPlayers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.MythicPlayers.Mechanics.mmCreateActivePlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmNormalPlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmSetTarget;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class MythicPlayerMythicMobsLoadEvent implements Listener {

	@EventHandler
	public void onMythicMobsLoad(MythicMechanicLoadEvent e) {
		SkillMechanic skill;
		String mech = e.getMechanicName().toLowerCase();
		switch (mech) 
		{
			case "activeplayer": 
			case "activeplayer_ext": 
			{
				skill = new mmCreateActivePlayer(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			}
			case "normalplayer": 
			case "normalplayer_ext": 
			{
				skill = new mmNormalPlayer(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			}
			case "settarget": 
			case "settarget_ext": 
			{
				skill = new mmSetTarget(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			}
		}
	}

}
