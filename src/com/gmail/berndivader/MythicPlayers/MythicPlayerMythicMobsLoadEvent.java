package com.gmail.berndivader.MythicPlayers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.MythicPlayers.Mechanics.mmCreateActivePlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmNormalPlayer;
import com.gmail.berndivader.MythicPlayers.Mechanics.mmSetTarget;
import com.gmail.berndivader.MythicPlayers.Targeters.CrosshairTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.EyeDirectionTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.LastDamagerTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.OwnerTargetTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.TargetsTargetTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.TriggerDirectionTargeter;
import com.gmail.berndivader.MythicPlayers.Targeters.TriggerTargetTargeter;

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
		switch (TargeterName) {
		case "crosshair": {
			SkillTargeter targeter = new CrosshairTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
		case "ownertarget": {
			SkillTargeter targeter=new OwnerTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
		case "lastdamager": {
			SkillTargeter targeter=new LastDamagerTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "triggerstarget": {
			SkillTargeter targeter=new TriggerTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "targetstarget": {
			SkillTargeter targeter=new TargetsTargetTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "eyedirection": {
			SkillTargeter targeter=new EyeDirectionTargeter(e.getConfig());
			e.register(targeter);
			break;
		}case "triggerdirection": {
			SkillTargeter targeter=new TriggerDirectionTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
		}
	}
}
