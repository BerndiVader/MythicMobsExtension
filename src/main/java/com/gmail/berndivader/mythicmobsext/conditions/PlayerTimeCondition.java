package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class PlayerTimeCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble time;

	public PlayerTimeCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.time=new RangedDouble(mlc.getString(new String[] {"time","t"},"0"));
	}

	@Override
	public boolean check(AbstractEntity target) {
		if (target.isPlayer()) {
			Player p=(Player)target.getBukkitEntity();
			return this.time.equals(p.getPlayerTime());
		}
		return false;
	}
}
