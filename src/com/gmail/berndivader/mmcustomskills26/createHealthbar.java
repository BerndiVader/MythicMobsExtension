package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;

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
	
	public createHealthbar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.offset = mlc.getDouble("offset",2D);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!HealthbarHandler.healthbars.containsKey(target.getUniqueId())
				&& target.isLiving()) {
			LivingEntity entity = (LivingEntity)target.getBukkitEntity();
			new Healthbar(entity,this.offset);
			return true;
		};
		return false;
	}

}
