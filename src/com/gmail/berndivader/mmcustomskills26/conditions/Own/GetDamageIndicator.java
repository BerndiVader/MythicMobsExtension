package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class GetDamageIndicator 
extends 
mmCustomCondition
implements
IEntityCondition {
	private RangedDouble rd;

	public GetDamageIndicator(String line, MythicLineConfig mlc) {
		super(line, mlc);
		rd(mlc.getString("value","<1.1"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isPlayer()) {
			return this.rd.equals((double)Main.getPlugin().getVolatileHandler().getIndicatorPercentage((Player)e.getBukkitEntity()));
		}
		return true;
	}
	
	private void rd(String s1) {
		this.rd=new RangedDouble(s1);
	}


}
