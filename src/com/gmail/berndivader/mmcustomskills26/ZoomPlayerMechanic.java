package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ZoomPlayerMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	private float f1;

	public ZoomPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		f(mlc.getFloat(new String[] {"value","v","amount","a"},0.0f));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Main.getPlugin().getVolatileHandler().setFieldOfViewPacketSend((Player)target.getBukkitEntity(),this.f1);
			return true;
		}
		return false;
	}
	
	private void f(float f1) {
		this.f1=f1>1.0f?1.0f:f1<0.0f?0.0f:f1;
	}

}
