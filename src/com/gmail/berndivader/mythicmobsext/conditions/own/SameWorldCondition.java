package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;

public class SameWorldCondition
extends
AbstractCustomCondition
implements
ILocationComparisonCondition {

	public SameWorldCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractLocation c, AbstractLocation t) {
		return c.getWorld()==null||t.getWorld()==null?false:c.getWorld()==t.getWorld()?true:false;
	}

}
