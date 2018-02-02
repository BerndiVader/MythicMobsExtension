package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import java.util.Optional;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EnumBlockFaceShape;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.NavigationFlying;
import net.minecraft.server.v1_12_R1.PathType;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.Vec3D;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class PathfinderGoalReturnHome extends PathfinderGoal {
	private final EntityInsentient d;
	private final Vec3D v;
	private Optional<ActiveMob> mM;
	private Vec3D aV;
	private final double f;
	private final double mR,tR;
	net.minecraft.server.v1_12_R1.World a;
	private final NavigationAbstract g;
	private int h;
	float b;
	float c;
	private float i;
	private boolean iF,iT;
	
	public PathfinderGoalReturnHome(EntityInsentient entity, double d0, double hx, double hy, double hz, double mR, double tR, boolean iT) {
		this.d=entity;
		this.f=d0;
		this.a=entity.world;
		g=entity.getNavigation();
		a(3);
		this.v = new Vec3D(hx,hy+(double)d.getHeadHeight(),hz);
		this.mR=mR;
		this.tR=tR;
		this.iF=false;
		this.iT=iT;
		if ((!(entity.getNavigation() instanceof Navigation)) && (!(entity.getNavigation() instanceof NavigationFlying))) {
			throw new IllegalArgumentException("Unsupported mob type for ReturnHomeGoal");
		}
		this.mM=Utils.mobmanager.getActiveMob(entity.getUniqueID());
	}

	public boolean a() {
		this.aV=new Vec3D(d.locX,d.locY,d.locZ);
		if (this.iT
				|| this.d.getGoalTarget()==null
				|| !this.d.getGoalTarget().isAlive()) {
			double ds=v.distanceSquared(this.aV);
			if (ds>this.mR) {
				return true;
			} else if (this.iF && ds>2.0D) return true;
		}
		return false;
	}
	
	public boolean b() {
		return (!g.o()) && v.distanceSquared(this.aV)>2.0D;
	}
	
	public void c() {
		h = 0;
		i = d.a(PathType.WATER);
		d.a(PathType.WATER, 0.0F);
		if (this.mM.isPresent()
				&& !this.iF) {
			ActiveMob am = this.mM.get();
			am.signalMob(null, "GOAL_STARTRETURNHOME");
		}
		this.iF=true;
	}
  
	public void d() {
		g.p();
		d.a(PathType.WATER, i);
		if (v.distanceSquared(this.aV)<10.0D) {
			this.iF=false;
			if (this.mM.isPresent()) {
				ActiveMob am = this.mM.get();
				am.signalMob(null, "GOAL_ENDRETURNHOME");
			}
		}
	}
	
	public void e() {
		d.getControllerLook().a(v.x,v.y,v.z,10.0F,d.N());
		if (--h<=0) {
			h=10;
			if (!g.a(v.x,v.y,v.z,f)
					&& (!d.isLeashed()) && (!d.isPassenger())
					&& v.distanceSquared(this.aV)>this.tR) {
				CraftEntity entity = d.getBukkitEntity();
				Location to = new Location(entity.getWorld(),v.x,v.y,v.z,d.yaw,d.pitch);
				EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
				d.world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) return;
				to = event.getTo();
				d.setPositionRotation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
				g.p();
				return;
			}
		}
	}
  
	protected boolean a(int i, int j, int k, int l, int i1) {
		BlockPosition blockposition = new BlockPosition(i + l, k - 1, j + i1);
		IBlockData iblockdata = a.getType(blockposition);
		return (iblockdata.d(a, blockposition, EnumDirection.DOWN) 
				== EnumBlockFaceShape.SOLID) 
				&& (iblockdata.a(d)) 
				&& (a.isEmpty(blockposition.up())) 
				&& (a.isEmpty(blockposition.up(2)));
	}
}
