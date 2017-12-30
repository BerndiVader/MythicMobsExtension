package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

public class IsVehicleCondition
extends
mmCustomCondition
implements
IEntityComparisonCondition {

	public IsVehicleCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		if (caster.getVehicle()!=null) return caster.getVehicle().equals(target);
		return false;
	}

}
