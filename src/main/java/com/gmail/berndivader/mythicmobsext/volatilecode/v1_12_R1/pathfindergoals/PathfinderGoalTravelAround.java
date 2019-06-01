package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import java.util.ArrayList;
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

public
class 
PathfinderGoalTravelAround 
extends
PathfinderGoal
{
	
	ArrayList<Vec3D>travelpoints;
	
	private final EntityInsentient d;
	private Vec3D v;
	private Optional<ActiveMob> mM;
	private Vec3D aV;
	private final double f;
	private final double mR,tR;
	net.minecraft.server.v1_12_R1.World a;
	private final NavigationAbstract g;
	private int h,travel_index;
	float b;
	float c;
	private float i;
	private boolean iF,iT,remove_travelpoint;
	
	public PathfinderGoalTravelAround(EntityInsentient entity,double d0,double mR,double tR,boolean iT) {
		this.d=entity;
		this.f=d0;
		this.a=entity.world;
		g=entity.getNavigation();
		this.travelpoints=new ArrayList<>();
		this.travel_index=0;
		this.v=nextCheckPoint();
		a(3);
		this.mR=mR;
		this.tR=tR;
		this.iF=false;
		this.iT=iT;
		this.remove_travelpoint=true;
		if ((!(entity.getNavigation() instanceof Navigation)) && (!(entity.getNavigation() instanceof NavigationFlying))) {
			throw new IllegalArgumentException("Unsupported mob type for TravelAroundGoal");
		}
		this.mM=Utils.mobmanager.getActiveMob(entity.getUniqueID());
	}

	@Override
	public boolean a() {
		this.aV=new Vec3D(d.locX,d.locY,d.locZ);
		if(this.v!=null) {
			if (this.iT||this.d.getGoalTarget()==null||!this.d.getGoalTarget().isAlive()) {
				double ds=v.distanceSquared(this.aV);
				if (ds>this.mR) {
					return true;
				} else if (this.iF&&ds>2.0D) {
					return true;
				} else {
					this.v=nextCheckPoint();
					if(this.iF) {
						if(this.mM.isPresent()) {
							ActiveMob am=this.mM.get();
							am.signalMob(null,v==null?Utils.signal_GOAL_TRAVELEND:Utils.signal_GOAL_TRAVELPOINT);
						}
						this.iF=false;
					}
				}
			}
		} else {
			this.v=nextCheckPoint();
			if(this.iF) {
				if(this.mM.isPresent()) {
					ActiveMob am=this.mM.get();
					am.signalMob(null,v==null?Utils.signal_GOAL_TRAVELEND:Utils.signal_GOAL_TRAVELPOINT);
				}
				this.iF=false;
			}
		}
		return false;
	}
	
	@Override
	public boolean b() {
		return (!g.o())&&v.distanceSquared(this.aV)>2.0D;
	}
	
	@Override
	public void c() {
		h=0;
		i=d.a(PathType.WATER);
		d.a(PathType.WATER,0.0F);
		if(!this.iF) {
			if(this.mM.isPresent()) {
				ActiveMob am=this.mM.get();
				am.signalMob(null,"GOAL_TRAVELSTART");
			}
		}
		this.iF=true;
	}
  
	@Override
	public void d() {
		g.p();
		d.a(PathType.WATER, i);
		if (v.distanceSquared(this.aV)<10.0D) {
			this.iF=false;
			this.v=null;
		}
	}
	
	@Override
	public void e() {
		d.getControllerLook().a(v.x,v.y,v.z,10.0F,d.N());
		if (h--<=0) {
			h=10;
			if (!g.a(v.x,v.y,v.z,f)&&(!d.isLeashed())&&(!d.isPassenger())&&v.distanceSquared(this.aV)>this.tR) {
				CraftEntity entity=d.getBukkitEntity();
				Location to=new Location(entity.getWorld(),v.x,v.y,v.z,d.yaw,d.pitch);
				EntityTeleportEvent event=new EntityTeleportEvent(entity,entity.getLocation(),to);
				d.world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) return;
				to=event.getTo();
				d.setPositionRotation(to.getX(),to.getY(),to.getZ(),to.getYaw(),to.getPitch());
				g.p();
				return;
			}
		}
	}
  
	protected boolean a(int i, int j, int k, int l, int i1) {
		BlockPosition blockposition=new BlockPosition(i+l,k-1,j+i1);
		IBlockData iblockdata=a.getType(blockposition);
		return (iblockdata.d(a,blockposition,EnumDirection.DOWN)==EnumBlockFaceShape.SOLID)&&(iblockdata.a(d))&&(a.isEmpty(blockposition.up()))&&(a.isEmpty(blockposition.up(2)));
	}
	
	protected Vec3D nextCheckPoint() {
		int size=this.travelpoints.size();
		Vec3D vector=null;
		if(size>0) {
			if(remove_travelpoint) {
				vector=travelpoints.get(size-1);
				travelpoints.remove(size-1);
			} else {
				if(travel_index==size) travel_index=0;
				vector=travelpoints.get(travel_index);
				travel_index++;
			}
		}
		return vector;
	}
	
	public void addTravelPoint(com.gmail.berndivader.mythicmobsext.utils.Vec3D vector,boolean remove) {
		this.remove_travelpoint=remove;
		travelpoints.add(new Vec3D(vector.getX(),vector.getY(),vector.getZ()));
	}
	
}
