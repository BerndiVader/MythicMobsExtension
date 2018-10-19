package com.gmail.berndivader.mythicmobsext.mechanics.guardianbeam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

public 
class 
GuardianBeam {

	public final int duration,distanceSquared,squid,guardian;
	public final Entity startEntity,endEntity;
	public final Location start,end;
	private final Object guardianPacketSpawn,squidPacketSpawn,guardianPacketMove,squidPacketMove,destroyPacket;
	private BukkitRunnable run;
	
	public GuardianBeam(Location start,Location end,int duration,int distance) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		startEntity=null;
		endEntity=null;
		this.start=start;
		this.end=end;
		this.duration=duration;
		distanceSquared=distance*distance;
		
		squidPacketMove=null;
		guardianPacketMove=null;
		
		squidPacketSpawn=GuardianBeamPackets.createPacketSquidSpawn(end);
		squid=(int)NMSUtils.getField(GuardianBeamPackets.packetSpawn,"a",squidPacketSpawn);
		guardianPacketSpawn=GuardianBeamPackets.createPacketGuardianSpawn(start,squid);
		guardian=(int)NMSUtils.getField(GuardianBeamPackets.packetSpawn,"a",guardianPacketSpawn);
		destroyPacket=GuardianBeamPackets.createPacketRemoveEntities(squid,guardian);
	}
	
	public GuardianBeam(Entity startEntity,Entity endEntity,int duration,int distance) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		this.start=null;
		this.end=null;
		this.startEntity=startEntity;
		this.endEntity=endEntity;
		this.duration=duration;
		distanceSquared=distance*distance;
		
		squidPacketMove=null;
		guardianPacketMove=null;
		
		squidPacketSpawn=GuardianBeamPackets.createPacketSquidSpawn(end);
		squid=(int)NMSUtils.getField(GuardianBeamPackets.packetSpawn,"a",squidPacketSpawn);
		guardianPacketSpawn=GuardianBeamPackets.createPacketGuardianSpawn(start,squid);
		guardian=(int)NMSUtils.getField(GuardianBeamPackets.packetSpawn,"a",guardianPacketSpawn);
		destroyPacket=GuardianBeamPackets.createPacketRemoveEntities(squid,guardian);
	}
	
	
	public void start(Plugin plugin){
		run = new BukkitRunnable() {
			int time=duration;
			HashSet<Player>show=new HashSet<>();
			@Override
			public void run() {
				try {
					if(time==0) {
						cancel();
						return;
					}
					
					for(Player p:start.getWorld().getPlayers()) {
						if (isCloseEnough(p.getLocation())){
							if (!show.contains(p)) {
								sendStartPackets(p);
								show.add(p);
							}
						}else if(show.contains(p)){
							GuardianBeamPackets.sendPacket(p,destroyPacket);
							show.remove(p);
						}
					}
					if (time!=-1) time--;
				}catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public synchronized void cancel() throws IllegalStateException {
				super.cancel();
				try {
					for(Player p:show){
						GuardianBeamPackets.sendPacket(p,destroyPacket);
					}
				}catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				run=null;
			}
		};
		run.runTaskTimer(plugin,0l,20l);
	}
	
	public void stop(){
		if(run==null) return;
		run.cancel();
	}
	
	public boolean isStarted(){
		return run!=null;
	}
	
	private void sendStartPackets(Player p) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
		GuardianBeamPackets.sendPacket(p,squidPacketSpawn);
		GuardianBeamPackets.sendPacket(p,guardianPacketSpawn);
	}
	
	private boolean isCloseEnough(Location location) {
        return start.distanceSquared(location)<=distanceSquared||end.distanceSquared(location)<=distanceSquared;
    }
	
	public static class GuardianBeamPackets{
		private static int lastIssuedEID=2000000000;
		
		static int generateEID() {
			return lastIssuedEID++;
		}
		
		private static String npack="net.minecraft.server."+Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]+".";
		private static String cpack=Bukkit.getServer().getClass().getPackage().getName()+".";
		private static Object fakeSquid;
		private static Method watcherSet;
		private static Method watcherRegister;
		public static Class<?> packetSpawn;
		public static Class<?> packetMove;

		static{
			try {
				fakeSquid=Class.forName(cpack+"entity.CraftSquid").getDeclaredConstructors()[0].newInstance(null,Class.forName(npack + "EntitySquid").getDeclaredConstructors()[0].newInstance(new Object[]{null}));
				watcherSet=NMSUtils.getMethod(Class.forName(npack+"DataWatcher"),"set");
				watcherRegister=NMSUtils.getMethod(Class.forName(npack + "DataWatcher"),"register");
				packetSpawn=Class.forName(npack+"PacketPlayOutSpawnEntityLiving");
//				packetMove=Class.forName(npack+"PacketPlayOutEntityMove");
			}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
		public static void sendPacket(Player p,Object packet) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
			Object entityPlayer=Class.forName(cpack+"entity.CraftPlayer").getDeclaredMethod("getHandle").invoke(p);
			Object playerConnection=entityPlayer.getClass().getDeclaredField("playerConnection").get(entityPlayer);
			playerConnection.getClass().getDeclaredMethod("sendPacket",Class.forName(npack+"Packet")).invoke(playerConnection,packet);
		}

		public static Object createPacketSquidSpawn(Location location) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
			Object packet=packetSpawn.newInstance();
			Object packet1=packetMove.newInstance();

			NMSUtils.setField(packet,"a",generateEID());
			NMSUtils.setField(packet,"b",UUID.randomUUID());
			NMSUtils.setField(packet,"c",94);
			NMSUtils.setField(packet,"d",location.getX());
			NMSUtils.setField(packet,"e",location.getY());
			NMSUtils.setField(packet,"f",location.getZ());
			NMSUtils.setField(packet,"j",(byte)(location.getYaw()*256.0f/360.0f));
			NMSUtils.setField(packet,"k",(byte)(location.getPitch()*256.0f/360.0f));
			Object nentity=fakeSquid.getClass().getDeclaredMethod("getHandle").invoke(fakeSquid);
			Object watcher=Class.forName(npack+"Entity").getDeclaredMethod("getDataWatcher").invoke(nentity);
			watcherSet.invoke(watcher,NMSUtils.getField(Class.forName(npack+"Entity"),"Z",null),(byte)32);
			NMSUtils.setField(packet,"m",watcher);

			return packet;
		}

		public static Object createPacketGuardianSpawn(Location location,int entityId) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException {
			Object packet=packetSpawn.newInstance();
			Object packet1=packetMove.newInstance();

			NMSUtils.setField(packet,"a",generateEID());
			NMSUtils.setField(packet,"b",UUID.randomUUID());
			NMSUtils.setField(packet,"c",68);
			NMSUtils.setField(packet,"d",location.getX());
			NMSUtils.setField(packet,"e",location.getY());
			NMSUtils.setField(packet,"f",location.getZ());
			NMSUtils.setField(packet,"j",(byte)(location.getYaw()*256.0f/360.0f));
			NMSUtils.setField(packet,"k",(byte)(location.getPitch()*256.0f/360.0f));

			Object nentity=fakeSquid.getClass().getDeclaredMethod("getHandle").invoke(fakeSquid);
			Object watcher=Class.forName(npack+"Entity").getDeclaredMethod("getDataWatcher").invoke(nentity);
			watcherSet.invoke(watcher,NMSUtils.getField(Class.forName(npack+"Entity"),"Z",null),(byte)32);
			try{
				watcherSet.invoke(watcher,NMSUtils.getField(Class.forName(npack + "EntityGuardian"),"bA",null),false);
			}catch (InvocationTargetException ex){
				watcherRegister.invoke(watcher,NMSUtils.getField(Class.forName(npack+"EntityGuardian"),"bA",null),false);
			}
			try{
				watcherSet.invoke(watcher,NMSUtils.getField(Class.forName(npack+"EntityGuardian"),"bB",null),entityId);
			}catch (InvocationTargetException ex){
				watcherRegister.invoke(watcher,NMSUtils.getField(Class.forName(npack+"EntityGuardian"),"bB",null),entityId);
			}

			NMSUtils.setField(packet,"m",watcher);

			return packet;
		}

		public static Object createPacketRemoveEntities(int squidId,int guardianId) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
			Object packet=Class.forName(npack + "PacketPlayOutEntityDestroy").newInstance();
			NMSUtils.setField(packet,"a",new int[]{squidId,guardianId});
			return packet;
		}
	}
	
}