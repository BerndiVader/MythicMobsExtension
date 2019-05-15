package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="openbackbag",author="BerndiVader")
public
class
OpenBackBag 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	
	int size;

	public OpenBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		size=mlc.getInteger("size",9);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		
		if(abstract_entity.isPlayer()) {
			Player player=(Player)abstract_entity.getBukkitEntity();
			BackBag bag=new BackBag(player,size);
			bag.viewBackBag(player);
		}
		
		return false;
	}
}
