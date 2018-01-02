package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

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
			return this.c.equals(Main.getPlugin().getVolatileHandler().arrowsOnEntity(entity.getBukkitEntity()));
		}
		return false;
	}
}
