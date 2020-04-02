package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "setitemcooldown", author = "BerndiVader")
public class SetItemCooldownMechanic extends SkillMechanic implements ITargetedEntitySkill {
	int j1;
	int i1;

	public SetItemCooldownMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		j(mlc.getInteger(new String[] { "ticks", "t" }, 0));
		i(mlc.getInteger(new String[] { "slot", "s" }, -1));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Player p = (Player) target.getBukkitEntity();
			return Volatile.handler.setItemCooldown(p, j1, i1);
		}
		return false;
	}

	void j(int j) {
		this.j1 = j;
	}

	void i(int j) {
		this.i1 = j;
	}
}
