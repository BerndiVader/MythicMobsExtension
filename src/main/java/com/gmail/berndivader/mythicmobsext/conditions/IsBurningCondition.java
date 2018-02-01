package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class IsBurningCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble rd;

	public IsBurningCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.rd=new RangedDouble(mlc.getString(new String[] {"range","r"},">-1"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		return rd.equals(e.getBukkitEntity().getFireTicks());
	}

}
