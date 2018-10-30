package com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntitySnowman;
import net.minecraft.server.v1_10_R1.PathfinderGoal;
import net.minecraft.server.v1_10_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.CommandException;
import net.minecraft.server.v1_10_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_10_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying;
import net.minecraft.server.v1_10_R1.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.WorldBorder;
import net.minecraft.server.v1_10_R1.PacketPlayOutAbilities;
import net.minecraft.server.v1_10_R1.CommandTestFor;
import net.minecraft.server.v1_10_R1.GameProfileSerializer;
import net.minecraft.server.v1_10_R1.MojangsonParseException;
import net.minecraft.server.v1_10_R1.MojangsonParser;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.EnumItemSlot;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_10_R1.PacketPlayOutCamera;
import net.minecraft.server.v1_10_R1.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_10_R1.EntityItem;
import net.minecraft.server.v1_10_R1.EntityHuman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSnowman;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtil;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.GetLastDamageIndicatorCondition;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals.PathFinderGoalShoot;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals.PathfinderGoalBreakBlocks;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals.PathfinderGoalFollowEntity;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals.PathfinderGoalJumpOffFromVehicle;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals.PathfinderGoalMeleeRangeAttack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;

public class Core 
implements
Handler,
Listener {
	
	private static WorldBorder wb;
	
	private static HashMap<UUID,ChannelHandler>chl;
	private static Field cField;	
	
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
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}
	
	static {
        wb=new WorldBorder();
        wb.world=MinecraftServer.getServer().getWorldServer(0);
        wb.setCenter(999999,999999);
        wb.setSize(1);
        wb.setWarningDistance(1);
	    for(Field f:NetworkManager.class.getDeclaredFields()) {
	    	if(f.getType().isAssignableFrom(Channel.class)) {
	    		cField=f;
	    		cField.setAccessible(true);
	    		break;
	    	}
	    }
	    chl=new HashMap<>();
	}
	
	interface PacketReceivingHandler {
	    void handle(Player p,PacketPlayInArmAnimation packet);
	    void handle(Player p, PacketPlayInBlockDig packet);
		void handle(Player p,PacketPlayInFlying packet);
	    void handle(Player p,PacketPlayInSteerVehicle packet);
	}
	
	@EventHandler
	public void onJoinRegisterChannelListener(PlayerJoinEvent e) {
		Utils.pl.put(e.getPlayer().getUniqueId(),new com.gmail.berndivader.mythicmobsext.utils.Vec3D(0d,0d,0d));
	    chl.put(e.getPlayer().getUniqueId(),channelPlayerInProzess(e.getPlayer(), new PacketReceivingHandler() {
	    	@Override
	        public void handle(Player p, PacketPlayInArmAnimation packet) {
	        	float f1=getIndicatorPercentage(p);
	        	p.setMetadata(GetLastDamageIndicatorCondition.meta_LASTDAMAGEINDICATOR,new FixedMetadataValue(Main.getPlugin(),f1));;
	            return;
	        }
			@Override
			public void handle(Player p,PacketPlayInFlying packet) {
				net.minecraft.server.v1_10_R1.EntityPlayer me=((CraftPlayer)p).getHandle();
				com.gmail.berndivader.mythicmobsext.utils.Vec3D v3=new com.gmail.berndivader.mythicmobsext.utils.Vec3D(me.locX,me.locY,me.locZ);
				double dx=packet.a(me.locX),dy=packet.b(me.locY),dz=packet.c(me.locZ);
				v3=(v3.getX()!=dx||v3.getY()!=dy||v3.getZ()!=dz)
						?v3.length(new com.gmail.berndivader.mythicmobsext.utils.Vec3D(dx,dy,dz))
						:new com.gmail.berndivader.mythicmobsext.utils.Vec3D(0,0,0);
				Utils.pl.get(p.getUniqueId()).set(v3.getX(),v3.getY(),v3.getZ());
				return;
			}
			@Override
			public void handle(Player p, PacketPlayInSteerVehicle packet) {
				return;
			}
			@Override
			public void handle(Player p, PacketPlayInBlockDig packet) {
				p.setMetadata(Utils.meta_MMEDIGGING,new FixedMetadataValue(Main.getPlugin(),packet.c().name()));
				return;
			}
	    }));
	}

	@EventHandler
	public void onLeaveUnregisterChannelListener(PlayerQuitEvent e) {
		ChannelHandler ch=chl.get(e.getPlayer().getUniqueId());
		if (ch!=null) closeChannelListener(e.getPlayer(),ch);
		Utils.pl.remove(e.getPlayer().getUniqueId());
		chl.remove(e.getPlayer().getUniqueId());
	}
	
	private boolean closeChannelListener(Player p,ChannelHandler ch) {
	    try {
	        ChannelPipeline pipe=gnc(p).pipeline();
	        pipe.remove(ch);
	        return true;
	    } catch(Exception e) {
	        return false;
	    }
	}
	
	public ChannelHandler channelPlayerInProzess(final Player p, final PacketReceivingHandler prh) {
	    ChannelPipeline pipe=gnc(p).pipeline();
	    @SuppressWarnings("rawtypes")
		ChannelHandler ch=new MessageToMessageDecoder<Packet>() {
	    	@Override
			protected void decode(ChannelHandlerContext chc,Packet packet,List<Object>out) throws Exception {
	    		switch(packet.getClass().getSimpleName()) {
	    		case "PacketPlayInSteerVehicle":
	            	prh.handle(p,(PacketPlayInSteerVehicle)packet);
	            	break;
	    		case "PacketPlayInArmAnimation":
	                prh.handle(p,(PacketPlayInArmAnimation)packet);
	    			break;
	    		case "PacketPlayInPosition":
	    		case "PacketPlayInPositionLook":
	    		case "PacketPlayInLook":
	    			prh.handle(p,(PacketPlayInFlying)packet);
	    			break;
	    		case "PacketPlayInBlockDig2":
	    			prh.handle(p,(PacketPlayInBlockDig)packet);
	    			break;
	    		}
	    		out.add(packet);
			}
	    };
	    pipe.addAfter("decoder","listener",ch);
	    return ch;
	}	
	
	private Channel gnc(Player p) {
	    NetworkManager nm=((CraftPlayer)p).getHandle().playerConnection.networkManager;
	    Channel c=null;
	    try {
	        c=(Channel)cField.get(nm);
	    } catch (IllegalArgumentException|IllegalAccessException e) {
	        e.printStackTrace();
	    }
	    return c;
	}	

	@Override
	public void forceSetPositionRotation(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_10_R1.Entity me = ((CraftEntity)entity).getHandle();
        me.setLocation(x,y,z,yaw,pitch);
        if (entity instanceof Player) playerConnectionTeleport(entity,x,y,z,yaw,pitch,f,g);
        me.world.entityJoinedWorld(me, false);
	}
	
	private void playerConnectionTeleport(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
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
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,yaw,pitch,sssSet,0));
	}
	
	@Override
	public void playerConnectionSpin(Entity entity,float s) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)entity).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutPosition(0,0,0,s,0,ssSet,0));
	}
	
	@Override
	public void changeHitBox(Entity entity, double a0, double a1, double a2) {
		net.minecraft.server.v1_10_R1.Entity me = ((CraftEntity)entity).getHandle();
		me.getBoundingBox().a(a0,a1,a2);
	}
	
	
	@Override
	public void setItemMotion(Item i, Location ol, Location nl) {
		EntityItem ei=(EntityItem)((CraftItem)i).getHandle();
		ei.setPosition(ol.getX(), ol.getY(), ol.getZ());
	}

	@Override
	public void teleportEntityPacket(Entity entity) {
		net.minecraft.server.v1_10_R1.Entity me = ((CraftEntity)entity).getHandle();
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
	        			if (Utils.isNumeric(p[a])) {
	        				if (a==0) {
	        					speed=Double.parseDouble(p[a]);
	        				} else if (a==1) {
	        					aR=Float.parseFloat(p[a]);
	        				} else if (a==2) {
	        					zR=Float.parseFloat(p[a]);
	        				}
	        			}
	        		}
	        		if (Utils.isNumeric(data)) {
	        			speed = Double.parseDouble(data);
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
		PacketPlayOutEntityEquipment packet=new PacketPlayOutEntityEquipment(entity.getEntityId(), EnumItemSlot.CHEST, new ItemStack(Blocks.DIAMOND_BLOCK, 1));
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		while(it.hasNext()) {
			AbstractPlayer ap=it.next();
			CraftPlayer cp = (CraftPlayer)BukkitAdapter.adapt(ap);
			cp.getHandle().playerConnection.sendPacket(packet);
		}
	}

	@Override
	public void moveEntityPacket(Entity entity,Location cl,double x,double y,double z) {
		net.minecraft.server.v1_10_R1.Entity me = ((CraftEntity)entity).getHandle();
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
		net.minecraft.server.v1_10_R1.Entity me = ((CraftEntity)entity).getHandle();
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
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutCamera(((CraftEntity) entity).getHandle()));
	}

	@Override
	public void playEndScreenForPlayer(Player player,float f) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
        me.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4,f));
	}

	@Override
	public boolean playerIsSleeping(Player p) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSleeping()||me.isDeeplySleeping();
	}

	@Override
	public boolean playerIsRunning(Player p) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSprinting();
	}

	@Override
	public boolean playerIsCrouching(Player p) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return me.isSneaking();
	}

	@Override
	public boolean playerIsJumping(Player p) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
		return !me.onGround&&Utils.round(me.motY,5)!=-0.00784;
	}

	@Override
	public void forceCancelEndScreenPlayer(Player player) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)player).getHandle();
		me.playerConnection.sendPacket(new PacketPlayOutCloseWindow(0));
	}

	@Override
	public void fakeEntityDeath(Entity entity, long d) {
		net.minecraft.server.v1_10_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
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
		net.minecraft.server.v1_10_R1.EntityLiving me=((CraftLivingEntity)entity).getHandle();
	    DataWatcherObject<Integer>br=DataWatcher.a(EntityLiving.class,DataWatcherRegistry.b);
	    return me.getDataWatcher().get(br);
	}

	@Override
	public void modifyArrowsAtEntity(Entity entity,int a,char c) {
		net.minecraft.server.v1_10_R1.EntityLiving me=((CraftLivingEntity)entity).getHandle();
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
		net.minecraft.server.v1_10_R1.EntitySnowman me=((CraftSnowman)entity).getHandle();
	    DataWatcherObject<Byte>a=DataWatcher.a(EntitySnowman.class,DataWatcherRegistry.a);
        me.getDataWatcher().set(a,Byte.valueOf((byte)(me.getDataWatcher().get(a).byteValue()&-17)));
	}

	@Override
	public void setDeath(Player p, boolean b) {
		net.minecraft.server.v1_10_R1.EntityPlayer me = ((CraftPlayer)p).getHandle();
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
	        net.minecraft.server.v1_10_R1.Entity entity=((CraftEntity)e).getHandle();
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
	public float getItemCoolDown(Player p,int i1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setItemCooldown(Player p,int j1,int i1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public void setFieldOfViewPacketSend(Player player, float f1) {
		net.minecraft.server.v1_10_R1.EntityPlayer me=((CraftPlayer)player).getHandle();
		net.minecraft.server.v1_10_R1.PlayerAbilities arg=new net.minecraft.server.v1_10_R1.PlayerAbilities();
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
	public boolean addNBTTag(Entity e1, String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vec3D lastPosEntity(Entity e1) {
		net.minecraft.server.v1_10_R1.Entity me=((CraftEntity)e1).getHandle();
		return new Vec3D(me.motX,me.motY,me.motZ);
	}

	@Override
	public HashMap<Advancement, AdvancementProgress> getAdvMap(Player p, String s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void aiTargetSelector(LivingEntity entity, String uGoal, LivingEntity target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMNc(LivingEntity e1,String s1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceBowDraw(LivingEntity e1, LivingEntity target, boolean bl1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceSpectate(Player player, Entity entity, boolean bl1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getArmorStrength(LivingEntity e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void changeResPack(Player p, String url, String hash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceShield(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearActiveItem(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Zombie spawnCustomZombie(Location location,boolean sunBurn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playBlockBreak(int eid, Location location, int stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playAnimationPacket(LivingEntity e, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playAnimationPacket(LivingEntity e, Integer[] ints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPlayerToSleep(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extinguish(LivingEntity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWorldborder(Player p, int density, boolean play) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vec3D getAimBowTargetPosition(Player bukkit_player, LivingEntity bukkit_target) {
		// TODO Auto-generated method stub
		return null;
	}
}
