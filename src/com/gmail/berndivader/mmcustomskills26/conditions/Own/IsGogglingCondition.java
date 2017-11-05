package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.PlayerGoggleMechanic;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class IsGogglingCondition
extends
mmCustomCondition
implements
IEntityCondition {

	public IsGogglingCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		return entity.getBukkitEntity().hasMetadata(PlayerGoggleMechanic.str);
	}

}
