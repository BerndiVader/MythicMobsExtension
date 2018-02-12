package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.ConditionAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ConditionAnnotation(name="hastarget",author="BerndiVader")
public class HasTargetCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {

	public HasTargetCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			return Utils.getTargetedEntity((Player)entity.getBukkitEntity())!=null;
		} else {
			return entity.getTarget() != null;
		}
	}
}
