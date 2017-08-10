package com.gmail.berndivader.mmcustomskills26;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.garbagemule.MobArena.MobArenaHandler;
import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.healthbar.HealthbarHandler;
import com.gmail.berndivader.mmcustomskills26.conditions.Factions.FactionsFlags;
import com.gmail.berndivader.mmcustomskills26.conditions.Factions.mmFactionsFlag;
import com.gmail.berndivader.mmcustomskills26.conditions.MobArena.mmMobArenaConditions;
import com.gmail.berndivader.mmcustomskills26.conditions.Own.mmOwnConditions;
import com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard.WorldGuardFlags;
import com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard.mmWorldGuardFlag;
import com.gmail.berndivader.nanpatch.NaNpatch;
import com.gmail.berndivader.volatilecode.VolatileHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class Main extends JavaPlugin {

	private static Main plugin;
	public static HealthbarHandler healthbarhandler;
	public static NMSUtils nmsutils;
	public static Integer wgVer;
	public static WorldGuardFlags wgf;
	public static FactionsFlags fflags;
	public static String mpNameVar = "mythicprojectile";
	public static String noTargetVar = "nottargetable";
	public static boolean hasRpgItems = false;
	public static Logger logger;
	public static PluginManager pluginmanager;
	public MythicMobs mythicmobs;
	public WorldGuardPlugin wg;
	private ThiefHandler thiefhandler;
	private MobManager mobmanager;
	private MythicPlayers mythicplayers;
	private MobArenaHandler maHandler;
	private VolatileHandler volatilehandler;

	@Override
	public void onEnable() {
		plugin = this;
		pluginmanager = plugin.getServer().getPluginManager();
		logger = plugin.getLogger();
		if (pluginmanager.getPlugin("MythicMobs") != null) {
			this.mythicmobs = MythicMobs.inst();
			this.mobmanager = this.mythicmobs.getMobManager();
			pluginmanager.registerEvents(new UndoBlockListener(), this);
			new CustomSkillStuff();
			new mmCustomSkills26();
			this.thiefhandler = new ThiefHandler();
			logger.info("Found MythicMobs, registered CustomSkills.");
			new mmOwnConditions();
			if (pluginmanager.isPluginEnabled("WorldGuard")) {
				wg = getWorldGuard();
				wgf = new WorldGuardFlags();
				new mmWorldGuardFlag();
			}
			if (pluginmanager.isPluginEnabled("Factions")
					&& pluginmanager.isPluginEnabled("MassiveCore")) {
				fflags = new FactionsFlags();
				new mmFactionsFlag();
			}
			if (pluginmanager.getPlugin("RPGItems") != null) {
				logger.info("RPGItems support enabled!");
				hasRpgItems = true;
			}
			if (pluginmanager.isPluginEnabled("MobArena")) {
				maHandler = new MobArenaHandler();
				new mmMobArenaConditions();
			}
			if (pluginmanager.isPluginEnabled("HolographicDisplays")) {
				logger.info("HolographicDisplays support enabled!");
				new HealthbarHandler(this);
			}
			Main.nmsutils = new NMSUtils();
			this.volatilehandler = this.getVolatileHandler();
			this.mythicplayers = new MythicPlayers(this);
			logger.info("registered MythicPlayers!");
			new NaNpatch();
			logger.info("NaN patch applied!");
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTask(this.thiefhandler.taskid.getTaskId());
		this.thiefhandler = null;
		this.mythicplayers = null;
		this.mythicmobs = null;
		this.maHandler = null;
		this.volatilehandler = null;
		this.wg = null;
		Main.wgf = null;
		Main.fflags = null;
		pluginmanager.disablePlugin(this);
	}

	public static Main getPlugin() {
		return plugin;
	}

	public MythicMobs getMythicMobs() {
		return this.mythicmobs;
	}

	public MythicPlayers getMythicPlayers() {
		return this.mythicplayers;
	}

	public MobManager getMobManager() {
		return this.mobmanager;
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

	public NMSUtils getNMSUtils() {
		return Main.nmsutils;
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
