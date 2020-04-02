package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;

@ExternalAnnotation(name = "despawning", author = "BerndiVader")
public class SetDespawnMechanic extends SkillMechanic implements ITargetedEntitySkill, INoTargetSkill {
	boolean set;

	public SetDespawnMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		set = mlc.getBoolean("set", true);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return this.castAtEntity(data, data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			LivingEntity entity = (LivingEntity) target.getBukkitEntity();
			entity.setRemoveWhenFarAway(set);
			return true;
		}
		return false;
	}
}
