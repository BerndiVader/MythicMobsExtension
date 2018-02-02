package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class OnCooldownCondition 
extends
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble r;
	
	public OnCooldownCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		r(mlc.getString(new String[] {"value","v","amount","a","ticks","t"},"0.0d"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isPlayer()) {
			return r.equals((double)Volatile.handler.getItemCoolDown((Player) e.getBukkitEntity()));
		}
		return false;
	}
	
	private void r(String s) {
		this.r=new RangedDouble(s);
	}
}
