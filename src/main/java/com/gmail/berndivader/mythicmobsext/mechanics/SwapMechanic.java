package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "swap", author = "BerndiVader")
public class SwapMechanic extends SkillMechanic implements ITargetedEntitySkill {
	private boolean keepTargetYaw;
	private boolean keepCasterYaw;

	public SwapMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		this.keepTargetYaw = mlc.getBoolean(new String[] { "keeptargetyaw", "kty" }, false);
		this.keepCasterYaw = mlc.getBoolean(new String[] { "keepcasteryaw", "kcy" }, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		AbstractLocation tl = target.getLocation().clone();
		AbstractLocation cl = data.getCaster().getLocation().clone();
		if (this.keepTargetYaw)
			cl.setYaw(target.getLocation().getYaw());
		if (this.keepCasterYaw)
			tl.setYaw(data.getCaster().getLocation().getYaw());
		target.teleport(cl);
		data.getCaster().getEntity().teleport(tl);
		return true;
	}

}
