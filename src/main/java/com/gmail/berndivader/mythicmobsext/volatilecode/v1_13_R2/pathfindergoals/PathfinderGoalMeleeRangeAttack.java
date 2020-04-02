package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.pathfindergoals;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;

public class PathfinderGoalMeleeRangeAttack extends PathfinderGoalMeleeAttack {
	protected float range;

	public PathfinderGoalMeleeRangeAttack(EntityCreature entityCreature, double d, boolean b, float range) {
		super(entityCreature, d, b);
		this.range = range;
	}

	@Override
	protected double a(EntityLiving entity) {
		return (double) (this.a.width * this.range * this.a.width * this.range + entity.width);
	}
}
