package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="resetshader",author="BerndiVader")
public class ResetShaderMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill {
	
	public ResetShaderMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Volatile.handler.forceSpectate((Player)target.getBukkitEntity(),target.getBukkitEntity(),false);
			return true;
		} 
		return false;
	}
}
