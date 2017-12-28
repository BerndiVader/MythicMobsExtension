package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class OnCooldownCondition 
extends
mmCustomCondition
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
			return r.equals((double)Main.getPlugin().getVolatileHandler().getItemCoolDown((Player) e.getBukkitEntity()));
		}
		return false;
	}
	
	private void r(String s) {
		this.r=new RangedDouble(s);
	}
}
