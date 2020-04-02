package com.gmail.berndivader.mythicmobsext.cachedowners;

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

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class CachedOwnerHandler implements Listener {
	protected final static Plugin plugin = Main.getPlugin();
	protected final static MobManager mobmanager = Utils.mobmanager;
	protected final static String cacheFileName = "CachedOwners.txt";
	protected static ConcurrentHashMap<UUID, UUID> cachedOwners;
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
		new BukkitRunnable() {
			@Override
			public void run() {
				CachedOwnerHandler.restoreMobOwners();
			}
		}.runTaskLater(Main.getPlugin(), 20l);
	}

	@EventHandler
	public void onMythicLoad(MythicMechanicLoadEvent e) {
		String mechanicsName = e.getMechanicName().toLowerCase();
		switch (mechanicsName) {
		case "setcachedowner":
			e.register(new CachedOwnerSkill(e.getContainer().getConfigLine(), e.getConfig()));
			break;
		case "restorecachedowner":
			e.register(new RestoreCachedOwnerMechanic(e.getContainer().getConfigLine(), e.getConfig()));
			break;
		}
	}

	@EventHandler
	public void onMythicReloaded(MythicReloadedEvent e) {
		CachedOwnerHandler.restoreMobOwners();
	}

	public static UUID getMobOwner(UUID slave_uuid) {
		if (cachedOwners.isEmpty() || !cachedOwners.containsKey(slave_uuid))
			return null;
		return cachedOwners.get(slave_uuid);
	}

	public static void restoreMobOwners() {
		if (CachedOwnerHandler.cachedOwners.isEmpty())
			return;
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<UUID, UUID> entry : CachedOwnerHandler.cachedOwners.entrySet()) {
					UUID owner = entry.getValue();
					UUID slave = entry.getKey();
					if (CachedOwnerHandler.mobmanager.isActiveMob(slave)) {
						ActiveMob am = CachedOwnerHandler.mobmanager.getActiveMob(slave).get();
						am.setOwner(owner);
					}
					;
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

	public static ConcurrentHashMap<UUID, UUID> loadCachedOwners() {
		ConcurrentHashMap<UUID, UUID> cachedOwners = new ConcurrentHashMap<>();
		try (Scanner scanner = new Scanner(new InputStreamReader(
				new FileInputStream(CachedOwnerHandler.dir + System.getProperty("file.separator") + cacheFileName),
				StandardCharsets.UTF_8))) {
			while (scanner.hasNext()) {
				String parse[] = scanner.next().split(";");
				if (parse.length == 2) {
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
		if (cachedOwners.isEmpty())
			return;
		for (Map.Entry<UUID, UUID> entry : CachedOwnerHandler.cachedOwners.entrySet()) {
			if (!CachedOwnerHandler.mobmanager.isActiveMob(entry.getKey())) {
				CachedOwnerHandler.removeCachedOwner(entry.getKey());
			}
		}
	}

	public static void saveCachedOwners() {
		if (CachedOwnerHandler.cachedOwners == null)
			CachedOwnerHandler.cachedOwners = new ConcurrentHashMap<>();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(CachedOwnerHandler.dir + System.getProperty("file.separator") + cacheFileName),
				StandardCharsets.UTF_8))) {
			writer.flush();
			for (Map.Entry<UUID, UUID> entry : CachedOwnerHandler.cachedOwners.entrySet()) {
				String p = entry.getKey().toString() + ";" + entry.getValue().toString() + "\n";
				writer.append(p);
			}
		} catch (IOException ex) {
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
