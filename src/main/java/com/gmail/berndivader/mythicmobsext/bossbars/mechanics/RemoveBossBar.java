package com.gmail.berndivader.mythicmobsext.bossbars.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

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
	
	String title;
	
	public RemoveBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		title=mlc.getString("title","Bar");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			if(BossBars.contains(abstract_entity.getUniqueId())) {
				BossBars.removeBar((Player)abstract_entity.getBukkitEntity(),Utils.parseMobVariables(title,data,data.getCaster().getEntity(),abstract_entity,null));
				return true;
			}
		}
		return false;
	}
	
}
