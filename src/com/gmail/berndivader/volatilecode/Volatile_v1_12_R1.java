package com.gmail.berndivader.volatilecode;

import java.lang.reflect.Field;
import java.util.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_12_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSnowman;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers.NBT;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.NMS.NMSUtil;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class Volatile_v1_12_R1 
implements VolatileHandler {
	
	private static String signal_AISHOOT="AISHOOT";
	private static String signal_AIHIT="AIHIT";
	private static String meta_WALKSPEED="MMEXTWALKSPEED";
	
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
	public Volatile_v1_12_R1() {
		registerCustomParrot("mythic_parrot",105,MythicEntityParrot_1_12_R1.class);
	}
	
	private static void registerCustomParrot(String s1,int i1,Class<? extends net.minecraft.server.v1_12_R1.Entity>c1) {
		MinecraftKey k=new MinecraftKey(s1);
		EntityTypes.b.a(i1,k,c1);
		if (!EntityTypes.d.contains(k)) EntityTypes.d.add(k);
	}
	
	@Override
	public Parrot spawnCustomParrot(Location l1, boolean b1) {
		net.minecraft.server.v1_12_R1.World world=((CraftWorld)l1.getWorld()).getHandle();
		final MythicEntityParrot_1_12_R1 mep=new MythicEntityParrot_1_12_R1(world);
		mep.cd(b1);
		mep.setLocation(l1.getX(),l1.getY(),l1.getZ(),l1.getYaw(),l1.getPitch());
        world.addEntity(mep,SpawnReason.CUSTOM);
        return (Parrot)mep.getBukkitEntity();
	}
	
	@SuppressWarnings("rawtypes")
	private void sendPlayerPacketsAsync(Iterator<AbstractPlayer>it,List<Packet>pk) {
		new BukkitRunnable() {
			@Override
			public void run() {
				while(it.hasNext()) {
					AbstractPlayer ap=it.next();
					CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
					for(Packet p:pk) {
						cp.getHandle().playerConnection.sendPacket(p);
					}
				}
			}
		}.runTaskAsynchronously(Main.getPlugin());
	}
	
	@SuppressWarnings("rawtypes")
	private void sendPlayerPacketsSync(Iterator<AbstractPlayer>it,List<Packet>pk) {
		new BukkitRunnable() {
			@Override
			public void run() {
				while(it.hasNext()) {
					AbstractPlayer ap=it.next();
					CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
					for(Packet p:pk) {
						cp.getHandle().playerConnection.sendPacket(p);
					}
				}
			}
		}.runTask(Main.getPlugin());
	}
	
	@Override
    public void setFieldOfViewPacketSend(Player player, float f1) {
		net.minecraft.server.v1_12_R1.EntityPlayer me=((CraftPlayer)player).getHandle();
		PlayerAbilities arg1=(PlayerAbilities)Utils.cloneObject(me.abilities);
		arg1.walkSpeed=f1;
		me.playerConnection.sendPacket(new PacketPlayOutAbilities(arg1));
    }
	
	@Override
	public void removeSnowmanHead(Entity entity) {
		net.minecraft.server.v1_12_R1.EntitySnowman me = ((CraftSnowman)entity).getHandle();
		me.setHasPumpkin(false);
	}
	
	@Override
	public int arrowsOnEntity(Entity entity) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
		return me.getArrowCount();
	}
	
	@Override
	public void modifyArrowsAtEntity(Entity entity, int a, String c) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
		switch(c) {
		case 'ADD':
			a+=me.getArrowCount();
			break;
		case 'SUBSTRACT':
			a=me.getArrowCount()-a;
			if(a<0)a=0;
			break;
		case 'CLEAR':
			a=0;
			break;
		}
		me.setArrowCount(a);
	}
	
	@Override
    public void forceSpectate(Player player, Entity entity) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutCamera(((CraftEntity) entity).getHandle()));
    }
	
    public void forceEntitySitting(Entity entity) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
		net.minecraft.server.v1_12_R1.EntityArrow a=new EntityArrow(me.getWorld()) {
			@Override
			protected ItemStack j() {
				return null;
			}
		};
    }
    
	@Override
    public void playEndScreenForPlayer(Player player, float f) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4,f));
	}
	
	@Override
	public void fakeEntityDeath(Entity entity,long d) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
        me.world.broadcastEntityEffect(me,(byte)3);
		PacketPlayOutEntityDestroy pd=new PacketPlayOutEntityDestroy(me.getId());
		PacketPlayOutSpawnEntityLiving ps=new PacketPlayOutSpawnEntityLiving(me);
		new BukkitRunnable() {
			@Override
			public void run() {
				sendPlayerPacketsAsync(Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator()
						,Arrays.asList(new Packet[]{pd,ps}));
			}
		}.runTaskLaterAsynchronously(Main.getPlugin(),d);
	}
	
	@Override
    public void forceCancelEndScreenPlayer(Player player) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
		me.playerConnection.sendPacket(new PacketPlayOutCloseWindow(0));
   }
	
	@Override
	public void forceSetPositionRotation(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
        me.setLocation(x,y,z,yaw,pitch);
        if (entity instanceof Player) {
        	playerConnectionTeleport(entity,x,y,z,yaw,pitch,f,g);
        }
        me.world.entityJoinedWorld(me, false);
	}
	
	private void playerConnectionTeleport(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
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
	public void rotateEntityPacket(Entity entity,float y, float p) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		byte ya=(byte)((int)(y*256.0F/360.0F));
		byte pa=(byte)((int)(p*256.0F/360.0F));
		PacketPlayOutEntityLook el=new PacketPlayOutEntityLook(me.getId(),ya,pa,me.onGround);
		PacketPlayOutEntityHeadRotation hr=new PacketPlayOutEntityHeadRotation(me, ya);
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{el,hr}));
	}
	
	@Override
	public void playerConnectionLookAt(Entity entity,float yaw,float pitch) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,yaw,pitch,sssSet,0));
	}
	
	@Override
	public void playerConnectionSpin(Entity entity,float s) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,s,0,ssSet,0));
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
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{packet}));
	}

	@Override
	public void teleportEntityPacket(Entity entity) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(me);
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{tp}));
	}
	
	@Override
	public void moveEntityPacket(Entity entity,Location cl,double x,double y,double z) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		double x1=cl.getX()-me.locX;
		double y1=cl.getY()-me.locY;
		double z1=cl.getZ()-me.locZ;
		PacketPlayOutEntityVelocity vp = new PacketPlayOutEntityVelocity(me.getId(),x1,y1,z1);
		Iterator<AbstractPlayer> it=Main.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{vp}));
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
	            	if (data!=null) {
	            		range = Float.parseFloat(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalMeleeRangeAttack((EntityCreature)e,1.0,true,range));
	            }
	        	break;
	        }
	        case "attack": {
	            if (e instanceof EntityCreature) {
	        		double s=1.0d;
	        		float r=2.0f;
	            	if (data!=null) s=Double.parseDouble(data);
	            	if (data1!=null) r=Float.parseFloat(data1);
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalAttack((EntityCreature)e,s,true,r));
	            }
	        	break;
	        }
	        case "runfromsun": {
	        	if (e instanceof EntityCreature) {
	        		double s=1.0d;
	            	if (data!=null) s=Double.parseDouble(data);
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalFleeSun((EntityCreature)e,s));
	        	}
	        	break;
	        }
	        case "shootattack": {
	        	if (e instanceof EntityInsentient) {
	        		double d1=1.0d;
	        		int i1=20,i2=60;
	        		float f1=15.0f;
	        		if (data!=null) {
		        		String[]p=data.split(",");
		        		for (int a=0;a<p.length;a++) {
		        			switch(a) {
		        			case 0:
	        					d1=Double.parseDouble(p[a]);
		        				break;
		        			case 1:
	        					i1=Integer.parseInt(p[a]);
		        				break;
		        			case 2:
	        					i2=Integer.parseInt(p[a]);
		        				break;
		        			case 3:
	        					f1=Float.parseFloat(p[a]);
		        				break;
		        			}
		        		}
	        		}
	            	goals.a(i,new PathFinderGoalShoot((EntityInsentient)e,d1,i1,i2,f1));
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
	        			switch(a) {
	        			case 0:
        					speed=Double.parseDouble(p[a]);
        					break;
	        			case 1:
        					aR=Float.parseFloat(p[a]);
        					break;
	        			case 2:
        					zR=Float.parseFloat(p[a]);
        					break;
	        			}
	        		}
	        		if (data1!=null && (uuid = Utils.isUUID(data1))!=null) {
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
	        	System.err.println("hoho");
	        	if (e instanceof EntityCreature) {
	        		int chance=50;
	        		if (data1!=null 
	        				&& Utils.isNumeric(data1)) chance=Integer.parseInt(data1);
		        	System.err.println("hoho1");
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
	            	if (data!=null) {
	            		speed = Double.parseDouble(data);
	            	}
	            	if (data1!=null) {
		        		String[]p=data1.split(",");
		        		for (int a=0;a<p.length;a++) {
		        			if (Utils.isNumeric(p[a])) {
		        				switch(a) {
		        				case 0:
		        					x=Double.parseDouble(p[a]);
		        					break;
		        				case 1:
		        					y=Double.parseDouble(p[a]);
		        					break;
		        				case 2:
		        					z=Double.parseDouble(p[a]);
		        					break;
		        				case 3:
		        					mR=Double.parseDouble(p[a]);
		        					break;
		        				case 4:
		        					tR=Double.parseDouble(p[a]);
		        					break;
		        				}
		        			} else if (a==5) {
		        				iT=Boolean.parseBoolean(p[a].toUpperCase());
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
	
	public class PathfinderGoalAttack
	extends PathfinderGoalMeleeAttack {
		protected float r;
		
	    public PathfinderGoalAttack(EntityCreature e,double d,boolean b,float r) {
			super(e,d,b);
			this.r=r;
		}
	    
	    @Override
	    protected void a(EntityLiving entityLiving,double d2) {
	        double d3 = this.a(entityLiving);
	        if (d2 <= d3 && this.c <= 0) {
	            this.c = 20;
	            this.b.a(EnumHand.MAIN_HAND);
	            this.b.B(entityLiving);
            	ActiveMob am=Main.mythicmobs.getMobManager().getMythicMobInstance(this.b.getBukkitEntity());
            	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),signal_AIHIT);
	        }
	    }
	    
		@Override
		protected double a(EntityLiving e) {
		    return (double)(this.b.width*this.r*this.b.width*this.r+e.width);
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
	        return (this.a() || !this.a.getNavigation().o());
	    }

	    @Override
	    public void c() {
	        super.c();
	        if (this.a instanceof IRangedEntity) ((IRangedEntity)this.a).p(true);
	    }

	    @Override
	    public void d() {
	        super.d();
	        if (this.a instanceof IRangedEntity) ((IRangedEntity)this.a).p(false);
	        this.f = 0;
	        this.d1 = -1;
	        this.a.cN();
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
	        bl = this.f > 0;
	        if (bl2 != bl) {
	            this.f = 0;
	        }
	        this.f = bl2 ? ++this.f : --this.f;
	        if (d2 > (double)this.d || this.f < 20) {
	            this.a.getNavigation().a(entityLiving, this.b);
	            this.i = -1;
	        } else {
	            this.a.getNavigation().p();
	            ++this.i;
	        }
	        if (this.i >= 20) {
	            if ((double)this.a.getRandom().nextFloat() < 0.3) {
	                this.g = !this.g;
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
	            	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),signal_AISHOOT);
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
			if (h--<=0) {
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
	
	public boolean setEntityData(Entity e, String ns) {
		net.minecraft.server.v1_12_R1.Entity me=((CraftEntity)e).getHandle();
		
		return true;
	}
	
	@Override
	public boolean getNBTValueOf(Entity e1, String s1,boolean b1) {
		return getNBTValue(e1,s1);
	}
	
	private boolean getNBTValue(Entity e1, String s) {
		net.minecraft.server.v1_12_R1.Entity me=((CraftEntity)e1).getHandle();
		NBTTagCompound nbt1=null,nbt2=null;
		boolean bl1=false;
		if ((nbt1=TFa(me))!=null) {
			try {
				nbt2=MojangsonParser.parse(s);
			}
			catch (MojangsonParseException ex) {
				System.err.println(ex.getLocalizedMessage());
				return false;
			}
			NBTBase nb1=null,nb2=null;
			for(String s2:nbt1.c()) {
				if (nbt2.hasKey(s2)) {
					nb1=nbt1.get(s2);
					nb2=nbt2.get(s2);
					break;
				}
			}
			if (nb1!=null&&nb2!=null) bl1=nbtA(nb2,nb1,bl1=true);
		}
		return bl1;
	}
	
	private boolean nbtA(NBTBase nb1,NBTBase nb2,boolean bl1) {
		if(!bl1) return bl1;
		switch(nb1.getTypeId()) {
		case NBT.TAG_COMPOUND:
            NBTTagCompound nbt1=(NBTTagCompound)nb1;
            NBTTagCompound nbt2=(NBTTagCompound)nb2;
            for (String s:nbt1.c()) {
            	System.err.println(s);
            	if (nbt2.hasKey(s)) {
	                NBTBase nb3=nbt1.get(s);
	                bl1=nbtA(nb3,nbt2.get(s),bl1=true);
            	}
            }
			break;
		case NBT.TAG_LIST:
            NBTTagList nbl1=(NBTTagList)nb1;
            NBTTagList nbl2=(NBTTagList)nb2;
            System.err.println("nbl1:"+nbl1);
            System.err.println("nbl2:"+nbl2);
            for(int i1=0;i1<nbl1.size();i1++) {
            	NBTBase nb3=nbl1.i(i1);
            	NBTBase nb4=nbl2.i(i1);
            	System.err.println("nb3:"+nb3);
            	System.err.println("nb4:"+nb4);
            	nbtA(nb3,nb4,bl1);
            }
			break;
		case NBT.TAG_BYTE:
		case NBT.TAG_SHORT:
		case NBT.TAG_LONG:
		case NBT.TAG_INT:
		case NBT.TAG_FLOAT:
		case NBT.TAG_DOUBLE:
			System.err.println("number");
			System.err.println(nb1.toString());
			System.err.println(nb2.toString());
			break;
		case NBT.TAG_BYTE_ARRAY:
		case NBT.TAG_INT_ARRAY:
			System.err.println("num_array");
			System.err.println(nb1.toString());
			System.err.println(nb2.toString());
			break;
		case NBT.TAG_STRING:
			System.err.println("string");
			System.err.println(nb1.toString());
			System.err.println(nb2.toString());
			break;
		}
		return bl1;
	}
	
	@Override
	public boolean testForCondition(Entity e, String ns,char m) {
		return testFor(e,ns,m);
	}
 	
	private boolean testFor(Entity e,String c,char m) {
		net.minecraft.server.v1_12_R1.Entity me=((CraftEntity)e).getHandle();
		NBTTagCompound nbt1=null,nbt2=null;
		try {
			nbt2=MojangsonParser.parse(c);
		}
		catch (MojangsonParseException ex) {
			System.err.println(ex.getLocalizedMessage());
			return false;
		}
		nbt1=TFa(me);
		if (nbt2!=null&&!GPa(nbt2,nbt1,true)) {
			return false;
		}
        return true;
	}

	private NBTTagCompound TFa(net.minecraft.server.v1_12_R1.Entity e) {
		ItemStack is;
		NBTTagCompound nbt=e.save(new NBTTagCompound());
		if (e instanceof EntityHuman&&!(is=((EntityHuman)e).inventory.getItemInHand()).isEmpty()) {
			nbt.set("SelectedItem",is.save(new NBTTagCompound()));
		}
		return nbt;
	}
	
    private boolean GPa(NBTBase b1, NBTBase b2,boolean lc) {
        if (b1==b2||b1==null) return true;
        if (b2==null||!b1.getClass().equals(b2.getClass())) return false;
        if (b1 instanceof NBTTagCompound) {
            NBTTagCompound nbt1=(NBTTagCompound)b1;
            NBTTagCompound nbt2=(NBTTagCompound)b2;
            for (String s:nbt1.c()) {
                NBTBase b3=nbt1.get(s);
                if (GPa(b3,nbt2.get(s),false)) {
                	continue;
                }
                return false;
            }
            return true;
        }
        if (b1 instanceof NBTTagList) {
            NBTTagList nbtl1=(NBTTagList)b1;
            NBTTagList nbtl2=(NBTTagList)b2;
            if (nbtl1.isEmpty()) return nbtl2.isEmpty();
            for (int j=0;j<nbtl1.size();j++) {
                NBTBase b4=nbtl1.i(j);
                boolean bl2 = false;
                for (int k=0;k<nbtl2.size();k++) {
                    if (!GPa(b4,nbtl2.i(k),false)) continue;
                    bl2=true;
                    break;
                }
                if(bl2) continue;
                return false;
            }
            return true;
        }
        return b1.equals(b2);
    }
	
	@Override
	public boolean playerIsSleeping(Player p) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSleeping()||me.isDeeplySleeping();
	}
	@Override
	public boolean playerIsRunning(Player p) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSprinting();
	}
	@Override
	public boolean playerIsCrouching(Player p) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSneaking();
	}
	@Override
	public boolean playerIsJumping(Player p) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return !me.onGround&&Utils.round(me.motY,5)!=-0.00784;
	}
	
	@Override
	public void setDeath(Player p, boolean b) {
		net.minecraft.server.v1_12_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		me.dead=b;
	}
	
	@Override
	public float getIndicatorPercentage(Player p) {
        EntityHuman eh=((CraftHumanEntity)p).getHandle();
        return eh.n(0.0f);
	}
	
	@Override
	public float getItemCoolDown(Player p) {
        EntityHuman eh=((CraftHumanEntity)p).getHandle();
        return eh.getCooldownTracker().a(eh.inventory.getItemInHand().getItem(),0.0f);
	}

	@Override
	public boolean setItemCooldown(Player p,int j) {
        EntityHuman eh=((CraftHumanEntity)p).getHandle();
        net.minecraft.server.v1_12_R1.Item i=eh.inventory.getItemInHand().getItem();
        if (eh.getCooldownTracker().cooldowns.containsKey(i)) {
        	eh.getCooldownTracker().cooldowns.remove(i);
        };
       	eh.getCooldownTracker().a(i,j);
		return true;
	}
}
