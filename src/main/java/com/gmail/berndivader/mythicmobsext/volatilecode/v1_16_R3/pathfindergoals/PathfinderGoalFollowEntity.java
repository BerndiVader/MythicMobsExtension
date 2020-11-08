package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3.pathfindergoals;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.Navigation;
import net.minecraft.server.v1_16_R3.NavigationAbstract;
import net.minecraft.server.v1_16_R3.NavigationFlying;
import net.minecraft.server.v1_16_R3.PathType;
import net.minecraft.server.v1_16_R3.PathfinderGoal;

import java.util.EnumSet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;

public class PathfinderGoalFollowEntity extends PathfinderGoal {
	private final EntityInsentient d;
	private final EntityLiving d1;
	private EntityLiving e;
	net.minecraft.server.v1_16_R3.World a;
	private final double f;
	private final NavigationAbstract g;
	private int h;
	float b;
	float c;
	private float i;

	public PathfinderGoalFollowEntity(EntityInsentient entity, EntityLiving entity1, double d0, float f, float f1) {
		this.d = entity;
		this.a = entity.world;
		this.d1 = entity1;
		this.f = d0;
		g = entity.getNavigation();
		c = f;
		b = f1;
		a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
		if ((!(entity.getNavigation() instanceof Navigation))
				&& (!(entity.getNavigation() instanceof NavigationFlying))) {
			throw new IllegalArgumentException("Unsupported mob type for FollowEntityGoal");
		}
	}

	public boolean a() {
		this.e = this.d1;
		if ((this.e == null || !this.e.isAlive())
				|| (this.e instanceof EntityHuman) && ((EntityHuman) this.e).isSpectator() || (d.h(this.e) < c * c))
			return false;
		return true;
	}

	public boolean b() {
		return (!g.n()) && (d.h(e) > b * b);
	}

	public void c() {
		h = 0;
		i = d.a(PathType.WATER);
		d.a(PathType.WATER, 0.0F);
	}

	public void d() {
		e = null;
		g.q();
		d.a(PathType.WATER, i);
	}

	public void e() {
		d.getControllerLook().a(e, 10.0F, d.Q());
		if (h-- <= 0) {
			h = 10;
			if ((!g.a(e, f)) && (!d.isLeashed()) && (!d.isPassenger()) && (d.h(e) >= 144.0D)) {
				int i = MathHelper.floor(e.locX()) - 2;
				int j = MathHelper.floor(e.locZ()) - 2;
				int k = MathHelper.floor(e.getBoundingBox().maxY);
				for (int l = 0; l <= 4; l++) {
					for (int i1 = 0; i1 <= 4; i1++) {
						if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3)) && (a(i, j, k, l, i1))) {
							CraftEntity entity = d.getBukkitEntity();
							Location to = new Location(entity.getWorld(), i + l + 0.5F, k, j + i1 + 0.5F, d.yaw,
									d.pitch);
							EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
							d.world.getServer().getPluginManager().callEvent(event);
							if (event.isCancelled())
								return;
							to = event.getTo();
							d.setPositionRotation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
							g.q();
							return;
						}
					}
				}
			}
		}
	}

	protected boolean a(int i, int j, int k, int l, int i1) {
		BlockPosition blockposition = new BlockPosition(i + l, k - 1, j + i1);
		IBlockData iblockdata = a.getType(blockposition);
		return (iblockdata.c(a, blockposition, EnumDirection.DOWN) == 1) && (a.isEmpty(blockposition.up()))
				&& (a.isEmpty(blockposition.up(2)));
	}
}
