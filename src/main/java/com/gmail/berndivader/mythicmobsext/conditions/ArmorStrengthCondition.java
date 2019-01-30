package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="armorstrength",author="BerndiVader")
public class ArmorStrengthCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {
	RangedDouble range;

	public ArmorStrengthCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		range=new RangedDouble(mlc.getString(new String[] {"range","r","a","amount"},">0"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isLiving()) {
			return range.equals(NMSUtils.getArmorStrength((LivingEntity)entity.getBukkitEntity()));
		}
		return false;
	}
}
