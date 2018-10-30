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
ITargetedEntitySkill 
{
	boolean bl1;
	int density;
	

	public BloodyScreenMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bl1=mlc.getBoolean("play",true);
		density=mlc.getInteger("density",1);
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity var2) {
		if(var2.isPlayer()) {
			Volatile.handler.setWorldborder((Player)var2.getBukkitEntity(),this.density,this.bl1);
		}
		return true;
	}

}
