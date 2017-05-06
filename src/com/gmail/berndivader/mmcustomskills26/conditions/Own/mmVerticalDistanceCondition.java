package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class mmVerticalDistanceCondition extends SkillCondition implements ILocationComparisonCondition {
	private RangedDouble rd;
	
	public mmVerticalDistanceCondition(String line, MythicLineConfig mlc) {
		super(line);
	    String d = mlc.getString(new String[] { "distance", "d" },"1");
	    this.rd = new RangedDouble(d, false);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action","a"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
	}

	@Override
	public boolean check(AbstractLocation caster, AbstractLocation target) {
		return rd.equals((double)target.getBlockY() - caster.getBlockY());
	}
}
