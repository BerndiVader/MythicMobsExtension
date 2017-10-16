package com.gmail.berndivader.mmcustomskills26;

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
	protected String g;
	
	public advAIPathFinderSelector(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		String parse=mlc.getString("goal");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse=parse.substring(1, parse.length()-1);
		}
		this.g=SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		LivingEntity lS = null, lT = null;
		if (t!=null && t.isLiving()) {
			lT = (LivingEntity)t.getBukkitEntity();
		}
		if (data.getCaster().getEntity().isLiving()) {
			lS = (LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		}
		if (lS!=null) {
			String pG = SkillString.parseMobVariables(this.g, data.getCaster(), t, data.getTrigger());
			vh.aiPathfinderGoal(lS, pG, lT);
			return true;
		}
		return false;
	}
}
