package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class advAIPathFinderSelector extends SkillMechanic 
implements
INoTargetSkill {
	
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
	public boolean cast(SkillMetadata data) {
		Entity e = data.getCaster().getEntity().getBukkitEntity();
		if (e instanceof LivingEntity) {
			vh.aiPathfinderGoal((LivingEntity)data.getCaster().getEntity().getBukkitEntity(), this.goal);
			return true;
		}
		return false;
	}
}
