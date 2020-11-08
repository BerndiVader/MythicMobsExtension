package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3.navigation;

import net.minecraft.server.v1_16_R3.ControllerMove;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.Vec3D;

public class ControllerVex extends ControllerMove {
	public ControllerVex(EntityInsentient monster) {
		super(monster);
	}

	@Override
	public void a() {
		if (this.h == ControllerMove.Operation.MOVE_TO) {
			this.a.setNoGravity(true);
			double d0 = this.b - this.a.locX();
			double d1 = this.c - this.a.locY();
			double d2 = this.d - this.a.locZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			if ((d3 = (double) MathHelper.sqrt(d3)) < this.a.getBoundingBox().a()) {
				this.h = ControllerMove.Operation.WAIT;
				a.setMot(a.getMot().a(0.5));
			} else {
				Vec3D mot = a.getMot();
				mot.add(d0 / d3 * 0.05 * this.e, d1 / d3 * 0.05 * this.e, d2 / d3 * 0.05 * this.e);
				a.setMot(mot);
				if (a.getGoalTarget() == null) {
					a.aD = a.yaw = (-(float) MathHelper.d(mot.x, mot.z)) * 57.295776f;
				} else {
					double d4 = a.getGoalTarget().locX() - a.locX();
					double d5 = a.getGoalTarget().locZ() - a.locZ();
					a.aD = a.yaw = (-(float) MathHelper.d(d4, d5)) * 57.295776f;
				}
			}
		} else {
			this.a.setNoGravity(false);
			this.a.u(0.0f);
			this.a.v(0.0f);
		}
	}
}
