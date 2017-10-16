package com.gmail.berndivader.cachedowners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class CachedOwnerHandler implements Listener {
	protected final static Plugin plugin = Main.getPlugin();
	protected final static MobManager mobmanager = Main.getPlugin().getMobManager();
	protected final static String cacheFileName = "CachedOwners.txt";
	protected static ConcurrentHashMap<UUID,UUID>cachedOwners;
	protected static File dir;
	
	public CachedOwnerHandler(Plugin plugin) {
		CachedOwnerHandler.dir = CachedOwnerHandler.plugin.getDataFolder();
		if (!CachedOwnerHandler.chkDir()) {
			CachedOwnerHandler.createDir();
			CachedOwnerHandler.saveCachedOwners();
			CachedOwnerHandler.cachedOwners = new ConcurrentHashMap<>();
		} else {
			CachedOwnerHandler.cachedOwners = CachedOwnerHandler.loadCachedOwners();
		}
		Main.pluginmanager.registerEvents(this, plugin);
		CachedOwnerHandler.cleanUp();
		CachedOwnerHandler.restoreMobOwner();
	}
	
	@EventHandler
	public void onMythicLoad(MythicMechanicLoadEvent e) {
		String mechanicsName = e.getMechanicName().toLowerCase();
		SkillMechanic mechanic = null;
		switch (mechanicsName) {
		case "setcachedowner": {
			mechanic = new cachedOwnerSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(mechanic);
			break;			
		}}
	}

	@EventHandler
	public void onMythicReloaded(MythicReloadedEvent e) {
		CachedOwnerHandler.cleanUp();
		CachedOwnerHandler.restoreMobOwner();
	}
	
	public static void restoreMobOwner() {
		if (CachedOwnerHandler.cachedOwners.isEmpty()) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<UUID,UUID>entry:CachedOwnerHandler.cachedOwners.entrySet()) {
					UUID owner = entry.getValue();
					UUID slave = entry.getKey();
					if (CachedOwnerHandler.mobmanager.isActiveMob(slave)) {
						ActiveMob am = CachedOwnerHandler.mobmanager.getActiveMob(slave).get();
						am.setOwner(owner);
					};
				}
			}
		}.runTaskAsynchronously(plugin);		
	}
	
	public static void addCachedOwner(UUID slave, UUID owner) {
		if (CachedOwnerHandler.cachedOwners.containsKey(slave)) {
			CachedOwnerHandler.cachedOwners.replace(slave, owner);
		} else {
			CachedOwnerHandler.cachedOwners.put(slave, owner);
		}
	}

	public static void removeCachedOwner(UUID slave) {
		if (CachedOwnerHandler.cachedOwners.containsKey(slave)) {
			CachedOwnerHandler.cachedOwners.remove(slave);
		}
	}
	
	public static ConcurrentHashMap<UUID,UUID> loadCachedOwners() {
		ConcurrentHashMap<UUID,UUID>cachedOwners = new ConcurrentHashMap<>();
		try (Scanner scanner = new Scanner(
				new InputStreamReader(
						new FileInputStream(CachedOwnerHandler.dir+System.getProperty("file.separator")+cacheFileName), StandardCharsets.UTF_8))) {
			while (scanner.hasNext()) {
				String parse[] = scanner.next().split(";");
				if (parse.length==2) {
					UUID suuid = UUID.fromString(parse[0]);
					UUID muuid = UUID.fromString(parse[1]);
					cachedOwners.put(suuid, muuid);
				}
			}
		} catch (IOException ex) {
			// empty
		} 
		return cachedOwners;
	}
	
	public static void cleanUp() {
		if (cachedOwners.isEmpty()) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<UUID,UUID>entry:CachedOwnerHandler.cachedOwners.entrySet()) {
					if (!CachedOwnerHandler.mobmanager.isActiveMob(entry.getKey())) {
						CachedOwnerHandler.removeCachedOwner(entry.getKey());
					};
				}
			}
		}.runTaskLater(plugin, 1L);
	}
	
	public static void saveCachedOwners() {
		if (CachedOwnerHandler.cachedOwners==null)CachedOwnerHandler.cachedOwners=new ConcurrentHashMap<>();
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(CachedOwnerHandler.dir+System.getProperty("file.separator")+cacheFileName), StandardCharsets.UTF_8))) {
			writer.flush();
 			for(Map.Entry<UUID,UUID>entry:CachedOwnerHandler.cachedOwners.entrySet()) {
				String p = entry.getKey().toString()+";"+entry.getValue().toString()+"\n";
				writer.append(p);
			}
		} 
		catch (IOException ex) {
			ex.printStackTrace();
			// empty
		}
	}
	
	private static boolean chkDir() {
		return CachedOwnerHandler.dir.exists();
	}
	
	private static boolean createDir() {
		CachedOwnerHandler.dir.mkdirs();
		return true;
	}
	
}
