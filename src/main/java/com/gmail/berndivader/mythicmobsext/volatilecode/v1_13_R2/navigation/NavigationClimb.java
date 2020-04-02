package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.navigation;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.PathEntity;
import net.minecraft.server.v1_13_R2.World;

public class NavigationClimb extends Navigation {
	private BlockPosition i;

	public NavigationClimb(EntityInsentient entityInsentient, World world) {
		super(entityInsentient, world);
		NMSUtils.setField("moveController", EntityInsentient.class, this.a, new MoveController(this.a));
	}

	@Override
	public PathEntity b(BlockPosition blockPosition) {
		this.i = blockPosition;
		return super.b(blockPosition);
	}

	@Override
	public PathEntity a(Entity entity) {
		this.i = new BlockPosition(entity);
		return super.a(entity);
	}

	@Override
	public boolean a(Entity entity, double d2) {
		PathEntity pathEntity = this.a(entity);
		if (pathEntity != null) {
			return this.a(pathEntity, d2);
		}
		this.i = new BlockPosition(entity);
		this.d = d2;
		return true;
	}

	@Override
	public void d() {
		if (this.p()) {
			if (this.i != null) {
				double d2 = this.a.width * this.a.width;
				if (this.a.d(this.i) < d2 || this.a.locY > (double) this.i.getY() && this.a
						.d(new BlockPosition(this.i.getX(), MathHelper.floor(this.a.locY), this.i.getZ())) < d2) {
					this.i = null;
				} else {
					if (!(this.a.getControllerMove() instanceof MoveController)) {
						NMSUtils.setField("moveController", EntityInsentient.class, this.a, new MoveController(this.a));
					}
					((MoveController) this.a.getControllerMove()).aa(this.i.getX(), this.i.getY(), this.i.getZ(),
							this.d);
				}
			}
			return;
		}
		super.d();
	}

	class MoveController extends ControllerMove {

		public MoveController(EntityInsentient arg0) {
			super(arg0);
		}

		public void aa(double d2, double d3, double d4, double d5) {
			this.b = d2;
			this.c = d3;
			this.d = d4;
			this.e = d5;
			this.h = Operation.MOVE_TO;
		}
	}
}
