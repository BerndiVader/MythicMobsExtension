package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.healthbar.Healthbar;
import com.gmail.berndivader.healthbar.HealthbarHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class createHealthbar extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected double offset;
	protected String template;
	protected int counter;
	
	public createHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.offset = mlc.getDouble("offset",2D);
		this.counter = mlc.getInteger("counter",200);
		String parse = mlc.getString("display");
		if (parse.startsWith("\"") 
				&& parse.endsWith("\"")) {
			parse = parse.substring(1, parse.length()-1);
		}
		this.template = SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!HealthbarHandler.healthbars.containsKey(target.getUniqueId())
				&& target.isLiving()) {
			LivingEntity entity = (LivingEntity)target.getBukkitEntity();
			new Healthbar(entity,this.offset,this.counter,this.template);
			return true;
		};
		return false;
	}

}
