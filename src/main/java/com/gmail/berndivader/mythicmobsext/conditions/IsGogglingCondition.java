package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.mechanics.PlayerGoggleMechanic;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class IsGogglingCondition
extends
AbstractCustomCondition
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
