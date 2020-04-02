package com.gmail.berndivader.mythicmobsext.healthbar;

import java.util.UUID;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class UpdateHealthbar extends SkillMechanic implements ITargetedEntitySkill {

	public UpdateHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		UUID uuid = target.getUniqueId();
		if (HealthbarHandler.healthbars.containsKey(uuid)) {
			Healthbar h = HealthbarHandler.healthbars.get(uuid);
			if (h != null) {
				h.updateHealth();
				return true;
			}
		}
		return false;
	}
}
