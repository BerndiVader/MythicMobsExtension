package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;
import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class InMotionCondition extends mmCustomCondition
implements
IEntityCondition {
	protected VolatileHandler vh = Main.getPlugin().getVolatileHandler();

	public InMotionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isLiving() 
				&& !e.isPlayer()) return this.vh.inMotion((LivingEntity)e.getBukkitEntity());
		return false;
	}
}
