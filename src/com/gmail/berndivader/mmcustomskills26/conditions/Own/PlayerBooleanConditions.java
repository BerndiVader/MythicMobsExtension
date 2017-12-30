package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class PlayerBooleanConditions
extends
mmCustomCondition 
implements
IEntityCondition {
	private char tt;

	public PlayerBooleanConditions(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.tt=line.toUpperCase().charAt(0);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			switch(this.tt) {
			case 'C':
				return Main.getPlugin().getVolatileHandler().playerIsCrouching((Player)entity.getBukkitEntity());
			case 'R':
				return Main.getPlugin().getVolatileHandler().playerIsRunning((Player)entity.getBukkitEntity());
			case 'S':
				return Main.getPlugin().getVolatileHandler().playerIsSleeping((Player)entity.getBukkitEntity());
			case 'J':
				return Main.getPlugin().getVolatileHandler().playerIsJumping((Player)entity.getBukkitEntity());
			}
		}
		return false;
	}
}
