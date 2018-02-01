package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class MovementSpeedCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	private RangedDouble r1;

	public MovementSpeedCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		r1=new RangedDouble(mlc.getString("range",">0"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (e.isLiving()) {
			LivingEntity le1=(LivingEntity)e.getBukkitEntity();
			return r1.equals((double)le1.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
		}
		return false;
	}

}
