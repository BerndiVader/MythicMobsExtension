package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.compatibility.CompatibilityManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

@ExternalAnnotation(name="parseddisguise",author="BerndiVader")
public class ParsedDisguiseMechanic extends SkillMechanic
implements
ITargetedEntitySkill {

	private String disguise;
	public ParsedDisguiseMechanic(String skill, MythicLineConfig mlc) {
	
		super(skill, mlc);
		this.disguise=mlc.getString(new String[] { "disguise", "d" }, "Notch");
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (CompatibilityManager.LibsDisguises != null) {
			String d=SkillString.unparseMessageSpecialChars(this.disguise);
			d=Utils.parseMobVariables(d,data,data.getCaster().getEntity(),target,null);
			CompatibilityManager.LibsDisguises.setDisguise((ActiveMob)data.getCaster(), d);
			return true;
		}
		return false;
	}
}
