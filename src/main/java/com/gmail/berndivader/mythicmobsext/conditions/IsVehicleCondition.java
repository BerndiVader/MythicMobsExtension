package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name="isvehicle",author="BerndiVader")
public class IsVehicleCondition
extends
AbstractCustomCondition
implements
IEntityComparisonCondition {

	public IsVehicleCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		Entity e1;
		if ((e1=caster.getBukkitEntity()).getVehicle()!=null) {
			return e1.getVehicle().equals(target.getBukkitEntity());
		}
		return false;
	}
}
