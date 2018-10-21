package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="clearactiveitem,resetshield",author="BerndiVader")
public 
class 
ClearActiveItemMechanic
extends 
SkillMechanic
implements
ITargetedEntitySkill {
	
	public ClearActiveItemMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		if(entity.isPlayer()) {
			Volatile.handler.clearActiveItem((Player)entity.getBukkitEntity());
			return true;
		}
		return false;
	}


}
