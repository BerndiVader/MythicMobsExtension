package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="changegamemode",author="BerndiVader")
public class ChangeGamemodeMechanic
extends 
SkillMechanic
implements
ITargetedEntitySkill {
	
	GameMode mode;

	public ChangeGamemodeMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		try {
			mode=GameMode.valueOf(mlc.getString("mode","SURVIVAL").toUpperCase());
		} catch(Exception ex) {
			mode=GameMode.SURVIVAL;
			Main.logger.warning("UNKNOWN GAMEMODETYPE. USING SURVIVAL INSTEAD");
		}
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		if(entity.isPlayer()) {
			((Player)entity.getBukkitEntity()).setGameMode(mode);
			return true;
		}
		return false;
	}


}
