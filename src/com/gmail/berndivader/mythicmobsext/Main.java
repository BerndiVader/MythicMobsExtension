package com.gmail.berndivader.mythicmobsext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.garbagemule.MobArena.MobArenaHandler;
import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.cachedowners.CachedOwnerHandler;
import com.gmail.berndivader.healthbar.HealthbarHandler;
import com.gmail.berndivader.mythicmobsext.conditions.factions.FactionsFlags;
import com.gmail.berndivader.mythicmobsext.conditions.factions.FactionsFlagConditions;
import com.gmail.berndivader.mythicmobsext.conditions.mobarena.MobArenaConditions;
import com.gmail.berndivader.mythicmobsext.conditions.own.OwnConditions;
import com.gmail.berndivader.mythicmobsext.conditions.worldguard.WorldGuardFlags;
import com.gmail.berndivader.mythicmobsext.conditions.worldguard.WorldGuardFlag;
import com.gmail.berndivader.nanpatch.NaNpatch;
import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.volatilecode.VolatileHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class Main extends JavaPlugin {
	private static Main plugin;
	public static HealthbarHandler healthbarhandler;
	public static CachedOwnerHandler cachedOwnerHandler;
	public static Random random;
	public static Integer wgVer;
	public static WorldGuardFlags wgf;
	public static FactionsFlags fflags;
	public static final String mpNameVar = "mythicprojectile";
	public static final String noTargetVar = "nottargetable";
	public static boolean hasRpgItems = false;
	public static Logger logger;
	public static PluginManager pluginmanager;
	public static boolean slappyNewBorn = true;
	public static MythicMobs mythicmobs;
	public WorldGuardPlugin wg;
	private ThiefHandler thiefhandler;
	private static MobManager mobmanager;
	private static MythicPlayers mythicplayers;
	private MobArenaHandler maHandler;
	private VolatileHandler volatilehandler;
	public static HashSet<Entity>entityCache=new HashSet<Entity>();
	public static boolean disguisepresent;

	public void onEnable() {
		plugin = this;
		random = new Random();
		pluginmanager = plugin.getServer().getPluginManager();
		logger = plugin.getLogger();
/**
 *TODO: implement helper if needed
		if (!pluginmanager.isPluginEnabled("Helper")) {
			Plugin helper;
			logger.info("Helper not found. Try to register");
			helper=RegisterHelper.init();
			if (helper==null) {
				logger.info("There was a problem registering helper classes!");
				pluginmanager.disablePlugin(this);
				return;
			}
		}
 */
		this.volatilehandler = this.getVolatileHandler();
		if (pluginmanager.isPluginEnabled("MythicMobs")) {
			Main.mythicmobs = MythicMobs.inst();
			Main.mobmanager = Main.mythicmobs.getMobManager();
			pluginmanager.registerEvents(new UndoBlockListener(), this);
			this.thiefhandler = new ThiefHandler();
			logger.info("registered ThiefHandlers!");
			new Utils(this);
			new CustomMechanics(this);
			logger.info("Found MythicMobs, registered CustomSkills.");
			Main.mythicplayers = new MythicPlayers(this);
			logger.info("registered MythicPlayers!");
			new NaNpatch(this);
			logger.info("NaN patch applied!");
			new OwnConditions();
			if (pluginmanager.isPluginEnabled("WorldGuard")) {
				wg = getWorldGuard();
				wgf = new WorldGuardFlags();
				new WorldGuardFlag();
				logger.info("Worldguard support enabled!");
			}
			if (pluginmanager.isPluginEnabled("Factions")
					&& pluginmanager.isPluginEnabled("MassiveCore")) {
				fflags = new FactionsFlags();
				new FactionsFlagConditions();
				logger.info("Faction support enabled!");
			}
			if (pluginmanager.getPlugin("RPGItems") != null) {
				logger.info("RPGItems support enabled!");
				hasRpgItems = true;
			}
			if (pluginmanager.isPluginEnabled("MobArena")) {
				maHandler = new MobArenaHandler();
				new MobArenaConditions();
				logger.info("MobArena support enabled!");
			}
			if (pluginmanager.isPluginEnabled("HolographicDisplays")) {
				Main.healthbarhandler = new HealthbarHandler(this);
				logger.info("HolographicDisplays support enabled!");
			}
			Main.disguisepresent=pluginmanager.isPluginEnabled("LibsDisguise")?true:false;
			cachedOwnerHandler = new CachedOwnerHandler(plugin);
			logger.info("CachedOwner support enabled!");
			
	        new BukkitRunnable() {
				@Override
				public void run() {
					Main.mythicmobs.getRandomSpawningManager().reload();
				}
			}.runTask(this);
			
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Iterator<Entity>it=Main.entityCache.iterator();it.hasNext();) {
						Entity entity=it.next();
						if (entity!=null) {
							if (NMSUtils.getEntity(entity.getWorld(),entity.getUniqueId())==null) it.remove();
						} else {
							it.remove();
						}
					}
				}
			}.runTaskTimerAsynchronously(this,600L,600L);
		}
	}

	@Override
	public void onDisable() {
		for(Iterator<Entity>a=entityCache.iterator();a.hasNext();) {
			Entity e=a.next();
			if (e!=null) e.remove();
		}
		if (healthbarhandler!=null) {
			Main.healthbarhandler.removeHealthbars();
			Main.healthbarhandler.removeSpeechBubbles();
		}
		if (this.thiefhandler!=null) Bukkit.getServer().getScheduler().cancelTask(this.thiefhandler.taskid.getTaskId());
		if (Main.cachedOwnerHandler!=null) CachedOwnerHandler.saveCachedOwners();
		this.thiefhandler = null;
		Main.mythicplayers = null;
		Main.mythicmobs = null;
		this.maHandler = null;
		this.volatilehandler = null;
		this.wg = null;
		Main.cachedOwnerHandler = null;
		Main.wgf = null;
		Main.fflags = null;
		pluginmanager.disablePlugin(this);
	}

	public static Main getPlugin() {
		return plugin;
	}

	public MythicMobs getMythicMobs() {
		return Main.mythicmobs;
	}

	public MythicPlayers getMythicPlayers() {
		return Main.mythicplayers;
	}

	public MobManager getMobManager() {
		return Main.mobmanager;
	}

	public ThiefHandler getThiefHandler() {
		return this.thiefhandler;
	}

	private static WorldGuardPlugin getWorldGuard() {
		return (WorldGuardPlugin) pluginmanager.getPlugin("WorldGuard");
	}

	public MobArenaHandler getMobArenaHandler() {
		return this.maHandler;
	}

    public VolatileHandler getVolatileHandler() {
        if (this.volatilehandler != null) return this.volatilehandler;
		String v, n;
    	VolatileHandler vh=null;
		n = Bukkit.getServer().getClass().getPackage().getName();
        v = n.substring(n.lastIndexOf(46) + 1);
        try {
            Class<?> c = Class.forName("com.gmail.berndivader.volatilecode.Volatile_"+v);
            if (VolatileHandler.class.isAssignableFrom(c)) {
            	vh = (VolatileHandler)c.getConstructor(new Class[0]).newInstance(new Object[0]);
            }
        } catch (Exception ex) {
        	if (ex instanceof ClassNotFoundException) {
        		logger.warning("Server version not supported!");
        	}
        	ex.printStackTrace();
        }
        return vh;
    }

}
