package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetItemCooldownMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	private int j;

	public SetItemCooldownMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		j(mlc.getInteger(new String[] { "ticks", "t" }, 0));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Player p=(Player)target.getBukkitEntity();
			return Volatile.handler.setItemCooldown(p,j);
		}
		return false;
	}

	private void j(int j1) {
		this.j=j1;
	}
}
