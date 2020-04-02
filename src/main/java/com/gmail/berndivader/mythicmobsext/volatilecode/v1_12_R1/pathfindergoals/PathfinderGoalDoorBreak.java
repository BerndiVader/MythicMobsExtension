package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockDoor;
import net.minecraft.server.v1_12_R1.EntityInsentient;

public class PathfinderGoalDoorBreak extends PathfinderGoalInteractDoor {
	int g;
	int h = -1;
	boolean flag;

	public PathfinderGoalDoorBreak(EntityInsentient e, boolean bl1) {
		super(e, bl1);
	}

	public boolean a() {
		if (!super.a())
			return false;
		if (bl1 && !this.a.world.getGameRules().getBoolean("mobGriefing"))
			return false;
		return !BlockDoor.d(this.a.world, this.b);
	}

	@Override
	public void c() {
		super.c();
		this.g = 0;
	}

	@Override
	public boolean b() {
		double d0 = this.a.c(this.b);
		if (this.g <= 240 && !BlockDoor.d(this.a.world, this.b) && d0 < 4.0) {
			flag = true;
			return flag;
		}
		flag = false;
		return flag;
	}

	@Override
	public void d() {
		super.d();
		this.a.world.c(this.a.getId(), this.b, -1);
	}

	@Override
	public void e() {
		super.e();
		if (this.a.getRandom().nextInt(20) == 0)
			this.a.world.triggerEffect(1019, this.b, 0);
		this.g++;
		int i2 = (int) ((float) this.g / 240.0f * 10.0f);
		if (i2 != this.h) {
			this.a.world.c(this.a.getId(), this.b, i2);
			this.h = i2;
		}
		if (this.g == 240) {
			if (CraftEventFactory.callEntityBreakDoorEvent(this.a, this.b.getX(), this.b.getY(), this.b.getZ())
					.isCancelled()) {
				this.c();
				return;
			}
			this.a.world.setAir(this.b);
			this.a.world.triggerEffect(1021, this.b, 0);
			this.a.world.triggerEffect(2001, this.b, Block.getId(this.c));
		}
	}
}
