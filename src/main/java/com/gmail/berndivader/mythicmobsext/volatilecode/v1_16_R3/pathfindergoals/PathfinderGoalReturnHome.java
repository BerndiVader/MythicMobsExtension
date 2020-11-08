package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3.pathfindergoals;

import java.util.EnumSet;
import java.util.Optional;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.Navigation;
import net.minecraft.server.v1_16_R3.NavigationAbstract;
import net.minecraft.server.v1_16_R3.NavigationFlying;
import net.minecraft.server.v1_16_R3.PathType;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.Vec3D;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class PathfinderGoalReturnHome extends PathfinderGoal {
	private final EntityInsentient d;
	private final Vec3D v;
	private Optional<ActiveMob> mM;
	private Vec3D aV;
	private final double f;
	private final double mR, tR;
	net.minecraft.server.v1_16_R3.World a;
	private final NavigationAbstract g;
	private int h;
	float b;
	float c;
	private float i;
	private boolean iF, iT;

	public PathfinderGoalReturnHome(EntityInsentient entity, double d0, double hx, double hy, double hz, double mR,
			double tR, boolean iT) {
		this.d = entity;
		this.f = d0;
		this.a = entity.world;
		g = entity.getNavigation();
		this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
		this.v = new Vec3D(hx, hy + (double) d.getHeadHeight(), hz);
		this.mR = mR;
		this.tR = tR;
		this.iF = false;
		this.iT = iT;
		if ((!(entity.getNavigation() instanceof Navigation))
				&& (!(entity.getNavigation() instanceof NavigationFlying))) {
			throw new IllegalArgumentException("Unsupported mob type for ReturnHomeGoal");
		}
		this.mM = Utils.mobmanager.getActiveMob(entity.getUniqueID());
	}

	public boolean a() {
		this.aV = new Vec3D(d.locX(), d.locY(), d.locZ());
		if (this.iT || this.d.getGoalTarget() == null || !this.d.getGoalTarget().isAlive()) {
			double ds = v.distanceSquared(this.aV);
			if (ds > this.mR) {
				return true;
			} else if (this.iF && ds > 2.0D)
				return true;
		}
		return false;
	}

	public boolean b() {
		return (!g.n()) && v.distanceSquared(this.aV) > 2.0D;
	}

	public void c() {
		h = 0;
		i = d.a(PathType.WATER);
		d.a(PathType.WATER, 0.0F);
		if (this.mM.isPresent() && !this.iF) {
			ActiveMob am = this.mM.get();
			am.signalMob(null, "GOAL_STARTRETURNHOME");
		}
		this.iF = true;
	}

	public void d() {
		g.q();
		d.a(PathType.WATER, i);
		if (v.distanceSquared(this.aV) < 10.0D) {
			this.iF = false;
			if (this.mM.isPresent()) {
				ActiveMob am = this.mM.get();
				am.signalMob(null, "GOAL_ENDRETURNHOME");
			}
		}
	}

	public void e() {
		d.getControllerLook().a(v.x, v.y, v.z, 10.0F, d.Q());
		if (h-- <= 0) {
			h = 10;
			if (!g.a(v.x, v.y, v.z, f) && (!d.isLeashed()) && (!d.isPassenger())
					&& v.distanceSquared(this.aV) > this.tR) {
				CraftEntity entity = d.getBukkitEntity();
				Location to = new Location(entity.getWorld(), v.x, v.y, v.z, d.yaw, d.pitch);
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

	protected boolean a(int i, int j, int k, int l, int i1) {
		BlockPosition blockposition = new BlockPosition(i + l, k - 1, j + i1);
		IBlockData iblockdata = a.getType(blockposition);
		return (iblockdata.c(a, blockposition, EnumDirection.DOWN) == 1) && (a.isEmpty(blockposition.up()))
				&& (a.isEmpty(blockposition.up(2)));
	}
}
