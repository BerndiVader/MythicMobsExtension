package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="hastarget",author="BerndiVader")
public class HasTargetCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {
	
	int length;

	public HasTargetCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		length=mlc.getInteger("length",32);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			return Utils.getTargetedEntity((Player)entity.getBukkitEntity(),length)!=null;
		} else {
			return entity.getTarget() != null;
		}
	}
}
