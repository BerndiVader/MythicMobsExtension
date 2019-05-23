package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="forceslot",author="BerndiVader")
public
class 
ForceSlot 
extends 
SkillMechanic 
implements
ITargetedEntitySkill
{
	int slot;

	public ForceSlot(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		
		this.slot=mlc.getInteger("slot",0);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_target) {
		if(abstract_target.isPlayer()) {
			Player player=(Player)abstract_target.getBukkitEntity();
			player.getInventory().setHeldItemSlot(slot);
		}
		return false;
	}

}
