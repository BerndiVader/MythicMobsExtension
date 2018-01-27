package com.gmail.berndivader.volatilecode.v1_11_R1.pathfindergoals;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.Navigation;
import net.minecraft.server.v1_11_R1.NavigationAbstract;
import net.minecraft.server.v1_11_R1.PathType;
import net.minecraft.server.v1_11_R1.PathfinderGoal;

public class PathfinderGoalFollowEntity extends PathfinderGoal {
	private final EntityInsentient d;
	private final EntityLiving d1;
	private EntityLiving e;
	net.minecraft.server.v1_11_R1.World a;
	private final double f;
	private final NavigationAbstract g;
	private int h;
	float b;
	float c;
	private float i;
  
	public PathfinderGoalFollowEntity(EntityInsentient entity, EntityLiving entity1, double d0, float f, float f1) {
		this.d=entity;
		this.a=entity.world;
		this.d1=entity1;
		this.f = d0;
		g = entity.getNavigation();
		c = f;
		b = f1;
		a(3);
		if ((!(entity.getNavigation() instanceof Navigation))) {
			throw new IllegalArgumentException("Unsupported mob type for FollowEntityGoal");
		}
	}
  
	public boolean a() {
		this.e=this.d1;
		if ((this.e==null||!this.e.isAlive())
				|| (this.e instanceof EntityHuman)&&((EntityHuman)this.e).isSpectator()
				|| (d.h(this.e) < c * c)) return false;
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
	    e=null;
	    g.o();
	    d.a(PathType.WATER, i);
	}
  
	public void e() {
		d.getControllerLook().a(e, 10.0F, d.N());
		if (--h<=0) {
			h=10;
			if ((!g.a(e, f)) 
					&& (!d.isLeashed()) && (!d.isPassenger())
					&& (d.h(e) >= 144.0D)) {
				int j = MathHelper.floor(e.locX) - 2;
				int k = MathHelper.floor(e.locZ) - 2;
				int m = MathHelper.floor(e.getBoundingBox().b);
				
			    for (int n = 0; n <= 4; n++) {
			    	for (int i1 = 0; i1 <= 4; i1++) {
			    		if ((n < 1) || (i1 < 1) || (n > 3) || (i1 > 3)) {
			    			if ((a.getType(new BlockPosition(j + n, m - 1, k + i1)).q()) && (a(new BlockPosition(j + n, m, k + i1))) && (a(new BlockPosition(j + n, m + 1, k + i1)))) {
			    				d.setPositionRotation(j + n + 0.5F, m, k + i1 + 0.5F, d.yaw, d.pitch);
			    				g.o();
			    				return;
			    			}
			    		}
			       	}
			    }
			}
		}
	}
  
	protected boolean a(BlockPosition paramBlockPosition) {
		IBlockData localIBlockData = a.getType(paramBlockPosition);
		if (localIBlockData.getMaterial() == net.minecraft.server.v1_11_R1.Material.AIR) {
			return true;
		}
		return !localIBlockData.h();
	}
}
