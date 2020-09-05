package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name = "oncooldown", author = "BerndiVader")
public class OnCooldownCondition extends AbstractCustomCondition implements IEntityCondition {
	RangedDouble r;
	int i1;

	public OnCooldownCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		r(mlc.getString(new String[] { "value", "v", "amount", "a", "ticks", "t" }, "0.0d"));
		i(mlc.getInteger(new String[] { "slot", "s", }, -1));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isPlayer()) {
			double cooldown = Volatile.handler.getItemCoolDown((Player) e.getBukkitEntity(), this.i1);
			return r.equals((double) Volatile.handler.getItemCoolDown((Player) e.getBukkitEntity(), this.i1));
		}
		return false;
	}

	void r(String s) {
		this.r = new RangedDouble(s);
	}

	void i(int i) {
		this.i1 = i;
	}
}
