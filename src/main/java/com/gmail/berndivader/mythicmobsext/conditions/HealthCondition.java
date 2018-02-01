package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

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
		return this.pc.equals(Utils.round(percent,0));
	}

}
