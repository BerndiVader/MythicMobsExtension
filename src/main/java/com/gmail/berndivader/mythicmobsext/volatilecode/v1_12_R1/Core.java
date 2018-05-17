package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_12_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.config.Config;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtil;
import com.gmail.berndivader.mythicmobsext.conditions.GetLastDamageIndicatorCondition;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.entitiy.MythicEntityParrot;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathFinderGoalShoot;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalAttack;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalBreakBlocks;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalJumpOffFromVehicle;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalMeleeRangeAttack;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalNotifyOnCollide;
import com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalReturnHome;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;

public class Core 
implements Handler,Listener {
	
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
	
	static {
        wb=new WorldBorder();
        wb.world=MinecraftServer.getServer().getWorldServer(1);
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
	
	public Core() {
		if (Config.m_parrot) registerCustomEntity("mythic_parrot",105,MythicEntityParrot.class);
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}
	
	private static void registerCustomEntity(String s1,int i1,Class<? extends net.minecraft.server.v1_12_R1.Entity>c1) {
		MinecraftKey k=new MinecraftKey(s1);
		EntityTypes.b.a(i1,k,c1);
		if (!EntityTypes.d.contains(k)) EntityTypes.d.add(k);
	}
	
	@EventHandler
	public void onJoinRegisterChannelListener(PlayerJoinEvent e) {
		Utils.pl.put(e.getPlayer().getUniqueId(),new com.gmail.berndivader.mythicmobsext.utils.Vec3D(0d,0d,0d));
		final Player player=e.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
				    chl.put(player.getUniqueId(),channelPlayerInProzess(player,new PacketReceivingHandler() {
				    	@Override
				        public void handle(Player p, PacketPlayInArmAnimation packet) {
				        	float f1=getIndicatorPercentage(p);
				        	p.setMetadata(GetLastDamageIndicatorCondition.meta_LASTDAMAGEINDICATOR,new FixedMetadataValue(Main.getPlugin(),f1));;
				            return;
				        }
						@Override
						public void handle(Player p,PacketPlayInFlying packet) {
							net.minecraft.server.v1_12_R1.EntityPlayer me=((CraftPlayer)p).getHandle();
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
				} catch (NoSuchElementException ex) {
					Main.logger.warning("Error while register Channellistener for player " + e.getPlayer().getName());
					try {
					    PrintWriter pw=new PrintWriter(new File(Main.getPlugin().getDataFolder()+File.separator+"channelerror.txt"));
					    ex.printStackTrace(pw);
					    pw.close();			
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		}.runTaskLater(Main.getPlugin(),1l);
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
	    		case "PacketPlayInBlockDig":
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
	public Parrot spawnCustomParrot(Location l1, boolean b1) {
		net.minecraft.server.v1_12_R1.World world=((CraftWorld)l1.getWorld()).getHandle();
		final MythicEntityParrot mep=new MythicEntityParrot(world);
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
		if (f1!=0) {
			player.setMetadata(Utils.meta_WALKSPEED,new FixedMetadataValue(Main.getPlugin(),arg1.walkSpeed));
		} else if (player.hasMetadata(Utils.meta_WALKSPEED)) {
			f1=player.getMetadata(Utils.meta_WALKSPEED).get(0).asFloat();
		}
		arg1.walkSpeed=f1;
		me.playerConnection.sendPacket(new PacketPlayOutAbilities(arg1));
    }
	
	@Override
	public void removeSnowmanHead(Entity entity) {
		net.minecraft.server.v1_12_R1.EntitySnowman me = ((CraftSnowman)entity).getHandle();
		me.setHasPumpkin(false);
	}
	
	public void advancementTest(Player player) {
		
	}
	
	
	@Override
	public int arrowsOnEntity(Entity entity) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
		return me.getArrowCount();
	}
	
	@Override
	public void modifyArrowsAtEntity(Entity entity, int a, char c) {
		net.minecraft.server.v1_12_R1.EntityLiving me = ((CraftLivingEntity)entity).getHandle();
		switch(c) {
		case 'A':
			a+=me.getArrowCount();
			break;
		case 'S':
			a=me.getArrowCount()-a;
			if(a<0)a=0;
			break;
		case 'C':
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
				sendPlayerPacketsAsync(Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator()
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
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
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
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{packet}));
	}

	@Override
	public void teleportEntityPacket(Entity entity) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(me);
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
		sendPlayerPacketsAsync(it,Arrays.asList(new Packet[]{tp}));
	}
	
	@Override
	public void moveEntityPacket(Entity entity,Location cl,double x,double y,double z) {
		net.minecraft.server.v1_12_R1.Entity me = ((CraftEntity)entity).getHandle();
		double x1=cl.getX()-me.locX;
		double y1=cl.getY()-me.locY;
		double z1=cl.getZ()-me.locZ;
		PacketPlayOutEntityVelocity vp = new PacketPlayOutEntityVelocity(me.getId(),x1,y1,z1);
		Iterator<AbstractPlayer> it=Utils.mythicmobs.getEntityManager().getPlayersInRangeSq(BukkitAdapter.adapt(entity.getLocation()),8192).iterator();
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
        	i=Integer.parseInt(parse[0]);
    		String[]cpy=new String[parse.length-1];
    		System.arraycopy(parse,0,cpy,0,0);
    		System.arraycopy(parse,1,cpy,0,parse.length-1);
    		parse=cpy;
        }
      	if(parse.length>0) {
       		goal=parse[0];
       		if (parse.length>1) {
       			data = parse[1];
       		}
       		if (parse.length>2) {
       			data1 = parse[2];
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
						Entity ee = NMSUtil.getEntity(w, uuid);
	        			if (ee instanceof LivingEntity) {
	        		        tE = (EntityLiving)((CraftLivingEntity)(LivingEntity)ee).getHandle();
	        			}
	        		}
	        		if (tE!=null && tE.isAlive()) {
		            	goals.a(i, (PathfinderGoal)new com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals.PathfinderGoalFollowEntity(e,tE,speed,zR,aR));
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
	        case "notifycollide": {
	        	if (e instanceof EntityInsentient) {
	        		int c=data!=null&&Utils.isNumeric(data)?Integer.parseInt(data):5;
                    goals.a(i,(PathfinderGoal)new PathfinderGoalNotifyOnCollide(e,c));
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
	        	Utils.mythicmobs.getVolatileCodeHandler().aiGoalSelectorHandler(entity, gList);
	        }}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	interface PacketReceivingHandler {
	    void handle(Player p,PacketPlayInArmAnimation packet);
	    void handle(Player p,PacketPlayInFlying packet);
	    void handle(Player p,PacketPlayInSteerVehicle packet);
	    void handle(Player p,PacketPlayInBlockDig packet);
	}
	
	@Override
	public boolean getNBTValueOf(Entity e1, String s1,boolean b1) {
		return getNBTValue(e1,s1);
	}

	@Override
	public boolean addNBTTag(Entity e1,String s) {
		net.minecraft.server.v1_12_R1.Entity me=((CraftEntity)e1).getHandle();
		NBTTagCompound nbt=null,nbt2=null,nbt3=null;
		if ((nbt2=TFa(me))!=null) {
			nbt3=nbt2.g();
			try {
				nbt=MojangsonParser.parse(s);
			}
			catch (MojangsonParseException ex) {
				System.err.println("Error setting NBT: "+ex.getLocalizedMessage());
				return false;
			}
			nbt2.a(nbt);
			me.a(me.getUniqueID());
			if(nbt2.equals(nbt3)) {
				return false;
			}
			if (me instanceof EntityLiving) {
				((EntityLiving)me).a(nbt2);
			} else {
				me.f(nbt2);
			}
			return true;
		}
		return false;
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
				System.err.println("Error testing NBT: "+ex.getLocalizedMessage());
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
		case NBT.TAG_LIST:
            NBTTagList nbl1=(NBTTagList)nb1;
            NBTTagList nbl2=(NBTTagList)nb2;
            for(int i1=0;i1<nbl1.size();i1++) {
            	NBTBase nb3=nbl1.i(i1);
            	NBTBase nb4=nbl2.i(i1);
            	if (nb3.toString().toLowerCase().contains("id:\"ignore\"")) nb3=nb4;
            	if (!(bl1=nbtA(nb3,nb4,bl1))) break;
            }
			break;
		case NBT.TAG_COMPOUND:
            NBTTagCompound nbt1=(NBTTagCompound)nb1;
            NBTTagCompound nbt2=(NBTTagCompound)nb2;
            if (nbt1.isEmpty()&&!nbt2.isEmpty()) bl1=false;
            for (String s:nbt1.c()) {
            	if (!bl1) break;
            	if (nbt2.hasKey(s)) {
	                NBTBase nb3=nbt1.get(s);
	                bl1=nbtA(nb3,nbt2.get(s),bl1=true);
            	} else {
	                NBTBase nb3=nbt1.get(s);
	                bl1=nbtA(nb3,nbt2.get(s),bl1=false);
            	}
            }
			break;
		default:
			bl1=Utils.parseNBToutcome(nb1.toString(),nb2.toString(),nb1.getTypeId());
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
	
	@Override
	public void moveto(LivingEntity entity) {
		net.minecraft.server.v1_12_R1.Entity e=((CraftLivingEntity)entity).getHandle();
	}
	
	@Override
	public void setWBWB(Player p,boolean bl1) {
		EntityPlayer ep=((CraftPlayer)p).getHandle();
		WorldBorder wb=ep.world.getWorldBorder();
		if (bl1) wb=Core.wb;
  		PacketPlayOutWorldBorder ppw=new PacketPlayOutWorldBorder(wb,EnumWorldBorderAction.INITIALIZE);
   		ep.playerConnection.sendPacket(ppw);
	}

	@Override
	public Vec3D lastPosEntity(Entity e1) {
		net.minecraft.server.v1_12_R1.Entity me=((CraftEntity)e1).getHandle();
		return new Vec3D(me.motX,me.motY,me.motZ);
	}
	
	@Override
	public HashMap<org.bukkit.advancement.Advancement, org.bukkit.advancement.AdvancementProgress> getAdvMap(Player p,String s1) {
		return null;
	}
}
