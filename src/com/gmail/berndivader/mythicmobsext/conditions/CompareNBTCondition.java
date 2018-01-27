package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class CompareNBTCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private String s1;

	public CompareNBTCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		s1=mlc.getString("nbt",null);
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (s1!=null) {
			String s=s1.replaceAll("\\(","{").replaceAll("\\)","}");
			return Main.getPlugin().getVolatileHandler().getNBTValueOf(e.getBukkitEntity(),s,false);
		}
		return false;
	}

}
