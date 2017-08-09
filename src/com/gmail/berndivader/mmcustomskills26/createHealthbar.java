package com.gmail.berndivader.mmcustomskills26;

import com.gmail.berndivader.healthbar.Healthbar;
import com.gmail.berndivader.healthbar.HealthbarHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class createHealthbar extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected double offset;
	protected String string;
	
	public createHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.offset = mlc.getDouble("offset",2D);
		this.string = mlc.getString("text","Health: ");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!HealthbarHandler.healthbars.containsKey(target.getUniqueId())) {
			new Healthbar(target.getBukkitEntity(),this.offset,this.string);
		};
		return false;
	}

}
