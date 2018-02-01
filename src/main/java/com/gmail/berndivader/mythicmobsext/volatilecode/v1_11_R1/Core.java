package main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.server.v1_11_R1.CommandException;
import net.minecraft.server.v1_11_R1.CommandTestFor;
import net.minecraft.server.v1_11_R1.GameProfileSerializer;
import net.minecraft.server.v1_11_R1.MojangsonParseException;
import net.minecraft.server.v1_11_R1.MojangsonParser;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_11_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_11_R1.PacketPlayOutAbilities;
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
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.PathfinderGoal;
import net.minecraft.server.v1_11_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftSnowman;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftItem;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.gmail.berndivader.mythicmobsext.NMS.NMSUtil;
import main.java.com.gmail.berndivader.mythicmobsext.Main;
import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;
import main.java.com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1.pathfindergoals.PathFinderGoalShoot;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1.pathfindergoals.PathfinderGoalBreakBlocks;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1.pathfindergoals.PathfinderGoalFollowEntity;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1.pathfindergoals.PathfinderGoalJumpOffFromVehicle;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_11_R1.pathfindergoals.PathfinderGoalMeleeRangeAttack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;

public class Core 
implements Handler {
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
	
	public Core() {
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
		Collection<AbstractPlayer> players=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192);
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
	            	if (Utils.isNumeric(data)) {
	            		range = Float.parseFloat(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalMeleeRangeAttack((EntityCreature)e, 1.0, true, range));
	            }
	        	break;
	        }
	        case "runfromsun": {
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	            	if (Utils.isNumeric(data)) {
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
	        		if (data1!=null && (uuid = Utils.isUUID(data1))!=null) {
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
	        				&& Utils.isNumeric(data1)) chance=Integer.parseInt(data1);
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalBreakBlocks(e,data,chance));
	        	}
	        	break;
	        }
	        case "jumpoffvehicle": {
	        	if (e instanceof EntityCreature) {
	            	goals.a(i,(PathfinderGoal)new PathfinderGoalJumpOffFromVehicle(e));
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
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
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
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
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
		return !me.onGround&&Utils.round(me.motY,5)!=-0.00784;
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
				Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
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
	public void modifyArrowsAtEntity(Entity entity,int a, char c) {
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
		net.minecraft.server.v1_11_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		me.dead=b;
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
		net.minecraft.server.v1_11_R1.EntityPlayer me=((CraftPlayer)player).getHandle();
		net.minecraft.server.v1_11_R1.PlayerAbilities arg=new net.minecraft.server.v1_11_R1.PlayerAbilities();
		arg.walkSpeed=f1;
		me.playerConnection.sendPacket(new PacketPlayOutAbilities(arg));
    }

	@Override
	public float getIndicatorPercentage(Player p) {
        EntityHuman eh=((CraftHumanEntity)p).getHandle();
        return eh.o(0.0f);
	}

	@Override
	public Parrot spawnCustomParrot(Location l1, boolean b1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getNBTValueOf(Entity e1, String s1,boolean b1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void moveto(LivingEntity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWBWB(Player p, boolean bl1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addNBTTag(Entity e1, String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vec3D lastPosEntity(Entity bukkitEntity) {
		// TODO Auto-generated method stub
		return null;
	}
}
