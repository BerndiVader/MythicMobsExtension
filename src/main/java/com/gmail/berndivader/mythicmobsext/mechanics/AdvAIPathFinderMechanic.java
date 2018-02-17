package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

@ExternalAnnotation(name="advaipathfinder,custompathfinder",author="BerndiVader")
public class AdvAIPathFinderMechanic extends SkillMechanic
implements
ITargetedEntitySkill {
	
	Handler vh=Volatile.handler;
	String g;
	
	public AdvAIPathFinderMechanic(String skill, MythicLineConfig mlc) {
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
