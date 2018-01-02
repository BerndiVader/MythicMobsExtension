package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.Player;

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
			Main.getPlugin().getVolatileHandler().playEndScreenForPlayer((Player)target.getBukkitEntity(),1);
			return true;
		}
		return false;
	}
	
}
