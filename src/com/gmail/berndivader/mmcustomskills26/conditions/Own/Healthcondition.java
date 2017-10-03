package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class Healthcondition extends mmCustomCondition
implements
IEntityCondition {
	
	protected RangedDouble pc;

	public Healthcondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.pc=new RangedDouble(mlc.getString(new String[]{"percentage","p"},"1to100"),false);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		double maxHealth=entity.getMaxHealth();
		double health=entity.getHealth();
		double percent=(health*100)/maxHealth;
		return this.pc.equals(CustomSkillStuff.round(percent,0));
	}

}
