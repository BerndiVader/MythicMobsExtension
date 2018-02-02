package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class GetDamageIndicatorCondition
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble rd;

	public GetDamageIndicatorCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		rd(mlc.getString("value","<1.1"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isPlayer()) {
			return this.rd.equals((double)Volatile.handler.getIndicatorPercentage((Player)e.getBukkitEntity()));
		}
		return true;
	}
	
	private void rd(String s1) {
		this.rd=new RangedDouble(s1);
	}


}
