package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="spidernavigation",author="BerndiVader")
public class NavigationMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {

	public NavigationMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(target.isLiving()) {
			LivingEntity e=(LivingEntity)target.getBukkitEntity();
			Volatile.handler.cNav(e);
		}
		return true;
	}

}
