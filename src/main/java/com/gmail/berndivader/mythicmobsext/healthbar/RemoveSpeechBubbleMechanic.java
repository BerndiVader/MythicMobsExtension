package com.gmail.berndivader.mythicmobsext.healthbar;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RemoveSpeechBubbleMechanic extends SkillMechanic implements INoTargetSkill {
	private String id;

	public RemoveSpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.id = mlc.getString("id", "bubble");
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (HealthbarHandler.speechbubbles
				.containsKey(data.getCaster().getEntity().getUniqueId().toString() + this.id)) {
			HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId().toString() + this.id)
					.remove();
			return true;
		}
		return false;
	}
}
