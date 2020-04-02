package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "forceshader", author = "BerndiVader")
public class ForceShaderMechanic extends SkillMechanic implements ITargetedEntitySkill {

	EntityType entityType;

	public ForceShaderMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;

		entityType = EntityType.valueOf(mlc.getString("type", "CREEPER"));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Player p = (Player) target.getBukkitEntity();
			Location l = p.getLocation();
			double dx = l.getX(), dz = l.getZ();
			LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(new Location(p.getWorld(), dx, 0, dz),
					entityType);
			entity.setAI(false);
			entity.setInvulnerable(false);
			Volatile.handler.forceSpectate(p, entity, true);
			return true;
		}
		return false;
	}
}
