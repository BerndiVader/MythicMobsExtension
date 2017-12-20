package com.gmail.berndivader.volatilecode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.NMS.NMSUtil;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftSnowman;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftItem;

import net.minecraft.server.v1_11_R1.Vec3D;
import net.minecraft.server.v1_11_R1.CommandException;
import net.minecraft.server.v1_11_R1.CommandTestFor;
import net.minecraft.server.v1_11_R1.GameProfileSerializer;
import net.minecraft.server.v1_11_R1.MojangsonParseException;
import net.minecraft.server.v1_11_R1.MojangsonParser;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_11_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_11_R1.IRangedEntity;
import net.minecraft.server.v1_11_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_11_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_11_R1.PacketPlayOutCamera;
import net.minecraft.server.v1_11_R1.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_11_R1.EntityItem;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.Navigation;
import net.minecraft.server.v1_11_R1.NavigationAbstract;
import net.minecraft.server.v1_11_R1.PathType;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.PathfinderGoal;
import net.minecraft.server.v1_11_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_11_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.PathEntity;
import net.minecraft.server.v1_11_R1.PathPoint;

public class Volatile_v1_11_R1 
implements VolatileHandler {
	private static Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>sSet=new HashSet<>(Arrays.asList(
			new EnumPlayerTeleportFlags[] { 
					EnumPlayerTeleportFlags.X_ROT,
					EnumPlayerTeleportFlags.Y_ROT,
					}));	
	private static Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>ssSet=new HashSet<>(Arrays.asList(
			new EnumPlayerTeleportFlags[] { 
					EnumPlayerTeleportFlags.X_ROT,
					EnumPlayerTeleportFlags.Y_ROT,
					EnumPlayerTeleportFlags.X,
					EnumPlayerTeleportFlags.Y,
					EnumPlayerTeleportFlags.Z
					}));	
	private static Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>sssSet=new HashSet<>(Arrays.asList(
			new EnumPlayerTeleportFlags[] { 
					EnumPlayerTeleportFlags.X,
					EnumPlayerTeleportFlags.Y,
					EnumPlayerTeleportFlags.Z
					}));	
	
	public Volatile_v1_11_R1() {
	}

	@Override
	public void forceSetPositionRotation(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_11_R1.Entity me = ((CraftEntity)entity).getHandle();
        me.setLocation(x,y,z,yaw,pitch);
        if (entity instanceof Player) playerConnectionTeleport(entity,x,y,z,yaw,pitch,f,g);
        me.world.entityJoinedWorld(me, false);
	}
	
	private void playerConnectionTeleport(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
		Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>set=new HashSet<>();
		if (f) {
			set=sSet;
			yaw=0.0F;pitch=0.0F;
		}
		if (g) {
			set.add(EnumPlayerTeleportFlags.Y);
			y=0.0D;
		}
		me.playerConnection.sendPacket(new PacketPlayOutPosition(x,y,z,yaw,pitch,set,0));
	}
	
	@Override
	public void playerConnectionLookAt(Entity entity,float yaw,float pitch) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,yaw,pitch,sssSet,0));
	}
	
	@Override
	public void playerConnectionSpin(Entity entity,float s) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,s,0,ssSet,0));
	}
	
	@Override
	public void changeHitBox(Entity entity, double a0, double a1, double a2) {
		net.minecraft.server.v1_11_R1.Entity me = ((CraftEntity)entity).getHandle();
		me.getBoundingBox().a(a0,a1,a2);
	}
	
	@Override
	public void setItemMotion(Item i, Location ol, Location nl) {
		EntityItem ei=(EntityItem)((CraftItem)i).getHandle();
		ei.setPosition(ol.getX(), ol.getY(), ol.getZ());
	}

	@Override
	public void teleportEntityPacket(Entity entity) {
		net.minecraft.server.v1_11_R1.Entity me = ((CraftEntity)entity).getHandle();
		PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(me);
		Collection<AbstractPlayer> players=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192);
		players.stream().forEach(ap-> {
			CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
			cp.getHandle().playerConnection.sendPacket(tp);
		});
	}

	@Override
	public boolean inMotion(LivingEntity entity) {
		EntityInsentient e = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
		if (e.lastX!=e.locX 
				|| e.lastY!=e.locY 
				|| e.lastZ!=e.locZ) return true;
        return false;
	}

	@Override
	public void aiPathfinderGoal(LivingEntity entity, String uGoal, LivingEntity target) {
		World w = entity.getWorld();
        EntityInsentient e = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
        EntityLiving tE = null;
        if (target!=null) {
        	tE = (EntityLiving)((CraftLivingEntity)entity).getHandle();
        }
        Field goalsField;
        int i=0;
        String goal=uGoal;
        String data=null;
        String data1=null;
        String[] parse = uGoal.split(" ");
        if (parse[0].matches("[0-9]*")) {
        	i = Integer.parseInt(parse[0]);
        	if (parse.length>1) {
        		goal = parse[1];
        		if (parse.length>2) {
        			data = parse[2];
        		}
        		if (parse.length>3) {
        			data1 = parse[3];
        		}
        	}
        }
		try {
			goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
	        goalsField.setAccessible(true);
	        PathfinderGoalSelector goals = (PathfinderGoalSelector)goalsField.get((Object)e);
	        switch (goal) {
	        case "rangedmelee": {
	            if (e instanceof EntityCreature) {
	            	float range = 2.0f;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		range = Float.parseFloat(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalMeleeRangeAttack((EntityCreature)e, 1.0, true, range));
	            }
	        	break;
	        }
	        case "runfromsun": {
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		speed = Double.parseDouble(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalFleeSun((EntityCreature)e, speed));
	        	}
	        	break;
	        }
	        case "shootattack": {
	        	if (e instanceof EntityInsentient) {
	            	goals.a(i,new PathFinderGoalShoot((EntityInsentient)e,1.0,20,60,15.0f));
	        	}
	        	break;
	        }
	        case "followentity": {
	        	UUID uuid=null;
	        	if (e instanceof EntityCreature) {
	        		double speed=1.0d;
	        		float aR=2.0F;
	        		float zR=10.0F;
	        		String[]p=data.split(",");
	        		for (int a=0;a<p.length;a++) {
        				if (a==0) {
        					speed=Double.parseDouble(p[a]);
        				} else if (a==1) {
        					aR=Float.parseFloat(p[a]);
        				} else if (a==2) {
        					zR=Float.parseFloat(p[a]);
        				}
	        		}
	        		if (data1!=null && (uuid = CustomSkillStuff.isUUID(data1))!=null) {
	        			Main.getPlugin().getNMSUtils();
						Entity ee = NMSUtil.getEntity(w, uuid);
	        			if (ee instanceof LivingEntity) {
	        		        tE = (EntityLiving)((CraftLivingEntity)(LivingEntity)ee).getHandle();
	        			}
	        		}
	        		if (tE!=null && tE.isAlive()) {
		            	goals.a(i, (PathfinderGoal)new PathfinderGoalFollowEntity(e,tE,speed,zR,aR));
	        		}
	        	}
	        	break;
	        }
	        case "breakblocks": {
	        	if (e instanceof EntityCreature) {
	        		int chance=50;
	        		if (data1!=null 
	        				&& CustomSkillStuff.isNumeric(data1)) chance=Integer.parseInt(data1);
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalBreakBlocks(e,data,chance));
	        	}
	        	break;
	        }
	        default: {
	        	List<String>gList=new ArrayList<String>();
	        	gList.add(uGoal);
	            MythicMobs.inst().getVolatileCodeHandler().aiGoalSelectorHandler(entity, gList);
	        }}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    
	public class PathfinderGoalMeleeRangeAttack extends PathfinderGoalMeleeAttack {
		protected float range;

		public PathfinderGoalMeleeRangeAttack(EntityCreature entityCreature, double d, boolean b, float range) {
			super(entityCreature, d, b);
			this.range=range;
		}

		@Override
		protected double a(EntityLiving entity) {
		    return (double)(this.b.width * this.range * this.b.width * this.range + entity.width);
		}
	}
	
	public class PathFinderGoalShoot extends PathfinderGoal {
	    private final EntityInsentient a;
	    private final double b;
	    private int c,h1;
	    private final float d,i1;
	    private int d1 = -1;
	    private int f;
	    private boolean g;
	    private boolean h;
	    private int i = -1;

	    public PathFinderGoalShoot(EntityInsentient t, double d2, int n,int n1, float f2) {
	        this.a=t;
	        this.b=d2;
	        this.c=n;
	        this.h1=n1;
	        this.d = f2 * f2;
	        this.i1=f2;
	        this.a(3);
	    }

	    public void b(int n) {
	        this.c = n;
	    }

	    @Override
	    public boolean a() {
	        if (this.a.getGoalTarget() == null) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public boolean b() {
	        return (this.a() || !this.a.getNavigation().n());
	    }

	    @Override
	    public void c() {
	        super.c();
	    }

	    @Override
	    public void d() {
	        super.d();
	        this.f = 0;
	        this.d1 = -1;
	        this.a.cF();
	    }

	    @Override
	    public void e() {
	        boolean bl;
	        EntityLiving entityLiving = this.a.getGoalTarget();
	        if (entityLiving == null) {
	            return;
	        }
	        double d2 = this.a.d(entityLiving.locX, entityLiving.getBoundingBox().b, entityLiving.locZ);
	        boolean bl2 = this.a.getEntitySenses().a(entityLiving);
	        boolean bl3 = bl = this.f > 0;
	        if (bl2 != bl) {
	            this.f = 0;
	        }
	        this.f = bl2 ? ++this.f : --this.f;
	        if (d2 > (double)this.d || this.f < 20) {
	            this.a.getNavigation().a(entityLiving, this.b);
	            this.i = -1;
	        } else {
	            this.a.getNavigation().o();
	            ++this.i;
	        }
	        if (this.i >= 20) {
	            if ((double)this.a.getRandom().nextFloat() < 0.3) {
	                boolean bl4 = this.g = !this.g;
	            }
	            if ((double)this.a.getRandom().nextFloat() < 0.3) {
	                this.h = !this.h;
	            }
	            this.i = 0;
	        }
	        if (this.i > -1) {
	            if (d2 > (double)(this.d * 0.75f)) {
	                this.h = false;
	            } else if (d2 < (double)(this.d * 0.25f)) {
	                this.h = true;
	            }
	            this.a.getControllerMove().a(this.h ? -0.5f : 0.5f, this.g ? 0.5f : -0.5f);
	            this.a.a(entityLiving, 30.0f, 30.0f);
	        } else {
	            this.a.getControllerLook().a(entityLiving, 30.0f, 30.0f);
	        }
	        
	        if (--this.d1 == 0) {
	            float f2;
	            if (!bl2) {
	                return;
	            }
	            float f3 = f2 = MathHelper.sqrt(d2) / this.i1;
	            f3 = MathHelper.a(f3, 0.1f, 1.0f);
	            if (this.a instanceof IRangedEntity) {
		            ((IRangedEntity)this.a).a(entityLiving, f3);
	            } else {
	            	ActiveMob am=Main.mythicmobs.getMobManager().getMythicMobInstance(this.a.getBukkitEntity());
	            	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),"AISHOOT");
	            }
	            this.d1 = MathHelper.d(f2 * (float)(this.h1 - this.c) + (float)this.c);
	        } else if (this.d1 < 0) {
	            float f4 = MathHelper.sqrt(d2) / this.i1;
	            this.d1 = MathHelper.d(f4 * (float)(this.h1 - this.c) + (float)this.c);
	        }
	    }
	}
	
	public class PathfinderGoalReturnHome extends PathfinderGoal {
		private final EntityInsentient d;
		private final Vec3D v;
		private Optional<ActiveMob> mM;
		private Vec3D aV;
		private final double f;
		private final double mR,tR;
		net.minecraft.server.v1_11_R1.World a;
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
			if (!(entity.getNavigation() instanceof Navigation)) {
				throw new IllegalArgumentException("Unsupported mob type for ReturnHomeGoal");
			}
			this.mM=Main.mythicmobs.getMobManager().getActiveMob(entity.getUniqueID());
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
			return (!g.n()) && v.distanceSquared(this.aV)>2.0D;
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
			g.o();
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
					g.o();
					return;
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
	
	public class PathfinderGoalBreakBlocks extends PathfinderGoal {
		protected EntityInsentient entity;
		protected boolean isBreaking;
		protected int chance;
		protected HashSet<Material>materials;

		public PathfinderGoalBreakBlocks(EntityInsentient entity, String mL, int chance) {
			this.isBreaking=false;
			this.entity=entity;
			this.materials=new HashSet<>();
			this.chance=chance>100?100:chance<0?0:chance;
			if (mL!=null) {
				String[]parse=mL.toUpperCase().split(",");
				for(int a=0;a<parse.length;a++) {
					try {
						this.materials.add(Material.valueOf(parse[a]));
					} catch (Exception ex) {
						continue;
					}
				}
			}
		}

		public boolean a() {
			return this.entity.isAlive();
		}
		
		public boolean b() {
			if (this.entity.getGoalTarget()!=null
					&& this.entity.getGoalTarget().isAlive()) {
				return true;
			}
			return false;
		}

		public void e() {
			if (!this.canContinue()) return;
			EntityLiving target=this.entity.getGoalTarget();
			Block[] blocks=new Block[2];
            blocks[0] = this.getBreakableTargetBlock(target);
            blocks[1] = blocks[0].getRelative(BlockFace.UP);
            for (int a=0;a<blocks.length;a++) {
            	if (this.materials.isEmpty() 
           			|| this.materials.contains(blocks[a].getType())) this.attemptBreakBlock(blocks[a]);
            }
		}
		
		private boolean canContinue() {
			EntityLiving target = this.entity.getGoalTarget();
			if (target !=null
					&& target.isAlive()
					&& !this.isBreaking
					&& !this.isReachable(target)) {
				return true;
			}
			return false;
		}
		
	    private Block getBreakableTargetBlock(EntityLiving target) {
	        Location direction = target.getBukkitEntity().getLocation().subtract(this.entity.getBukkitEntity().getLocation());
	        double dx=direction.getX();
	        double dz=direction.getY();
	        int bdx=0;
	        int bdz=0;
	        if (Math.abs(dx)>Math.abs(dz)) {
	            bdx=(dx>0)?1:-1;
	        } else {
	            bdz=(dx>0)?1:-1;
	        }
	        return this.entity.world.getWorld().getBlockAt((int) Math.floor(this.entity.locX + bdx),
	        		(int) Math.floor(this.entity.locY),
	        		(int) Math.floor(this.entity.locZ + bdz));
	    }
	    
	    @SuppressWarnings("deprecation")
		private void attemptBreakBlock(Block block) {
	        Material type = block.getType();
	        if (!this.isBreaking
	        		&& type!=Material.AIR 
	        		&& type.isSolid()) {
	            Location location = block.getLocation();
	            if (Main.random.nextInt(100) < 50) {
                    this.entity.world.getWorld().playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
	            } else {
	            	this.isBreaking=true;
                    PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 4, false, false);
                    ((LivingEntity)this.entity.getBukkitEntity()).addPotionEffect(effect);
	            	new BukkitRunnable() {
						@Override
						public void run() {
			                EntityChangeBlockEvent event = new EntityChangeBlockEvent(PathfinderGoalBreakBlocks.this.entity.getBukkitEntity(), block, Material.AIR, (byte) 0);
			                Main.getPlugin().getServer().getPluginManager().callEvent(event);
			                if (!event.isCancelled()) {
			                    PathfinderGoalBreakBlocks.this.entity.world.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, Math.min(Main.random.nextFloat() + 0.2f, 1.0f), 1.0f);
		                        block.breakNaturally();
		                        PathfinderGoalBreakBlocks.this.isBreaking=false;
			                }
						}
					}.runTaskLaterAsynchronously(Main.getPlugin(), 20L);
	            }
	        }
	    }
	    private boolean isReachable(EntityLiving target) {
	    	if (this.entity.getEntitySenses().a(target)) return true;
	        PathEntity pe=this.entity.getNavigation().a(target);
	        if (pe==null) {
	            return false;
	        } else {
	            PathPoint pp=pe.c();
	            if (pp==null) {
	                return false;
	            } else {
	                int i=pp.a-MathHelper.floor(target.locX);
	                int j=pp.c-MathHelper.floor(target.locZ);
	                return (double)(i*i+j*j)<=2.25D;
	            }
	        }
	    }
	}

	@Override
	public void sendArmorstandEquipPacket(ArmorStand entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveEntityPacket(Entity entity,Location cl,double x,double y,double z) {
		net.minecraft.server.v1_11_R1.Entity me = ((CraftEntity)entity).getHandle();
		double x1=cl.getX()-me.locX;
		double y1=cl.getY()-me.locY;
		double z1=cl.getZ()-me.locZ;
		PacketPlayOutEntityVelocity vp = new PacketPlayOutEntityVelocity(me.getId(),x1,y1,z1);
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		while(it.hasNext()) {
			AbstractPlayer ap=it.next();
			CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
			cp.getHandle().playerConnection.sendPacket(vp);
		}
	}

	@Override
	public void rotateEntityPacket(Entity entity,float y, float p) {
		net.minecraft.server.v1_11_R1.Entity me = ((CraftEntity)entity).getHandle();
		byte ya=(byte)((int)(y*256.0F/360.0F));
		byte pa=(byte)((int)(p*256.0F/360.0F));
		PacketPlayOutEntityLook el=new PacketPlayOutEntityLook(me.getId(),ya,pa,me.onGround);
		PacketPlayOutEntityHeadRotation hr=new PacketPlayOutEntityHeadRotation(me, ya);
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		while(it.hasNext()) {
			AbstractPlayer ap=it.next();
			CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
			cp.getHandle().playerConnection.sendPacket(el);
			cp.getHandle().playerConnection.sendPacket(hr);
		}
	}

	@Override
	public void forceSpectate(Player player, Entity entity) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutCamera(((CraftEntity) entity).getHandle()));
		
	}

	@Override
	public void playEndScreenForPlayer(Player player,float f) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4,f));
	}

	@Override
	public boolean playerIsSleeping(Player p) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSleeping()||me.isDeeplySleeping();
	}

	@Override
	public boolean playerIsRunning(Player p) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSprinting();
	}

	@Override
	public boolean playerIsCrouching(Player p) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSneaking();
	}

	@Override
	public boolean playerIsJumping(Player p) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return !me.onGround&&CustomSkillStuff.round(me.motY,5)!=-0.00784;
	}

	@Override
	public void forceCancelEndScreenPlayer(Player player) {
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
		me.playerConnection.sendPacket(new PacketPlayOutCloseWindow(0));
	}

	@Override
	public void fakeEntityDeath(Entity entity, long d) {
		net.minecraft.server.v1_11_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
        me.world.broadcastEntityEffect(me,(byte)3);
		new BukkitRunnable() {
			@Override
			public void run() {
				PacketPlayOutEntityDestroy pd=new PacketPlayOutEntityDestroy(me.getId());
				PacketPlayOutSpawnEntityLiving ps=new PacketPlayOutSpawnEntityLiving(me);
				Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
				while(it.hasNext()) {
					AbstractPlayer ap=it.next();
					CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
					cp.getHandle().playerConnection.sendPacket(pd);
					cp.getHandle().playerConnection.sendPacket(ps);
				}
			}
		}.runTaskLater(Main.getPlugin(),d);
	}

	@Override
	public int arrowsOnEntity(Entity entity) {
		net.minecraft.server.v1_11_R1.EntityLiving me=((CraftLivingEntity)entity).getHandle();
	    DataWatcherObject<Integer>br=DataWatcher.a(EntityLiving.class,DataWatcherRegistry.b);
	    return me.getDataWatcher().get(br);
	}

	@Override
	public void removeArrowsFromEntity(Entity entity,int a,char c) {
		net.minecraft.server.v1_11_R1.EntityLiving me=((CraftLivingEntity)entity).getHandle();
	    DataWatcherObject<Integer>br=DataWatcher.a(EntityLiving.class,DataWatcherRegistry.b);
		switch(c) {
		case 'A':
			a+=arrowsOnEntity(entity);
			break;
		case 'S':
			a=arrowsOnEntity(entity)-a;
			if(a<0)a=0;
			break;
		case 'C':
			a=0;
			break;
		}
		me.getDataWatcher().set(br,a);
	}

	@Override
	public void removeSnowmanHead(Entity entity) {
		net.minecraft.server.v1_11_R1.EntitySnowman me = ((CraftSnowman)entity).getHandle();
		me.setHasPumpkin(false);
	}

	@Override
	public void setDeath(Player p, boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean testForCondition(Entity e, String command, char m) {
		boolean b=true;
		TestFor t=new TestFor();
		try {
			b=t.execute(e, new String[] {"dummy",command});
		} catch (CommandException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return b;
	}
	
	public class TestFor extends CommandTestFor {

		public TestFor() {
		}
		
	    public boolean execute(Entity e,String[] arrstring) throws CommandException {
	        net.minecraft.server.v1_11_R1.Entity entity=((CraftEntity)e).getHandle();
	        NBTTagCompound nBTTagCompound;
	        NBTTagCompound nBTTagCompound2=null;
            try {
                nBTTagCompound2=MojangsonParser.parse(CommandTestFor.a(arrstring,1));
            }
            catch (MojangsonParseException mojangsonParseException) {
            	return false;
            }
	        if (nBTTagCompound2!=null&&!GameProfileSerializer
	        		.a(nBTTagCompound2,nBTTagCompound=CommandTestFor.a(entity),true)) {
	            return false;
	        }
	        return true;
	    }
	}

	@Override
	public float getItemCoolDown(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setItemCooldown(Player p,int j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFieldOfViewPacketSend(Player player, float f1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getIndicatorPercentage(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBowTension(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}

}
