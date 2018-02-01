package main.java.com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class PlayCreditsMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	public PlayCreditsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Volatile.handler.playEndScreenForPlayer((Player)target.getBukkitEntity(),1);
			return true;
		}
		return false;
	}
	
}
