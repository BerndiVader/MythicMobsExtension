package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCreateActivePlayer extends SkillMechanic implements ITargetedEntitySkill {
	private String mobtype;

	public mmCreateActivePlayer(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.mobtype = mlc.getString(new String[]{"mobtype","type","mob","t","m"},"Player");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(this.mobtype);
        if (mm==null || !target.isPlayer() || MythicMobs.inst().getMobManager().isActiveMob(target)) return false;
        return ActivePlayerStuff.createActivePlayer((LivingEntity) target.getBukkitEntity(), mm);
	}
	
}
