package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

@ExternalAnnotation(name="arrowcount",author="BerndiVader")
public class ArrowOnEntityCondition
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble c;

	public ArrowOnEntityCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.c=new RangedDouble(mlc.getString("amount",">0"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isLiving()) {
			return this.c.equals(Volatile.handler.arrowsOnEntity(entity.getBukkitEntity()));
		}
		return false;
	}
}
