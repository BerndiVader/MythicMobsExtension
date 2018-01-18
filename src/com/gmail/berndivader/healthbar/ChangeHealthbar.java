package com.gmail.berndivader.healthbar;

import java.util.UUID;

import com.gmail.berndivader.healthbar.Healthbar;
import com.gmail.berndivader.healthbar.HealthbarHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class ChangeHealthbar extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected String display;

	public ChangeHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		String parse = mlc.getString(new String[] { "display", "dur", "text", "t" }, "$h");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse = parse.substring(1, parse.length()-1);
		}
		this.display = SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		UUID uuid = target.getUniqueId();
		if (HealthbarHandler.healthbars.containsKey(uuid)) {
			Healthbar h = HealthbarHandler.healthbars.get(uuid);
			if (h!=null) {
				h.changeDisplay(this.display);
				return true;
			}
		}
		
		return false;
	}

}
