package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name="health",author="BerndiVader")
public class HealthCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	protected RangedDouble pc;

	public HealthCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.pc=new RangedDouble(mlc.getString(new String[]{"percentage","p"},"1to100"),false);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		double maxHealth=entity.getMaxHealth();
		double health=entity.getHealth();
		double percent=(health*100)/maxHealth;
		return this.pc.equals(MathUtils.round(percent,0));
	}

}
