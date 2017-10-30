package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class IsBurningCondition
extends
mmCustomCondition
implements
IEntityCondition {

	public IsBurningCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity e) {
		return e.getBukkitEntity().getFireTicks()>-1;
	}

}
