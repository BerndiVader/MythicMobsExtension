package com.gmail.berndivader.mythicmobsext.bossbars.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class
RemoveBossBar 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	
	String bar_name;
	
	public RemoveBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bar_name=mlc.getString("title","Bar");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			if(BossBars.contains(abstract_entity.getUniqueId())) {
				BossBars.removeBar((Player)abstract_entity.getBukkitEntity(),bar_name);
				return true;
			}
		}
		return false;
	}
	
}
