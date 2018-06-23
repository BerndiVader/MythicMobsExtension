package com.gmail.berndivader.mythicmobsext.healthbar;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class CreateHealthbar extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	protected double offset;
	protected double hOffset;
	protected double vOffset;
	protected String display;
	protected int counter;
	protected boolean ignoreYaw;
	
	public CreateHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.offset = mlc.getDouble(new String[] { "offset", "o" }, 2D);
		this.counter = mlc.getInteger(new String[] { "counter", "c" }, 200);
		this.hOffset = mlc.getDouble(new String[] { "sideoffset", "so" }, 0D);
		this.vOffset = mlc.getDouble(new String[] { "forwardoffset", "fo" }, 0D);
		this.ignoreYaw = mlc.getBoolean(new String[] { "ignoreyaw", "iy" }, false);
		String parse = mlc.getString(new String[] { "display","text","t" },"$h");
		if (parse.startsWith("\"") && parse.endsWith("\"")) {
			parse=parse.substring(1, parse.length()-1);
		}
		this.display = SkillString.parseMessageSpecialChars(parse);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!HealthbarHandler.healthbars.containsKey(target.getUniqueId()) && target.isLiving()) {
			LivingEntity entity = (LivingEntity)target.getBukkitEntity();
			new Healthbar(entity,this.offset,this.counter,this.display,this.hOffset,this.vOffset,this.ignoreYaw);
			return true;
		};
		return false;
	}

}
