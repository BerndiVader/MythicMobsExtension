package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R2.pathfindergoals;

import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.PathfinderGoal;

public class PathfinderGoalJumpOffFromVehicle extends PathfinderGoal {
	protected EntityInsentient e;

	public PathfinderGoalJumpOffFromVehicle(EntityInsentient e2) {
		this.e = e2;
	}

	@Override
	public boolean a() {
		return e.getVehicle() != null;
	}

	@Override
	public boolean b() {
		if (e.getGoalTarget() != null)
			e.stopRiding();
		return false;
	}
}
