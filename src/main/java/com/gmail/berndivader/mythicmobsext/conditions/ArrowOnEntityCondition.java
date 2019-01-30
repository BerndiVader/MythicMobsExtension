package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name="arrowcount",author="BerndiVader")
public class ArrowOnEntityCondition
extends 
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble c;

	public ArrowOnEntityCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.c=new RangedDouble(mlc.getString("amount",">0"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isLiving()) {
			return this.c.equals(NMSUtils.getArrowsOnEntity((LivingEntity)entity.getBukkitEntity()));
		}
		return false;
	}
}
