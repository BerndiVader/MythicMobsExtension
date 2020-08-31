package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R1.pathfindergoals;

import java.util.EnumSet;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.EntityLiving;
import net.minecraft.server.v1_16_R1.EnumHand;
import net.minecraft.server.v1_16_R1.PathfinderGoal;
import net.minecraft.server.v1_16_R1.SoundEffects;
import net.minecraft.server.v1_16_R1.Vec3D;

public class PathfinderGoalVexA extends PathfinderGoal {
	EntityInsentient entity;
	BlockPosition c;
	DataWatcherObject<Byte> a;

	public PathfinderGoalVexA(EntityInsentient monster) {
		this.entity = monster;
		a(EnumSet.of(PathfinderGoal.Type.MOVE));
		this.a = (DataWatcherObject<Byte>) NMSUtils.getField("a", EntityInsentient.class, entity);
//    	System.err.println(a!=null);
	}

	@Override
	public boolean a() {
		boolean bl1 = entity.getGoalTarget() != null && !entity.getControllerMove().b() && Main.random.nextInt(7) == 0
				? entity.h(entity.getGoalTarget()) > 4.0
				: false;
//        System.err.println("a:"+bl1);
		return bl1;
	}

	@Override
	public boolean b() {
		boolean bl1 = entity.getControllerMove().b() && this.c(1) && entity.getGoalTarget() != null
				&& entity.getGoalTarget().isAlive();
//        System.err.println("b:"+bl1);
		return bl1;
	}

	private boolean c(int i2) {
		byte b0 = entity.getDataWatcher().get(a).byteValue();
		boolean bl1 = (b0 & i2) != 0;
//        System.err.println("c:"+bl1);
		return bl1;
	}

	private void a(int i2, boolean flag) {
		byte b0 = entity.getDataWatcher().get(this.a).byteValue();
		int j = flag ? b0 | i2 : b0 & ~i2;
		entity.swingHand(EnumHand.MAIN_HAND);
//        entity.getDataWatcher().set(a,Byte.valueOf((byte)(j&255)));
//        System.err.println("aa:"+b0+":"+j);
	}

	@Override
	public void c() {
		EntityLiving entityliving = entity.getGoalTarget();
		Vec3D vec3d = entityliving.f(1.0f);
		entity.getControllerMove().a(vec3d.x, vec3d.y, vec3d.z, 1.0);
		a(1, true);
		entity.playSound(SoundEffects.ENTITY_VEX_CHARGE, 1.0f, 1.0f);
//        System.err.println("cc");
	}

	@Override
	public void d() {
		this.a(1, false);
	}

	@Override
	public void e() {
//        System.err.println("ee");
		EntityLiving entityliving = entity.getGoalTarget();
		if (entity.getBoundingBox().c(entityliving.getBoundingBox())) {
			entity.attackEntity(entityliving);
			a(1, false);
		} else {
			double d0 = entity.h(entityliving);
			if (d0 < 9.0) {
				Vec3D vec3d = entityliving.f(1.0f);
				entity.getControllerMove().a(vec3d.x, vec3d.y, vec3d.z, 1.0);
			}
		}
	}
}