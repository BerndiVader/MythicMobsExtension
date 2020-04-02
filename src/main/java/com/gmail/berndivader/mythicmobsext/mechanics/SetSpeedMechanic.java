package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "setspeed,randomspeed", author = "BerndiVader")
public class SetSpeedMechanic extends SkillMechanic implements ITargetedEntitySkill, INoTargetSkill {
	private String s1;
	boolean bl1;

	public SetSpeedMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		s1 = mlc.getString(new String[] { "amount", "a", "range", "r" }, "0.2D").toLowerCase();
		bl1 = mlc.getBoolean("debug", false);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return d(data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return d(target);
	}

	private boolean d(AbstractEntity target) {
		if (target.isLiving()) {
			((LivingEntity) target.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
					.setBaseValue(MathUtils.randomRangeDouble(s1));
			LivingEntity l = (LivingEntity) target.getBukkitEntity();
			if (bl1) {
				System.out.println("randomspeed debug");
				System.out.println("Value: " + l.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
				System.out.println("Base: " + l.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
			}
			return true;
		}
		return false;
	}
}
