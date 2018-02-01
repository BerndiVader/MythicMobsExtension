package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import main.java.com.gmail.berndivader.mythicmobsext.mechanics.StunMechanic;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class IsStunnedCondition 
extends 
AbstractCustomCondition 
implements 
IEntityCondition {

	public IsStunnedCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity target) {
		return target.getBukkitEntity().hasMetadata(StunMechanic.str);
	}

}
