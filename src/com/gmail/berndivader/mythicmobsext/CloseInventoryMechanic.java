package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CloseInventoryMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {

	public CloseInventoryMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Main.getPlugin().getVolatileHandler().forceCancelEndScreenPlayer((Player)target.getBukkitEntity());
			return true;
		}
		return false;
	}
}
