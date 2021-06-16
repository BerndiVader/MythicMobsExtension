package com.gmail.berndivader.mythicmobsext.healthbar;

import java.util.UUID;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;

public class ChangeHealthbar extends SkillMechanic implements ITargetedEntitySkill {

	protected String display;

	public ChangeHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		String parse = mlc.getString(new String[] { "display", "text", "t" }, "$h");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse = parse.substring(1, parse.length() - 1);
		}
		this.display = SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		UUID uuid = target.getUniqueId();
		if (HealthbarHandler.healthbars.containsKey(uuid)) {
			Healthbar h = HealthbarHandler.healthbars.get(uuid);
			if (h != null) {
				h.changeDisplay(this.display);
				return true;
			}
		}

		return false;
	}
}
