package com.gmail.berndivader.mythicmobsext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.cachedowners.CachedOwnerHandler;
import com.gmail.berndivader.mythicmobsext.commands.VersionCommand;
import com.gmail.berndivader.mythicmobsext.compatibility.disguise.LibsDisguisesSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.factions.FactionsSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.mobarena.MobArenaSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus.NoCheatPlusSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.protocollib.ProtocolLibSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.quests.QuestsSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.worldguard.WorldGuardFlag;
import com.gmail.berndivader.mythicmobsext.conditions.CustomConditions;
import com.gmail.berndivader.mythicmobsext.config.Config;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.Internals;
import com.gmail.berndivader.mythicmobsext.healthbar.HealthbarHandler;
import com.gmail.berndivader.mythicmobsext.javascript.JavaScript;
import com.gmail.berndivader.mythicmobsext.mechanics.CustomMechanics;
import com.gmail.berndivader.mythicmobsext.placeholders.PlaceholderRegistery;
import com.gmail.berndivader.mythicmobsext.targeters.CustomTargeters;
import com.gmail.berndivader.mythicmobsext.thiefs.Thiefs;
import com.gmail.berndivader.mythicmobsext.utils.EntityCacheHandler;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public class Main extends JavaPlugin {
	private static Main plugin;
	private static MythicPlayers mythicplayers;

	public static PluginManager pluginmanager;
	public static HealthbarHandler healthbarhandler;
	public static CachedOwnerHandler cachedOwnerHandler;
	public static EntityCacheHandler entityCacheHandler;
	public static Logger logger;
	public static Random random;
	public static boolean hasRpgItems = false;
	public static boolean slappyNewBorn = true;
	public static boolean server_running;

	public Thiefs thiefs;

	public Internals internals;
	public Externals externals;

	public void onEnable() {
		server_running = true;
		plugin = this;
		random = new Random();
		pluginmanager = plugin.getServer().getPluginManager();
		logger = plugin.getLogger();

		if (Config.update) {
			new BukkitRunnable() {
				@Override
				public void run() {

					String version = new String();
					PluginDescriptionFile pdf = getDescription();
					try {
						URL url = new URL(
								"https://raw.githubusercontent.com/BerndiVader/MythicMobsExtension/master/version.txt");
						try (InputStream in = url.openStream();
								BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
							version = br.readLine().toString();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						logger.warning("Could not read version file!");
					}
					if (!pdf.getVersion().endsWith("SNAPSHOT") && !pdf.getVersion().equals(version)) {
						logger.info("MythicMobsExtension v" + version + " is available, get it here:");
						logger.info("https://www.spigotmc.org/resources/mythicmobsextension.51884/");
					} else {
						logger.info("Plugin is up-to-date!");
					}
				}
			}.runTaskAsynchronously(this);
		}
		if (pluginmanager.isPluginEnabled("MythicMobs")) {
			NMSUtils.initialize();
			new Utils();
			internals = new Internals();
			if (Config.externals) {
				externals = new Externals();
				logger.info("enabled externals");
			}
			new CustomMechanics();
			logger.info("registered mechanics!");
			new CustomConditions();
			logger.info("registered conditions!");
			new CustomTargeters();
			logger.info("registered targeters!");
			if (Config.javascript) {
				new JavaScript();
				logger.info("enabled javascript!");
			}
			if (Config.m_players)
				Main.mythicplayers = new MythicPlayers(this);
			if (Config.m_thiefs)
				thiefs = new Thiefs();

			if (Config.wguard && pluginmanager.getPlugin("WorldGuard") != null)
				new WorldGuardFlag();
			if (Config.factions && pluginmanager.getPlugin("Factions") != null
					&& pluginmanager.getPlugin("MassiveCore") != null)
				new FactionsSupport();
			if (Config.rpgitems && pluginmanager.getPlugin("RPGItems") != null) {
				hasRpgItems = true;
				logger.info("using RPGItems");
			}
			if (Config.mobarena && pluginmanager.getPlugin("MobArena") != null)
				new MobArenaSupport();
			if (Config.h_displays && pluginmanager.getPlugin("HolographicDisplays") != null)
				Main.healthbarhandler = new HealthbarHandler(this);
			if (pluginmanager.getPlugin("ProtocolLib") != null) {
				new ProtocolLibSupport(this);
			}
			if (Config.quests && pluginmanager.getPlugin("Quests") != null)
				new QuestsSupport(this);
			if (pluginmanager.getPlugin("LibsDisguise") != null)
				new LibsDisguisesSupport();
			if (Config.ncp && pluginmanager.getPlugin("NoCheatPlus") != null)
				new NoCheatPlusSupport(this);
			if (Config.c_owners)
				cachedOwnerHandler = new CachedOwnerHandler(plugin);

			entityCacheHandler = new EntityCacheHandler();
			new VersionCommand(this);
			new File(this.getDataFolder().getPath() + "/images").mkdirs(); // Creates the file for the particleImage mechanic
			new File(this.getDataFolder().getPath() + "/files").mkdirs(); // Creates the file for the fileLine mechanic
			new BackBagHelper();
			new BossBars();
			new PlaceholderRegistery();

			
			// Why?
			new BukkitRunnable() {
				@Override
				public void run() {
					Utils.mythicmobs.getRandomSpawningManager().reload();
				}
			}.runTask(this);
		}
	}

	@Override
	public void onDisable() {
		server_running = false;
		if (entityCacheHandler != null)
			entityCacheHandler.stop();
		if (healthbarhandler != null) {
			Main.healthbarhandler.removeHealthbars();
			Main.healthbarhandler.removeSpeechBubbles();
		}
		if (Main.cachedOwnerHandler != null) {
			CachedOwnerHandler.cleanUp();
			CachedOwnerHandler.saveCachedOwners();
		}
		Main.mythicplayers = null;
		Main.cachedOwnerHandler = null;
		pluginmanager.disablePlugin(this);
	}

	public static Main getPlugin() {
		return plugin;
	}

	public MythicPlayers getMythicPlayers() {
		return Main.mythicplayers;
	}

}
