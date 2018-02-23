package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="bloodyscreen",author="BerndiVader")
public class BloodyScreenMechanic
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	boolean bl1;

	public BloodyScreenMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bl1=mlc.getBoolean("play",true);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity var2) {
		if(var2.isPlayer()) {
			Volatile.handler.setWBWB((Player)var2.getBukkitEntity(),this.bl1);
		}
		return true;
	}

}
