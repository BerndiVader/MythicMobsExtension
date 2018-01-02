package com.gmail.berndivader.healthbar;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.healthbar.Healthbar;
import com.gmail.berndivader.healthbar.HealthbarHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class CreateHealthbar extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected double offset,hOffset,vOffset;
	protected String template;
	protected int counter;
	protected boolean ignoreYaw;
	
	public CreateHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.offset = mlc.getDouble("offset",2D);
		this.counter = mlc.getInteger("counter",200);
		this.hOffset=mlc.getDouble("so",0d);
		this.vOffset=mlc.getDouble("fo",0d);
		this.ignoreYaw=mlc.getBoolean("iy",false);
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
			new Healthbar(entity,this.offset,this.counter,this.template,this.hOffset,this.vOffset,this.ignoreYaw);
			return true;
		};
		return false;
	}

}
