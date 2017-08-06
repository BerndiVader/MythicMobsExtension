package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class advAIPathFinderSelector extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected VolatileHandler vh = Main.getPlugin().getVolatileHandler();
	protected String goal;
	
	public advAIPathFinderSelector(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		String parse=mlc.getString("goal");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse=parse.substring(1, parse.length()-1);
		}
		this.goal=SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity e = data.getCaster().getEntity().getBukkitEntity();
		if (e instanceof LivingEntity) {
			String pGoal = SkillString.parseMobVariables(this.goal, data.getCaster(), target, data.getTrigger());
			vh.aiPathfinderGoal((LivingEntity)data.getCaster().getEntity().getBukkitEntity(), pGoal);
			return true;
		}
		return false;
	}
}
