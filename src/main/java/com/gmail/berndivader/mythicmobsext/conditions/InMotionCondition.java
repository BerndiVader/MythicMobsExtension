package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class InMotionCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition {
	public InMotionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isLiving()&&!e.isPlayer()) {
			return Volatile.handler.inMotion((LivingEntity)e.getBukkitEntity());
		} else if (e.isPlayer()) {
			return Utils.playerInMotion((Player)e.getBukkitEntity());
		}
		return false;
	}
}
