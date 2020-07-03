package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

@ExternalAnnotation(name = "motionspeed", author = "BerndiVader")
public class MotionSpeed extends AbstractCustomCondition implements IEntityCondition {
	private RangedDouble r1;

	public MotionSpeed(String line, MythicLineConfig mlc) {
		super(line, mlc);
		r1 = new RangedDouble(mlc.getString("range", ">0"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isLiving()) {
			Vec3D motion=NMSUtils.getEntityLastMot(e.getBukkitEntity());
			LivingEntity le1 = (LivingEntity) e.getBukkitEntity();
			return r1.equals((double) le1.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
		}
		return false;
	}

}
