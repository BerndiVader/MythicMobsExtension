package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "advaipathfinder,custompathfinder", author = "BerndiVader")
public class AdvAIPathFinderMechanic extends SkillMechanic implements ITargetedEntitySkill {

	Handler vh = Volatile.handler;
	PlaceholderString g;

	public AdvAIPathFinderMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		String parse = mlc.getString("goal", "");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse = parse.substring(1, parse.length() - 1);
		}
		this.g = new PlaceholderString(SkillString.parseMessageSpecialChars(parse));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		LivingEntity lS = null, lT = null;
		if (t != null && t.isLiving()) {
			lT = (LivingEntity) t.getBukkitEntity();
		}
		if (data.getCaster().getEntity().isLiving()) {
			lS = (LivingEntity) data.getCaster().getEntity().getBukkitEntity();
		}
		if (lS != null) {
			String pG = this.g.get(data, t);
			vh.aiPathfinderGoal(lS, pG, lT);
			return true;
		}
		return false;
	}
}
