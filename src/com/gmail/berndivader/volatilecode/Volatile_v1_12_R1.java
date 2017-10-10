package com.gmail.berndivader.volatilecode;

import java.lang.reflect.Field;
import java.util.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.NMS.NMSUtil;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class Volatile_v1_12_R1 
implements VolatileHandler {
	
	public Volatile_v1_12_R1() {
	}

	@Override
	public void setMotion(Entity entity) {
	}

	@Override
	public void changeHitBox(Entity entity, double a0, double a1, double a2) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		me.getBoundingBox().a(a0,a1,a2);
	}

	@Override
	public void setItemMotion(Item i, Location ol, Location nl) {
		EntityItem ei=(EntityItem)((CraftItem)i).getHandle();
		ei.setPosition(ol.getX(), ol.getY(), ol.getZ());
	}
	
	@Override
	public void sendArmorstandEquipPacket(ArmorStand entity) {
		PacketPlayOutEntityEquipment packet=new PacketPlayOutEntityEquipment(entity.getEntityId(), EnumItemSlot.CHEST, new ItemStack(Blocks.DIAMOND_BLOCK, 1));
		Collection<AbstractPlayer> players=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),256);
		players.stream().forEach(ap-> {
			CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
			cp.getHandle().playerConnection.sendPacket(packet);
		});
	}

	@Override
	public void teleportEntityPacket(Entity entity) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(me);
		Collection<AbstractPlayer> players=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),256);
		players.forEach(ap-> {
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
	        case "followentity": {
	        	UUID uuid=null;
	        	if (e instanceof EntityCreature) {
	        		double speed=1.0d;
	        		float aR=2.0F;
	        		float zR=10.0F;
	        		String[]p=data.split(",");
	        		for (int a=0;a<p.length;a++) {
	        			if (CustomSkillStuff.isNumeric(p[a])) {
	        				if (a==0) {
	        					speed=Double.parseDouble(p[a]);
	        				} else if (a==1) {
	        					aR=Float.parseFloat(p[a]);
	        				} else if (a==2) {
	        					zR=Float.parseFloat(p[a]);
	        				}
	        			}
	        		}
	        		if (CustomSkillStuff.isNumeric(data)) {
	        			speed = Double.parseDouble(data);
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
	        case "returnhome": {
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	        		double x=e.locX;
	        		double y=e.locY;
	        		double z=e.locZ;
	        		double mR=10.0D;
	        		double tR=512.0D;
	        		boolean iT=false;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		speed = Double.parseDouble(data);
	            	}
	            	if (data1!=null) {
		        		String[]p=data1.split(",");
		        		for (int a=0;a<p.length;a++) {
		        			if (CustomSkillStuff.isNumeric(p[a])) {
		        				if (a==0) {
		        					x=Double.parseDouble(p[a]);
		        				} else if (a==1) {
		        					y=Double.parseDouble(p[a]);
		        				} else if (a==2) {
		        					z=Double.parseDouble(p[a]);
		        				} else if (a==3) {
		        					mR=Double.parseDouble(p[a]);
		        				} else if (a==4) {
		        					tR=Double.parseDouble(p[a]);
		        				}
		        			} else {
		        				if (a==5) {
		        					iT=Boolean.parseBoolean(p[a].toUpperCase());
		        				}
		        			}
		        		}
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalReturnHome(e,speed,x,y,z,mR,tR,iT));
	        	}
	        }
	        default: {
	        	List<String>gList=new ArrayList<String>();
	        	gList.add(uGoal);
	            Main.mythicmobs.getVolatileCodeHandler().aiGoalSelectorHandler(entity, gList);
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
	
	public class PathfinderGoalFollowEntity extends PathfinderGoal {
		private final EntityInsentient d;
		private final EntityLiving d1;
		private EntityLiving e;
		net.minecraft.server.v1_12_R1.World a;
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
			if ((!(entity.getNavigation() instanceof Navigation)) && (!(entity.getNavigation() instanceof NavigationFlying))) {
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
			return (!g.o()) && (d.h(e) > b * b);
		}
	  
		public void c() {
			h = 0;
			i = d.a(PathType.WATER);
			d.a(PathType.WATER, 0.0F);
		}
	  
		public void d() {
			e = null;
			g.p();
			d.a(PathType.WATER, i);
		}
	  
		public void e() {
			d.getControllerLook().a(e, 10.0F, d.N());
			if (--h<=0) {
				h=10;
				if ((!g.a(e, f)) 
						&& (!d.isLeashed()) && (!d.isPassenger())
						&& (d.h(e) >= 144.0D)) {
					int i = MathHelper.floor(e.locX) - 2;
					int j = MathHelper.floor(e.locZ) - 2;
					int k = MathHelper.floor(e.getBoundingBox().b);
					for (int l = 0; l <= 4; l++) {
						for (int i1 = 0; i1 <= 4; i1++) {
							if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3)) && (a(i, j, k, l, i1))) {
								CraftEntity entity = d.getBukkitEntity();
								Location to = new Location(entity.getWorld(), i + l + 0.5F, k, j + i1 + 0.5F, d.yaw, d.pitch);
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
}
