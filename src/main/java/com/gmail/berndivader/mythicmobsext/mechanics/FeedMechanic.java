package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class FeedMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	private int amount;

	public FeedMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.amount=mlc.getInteger(new String[] { "amount", "a" },1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()) return false;
		Player p=(Player)target.getBukkitEntity();
		p.setFoodLevel(Integer.min(p.getFoodLevel()+amount,20));
		return true;
	}

}
