package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class VerticalDistanceCondition
extends
AbstractCustomCondition
implements
ILocationComparisonCondition {
	private RangedDouble rd;

	public VerticalDistanceCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		String d=mlc.getString(new String[] { "distance", "d" }, "1");
		this.rd=new RangedDouble(d);
	}

	@Override
	public boolean check(AbstractLocation caster, AbstractLocation target) {
		return rd.equals((double)target.getBlockY()-caster.getBlockY());
	}
}
