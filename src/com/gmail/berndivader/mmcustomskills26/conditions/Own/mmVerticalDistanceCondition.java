package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class mmVerticalDistanceCondition extends mmCustomCondition implements ILocationComparisonCondition {
	private RangedDouble rd;

	public mmVerticalDistanceCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String d = mlc.getString(new String[] { "distance", "d" }, "1");
		this.rd = new RangedDouble(d, false);
	}

	@Override
	public boolean check(AbstractLocation caster, AbstractLocation target) {
		return rd.equals((double) target.getBlockY() - caster.getBlockY());
	}
}
