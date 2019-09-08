package com.gmail.berndivader.mythicmobsext.compatibility.disguise;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.compatibility.CompatibilityManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class ParsedDisguiseMechanic extends SkillMechanic
implements
ITargetedEntitySkill {

	PlaceholderString disguise;
	public ParsedDisguiseMechanic(String skill, MythicLineConfig mlc) {
	
		super(skill, mlc);
		this.disguise=mlc.getPlaceholderString(new String[] { "disguise", "d" }, "Notch");
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (CompatibilityManager.LibsDisguises != null) {
			String d=disguise.get(data,target);
			switch(d.toUpperCase()) {
			case "STEVE":
			case "ALEX":
				break;
			default:
				CompatibilityManager.LibsDisguises.setDisguise((ActiveMob)data.getCaster(), d);
			}
			return true;
		}
		return false;
	}
}
