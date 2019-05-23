package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name="reachable",author="BerndiVader")
public
class
ReachableCondition
extends
AbstractCustomCondition 
implements
IEntityComparisonCondition 
{
	
	public ReachableCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractEntity e, AbstractEntity t) {
		if(e.isLiving()&&t.isLiving()) return Volatile.handler.isReachable((LivingEntity)e.getBukkitEntity(),(LivingEntity)t.getBukkitEntity());
		return false;
	}
}
