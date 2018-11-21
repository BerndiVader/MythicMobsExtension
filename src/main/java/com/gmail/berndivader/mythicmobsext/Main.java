package com.gmail.berndivader.mythicmobsext;

import java.io.BufferedReader;
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
import com.gmail.berndivader.mythicmobsext.cachedowners.CachedOwnerHandler;
import com.gmail.berndivader.mythicmobsext.compatibility.disguise.LibsDisguisesSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.factions.FactionsFlagConditions;
import com.gmail.berndivader.mythicmobsext.compatibility.factions.FactionsFlags;
import com.gmail.berndivader.mythicmobsext.compatibility.mobarena.MobArenaConditions;
import com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus.NoCheatPlusSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.protocollib.ProtocolLibSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.quests.QuestsSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.worldguard.WorldGuardFlag;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.NMSUtils;
import com.gmail.berndivader.mythicmobsext.conditions.CustomConditions;
import com.gmail.berndivader.mythicmobsext.config.Config;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.Internals;
import com.gmail.berndivader.mythicmobsext.guardianbeam.GuardianBeam;
import com.gmail.berndivader.mythicmobsext.mechanics.CustomMechanics;
import com.gmail.berndivader.mythicmobsext.healthbar.HealthbarHandler;
import com.gmail.berndivader.mythicmobsext.javascript.JavaScript;
import com.gmail.berndivader.mythicmobsext.targeters.CustomTargeters;
import com.gmail.berndivader.mythicmobsext.thiefs.Thiefs;
import com.gmail.berndivader.mythicmobsext.utils.EntityCacheHandler;
import com.gmail.berndivader.mythicmobsext.utils.Pre44MobSpawnEvent;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public class Main extends JavaPlugin {
	private static Main plugin;
	public static HealthbarHandler healthbarhandler;
	public static CachedOwnerHandler cachedOwnerHandler;
	public static EntityCacheHandler entityCacheHandler;
	public static Random random;
	public static Integer wgVer;
	public static FactionsFlags fflags;
	public static boolean hasRpgItems = false;
	public static Logger logger;
	public static PluginManager pluginmanager;
	public static boolean slappyNewBorn = true;
	private static MythicPlayers mythicplayers;
	public Thiefs thiefs;
	
	public Internals internals;
	public Externals externals;

	public void onEnable() {
		plugin = this;
		random = new Random();
		pluginmanager = plugin.getServer().getPluginManager();
		logger = plugin.getLogger();
		NMSUtils.initialize();
		
		if (Config.update) {
			new BukkitRunnable() {
				@Override
				public void run() {
					String version=new String("");
					PluginDescriptionFile pdf = getDescription();
					try {
						URL url = new URL("https://raw.githubusercontent.com/BerndiVader/MythicMobsExtension/master/version.txt");
						try (InputStream in=url.openStream();
						BufferedReader br=new BufferedReader( new InputStreamReader(in))) {
							version=br.readLine().toString();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						logger.warning("Could not read version file!");
					}
					if (!pdf.getVersion().endsWith("SNAPSHOT")&&!pdf.getVersion().equals(version)) {
						logger.info("MythicMobsExtension v"+version+" is available, get it here:");
						logger.info("https://www.spigotmc.org/resources/mythicmobsextension.51884/");
					} else {
						logger.info("Plugin is up-to-date!");
					}
				}
			}.runTaskAsynchronously(this);
		}
		if (pluginmanager.isPluginEnabled("MythicMobs")) {
			new Utils();
			internals=new Internals();
			if (Config.externals) {
				externals=new Externals();
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
			if (Config.m_players) Main.mythicplayers=new MythicPlayers(this);
			if (Config.m_thiefs) thiefs=new Thiefs();
			if (Config.wguard&&pluginmanager.getPlugin("WorldGuard")!=null) {
				new WorldGuardFlag();
			}
			if (Config.factions&&pluginmanager.getPlugin("Factions")!=null&&pluginmanager.getPlugin("MassiveCore")!=null) {
				logger.info("using Factions");
				fflags = new FactionsFlags();
				new FactionsFlagConditions();
			}
			if (Config.rpgitems&&pluginmanager.getPlugin("RPGItems") != null) {
				hasRpgItems = true;
				logger.info("using RPGItems");
			}
			if (Config.mobarena&&pluginmanager.getPlugin("MobArena")!=null) {
				new MobArenaConditions();
				logger.info("using MobArena");
			}
			if (Config.h_displays&&pluginmanager.getPlugin("HolographicDisplays")!=null) {
				logger.info("using HolographicDisplays");
				Main.healthbarhandler=new HealthbarHandler(this);
			}
			if(ProtocolLibSupport.isPresent()) {
				new GuardianBeam(this);
				new ProtocolLibSupport(this);
			}
			if (Config.quests&&QuestsSupport.isPresent()) new QuestsSupport(this);
			if (LibsDisguisesSupport.isPresent()) new LibsDisguisesSupport();
			if (Config.ncp&&NoCheatPlusSupport.isPresent()) new NoCheatPlusSupport(this);
			if (Config.c_owners) cachedOwnerHandler = new CachedOwnerHandler(plugin);
			if (Config.pre44spawn) new Pre44MobSpawnEvent();
			
			entityCacheHandler=new EntityCacheHandler();
			
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
		if(entityCacheHandler!=null) entityCacheHandler.stop();
		if (healthbarhandler!=null) {
			Main.healthbarhandler.removeHealthbars();
			Main.healthbarhandler.removeSpeechBubbles();
		}
		if (Main.cachedOwnerHandler!=null) CachedOwnerHandler.saveCachedOwners();
		Main.mythicplayers = null;
		Main.cachedOwnerHandler = null;
		Main.fflags = null;
		pluginmanager.disablePlugin(this);
	}

	public static Main getPlugin() {
		return plugin;
	}

	public MythicPlayers getMythicPlayers() {
		return Main.mythicplayers;
	}

}
