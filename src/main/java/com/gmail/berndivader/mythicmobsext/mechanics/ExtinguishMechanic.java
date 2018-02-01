package main.java.com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ExtinguishMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {

	public ExtinguishMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity e=target.getBukkitEntity();
		e.setFireTicks(-1);
		return true;
	}
}
