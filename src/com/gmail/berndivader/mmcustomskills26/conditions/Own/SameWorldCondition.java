package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;

public class SameWorldCondition
extends
mmCustomCondition
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
