package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RemoveArrowsMechanic 
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private int a;
	private char m;

	public RemoveArrowsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.m=mlc.getString("mode","C").toUpperCase().charAt(0);
		this.a=mlc.getInteger("amount",1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			Main.getPlugin().getVolatileHandler().removeArrowsFromEntity(target.getBukkitEntity(),this.a,this.m);
			return true;
		}
		return false;
	}

}
