package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="playerzoom",author="BerndiVader")
public class PlayerZoomMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	private Float f1;

	public PlayerZoomMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		f(mlc.getFloat(new String[] { "value", "v", "amount", "a" }, 0.0F));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Volatile.handler.setFieldOfViewPacketSend((Player)target.getBukkitEntity(),this.f1);
			return true;
		}
		return false;
	}
	
	private void f(Float f1) {
		this.f1=f1>1.0f?1.0f:f1<0.0f?0.0f:f1;
	}

}
