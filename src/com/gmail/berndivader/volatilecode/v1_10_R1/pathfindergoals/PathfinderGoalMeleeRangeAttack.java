package com.gmail.berndivader.volatilecode.v1_10_R1.pathfindergoals;

import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;

public class PathfinderGoalMeleeRangeAttack extends PathfinderGoalMeleeAttack {
	protected float range;

	public PathfinderGoalMeleeRangeAttack(EntityCreature entityCreature, double d, boolean b, float range) {
		super(entityCreature, d, b);
		this.range=range;
	}

	@Override
	protected double a(EntityLiving entity) {
	    return (double)(this.b.width * this.range * this.b.width * this.range + entity.width);
	}
}
