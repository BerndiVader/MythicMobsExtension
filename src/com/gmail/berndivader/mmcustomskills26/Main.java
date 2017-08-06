package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.garbagemule.MobArena.MobArenaHandler;
import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.NMS.NMSUtils;
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
	public static NMSUtils nmsutils;
	public static Integer wgVer;
	public static WorldGuardFlags wgf;
	public static FactionsFlags fflags;
	public static String mpNameVar = "mythicprojectile";
	public static String noTargetVar = "nottargetable";
	public static boolean hasRpgItems = false;
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
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
			this.mythicmobs = MythicMobs.inst();
			this.mobmanager = this.mythicmobs.getMobManager();
			PluginManager pm = this.getServer().getPluginManager();
			pm.registerEvents(new UndoBlockListener(), this);
			new CustomSkillStuff();
			new mmCustomSkills26();
			this.thiefhandler = new ThiefHandler();
			Bukkit.getLogger().info("Found MythicMobs, registered CustomSkills.");
			new mmOwnConditions();
			if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
				wg = getWorldGuard();
				wgf = new WorldGuardFlags();
				new mmWorldGuardFlag();
			}
			if (Bukkit.getPluginManager().isPluginEnabled("Factions")
					&& Bukkit.getPluginManager().isPluginEnabled("MassiveCore")) {
				fflags = new FactionsFlags();
				new mmFactionsFlag();
			}
			if (Bukkit.getServer().getPluginManager().getPlugin("RPGItems") != null) {
				Bukkit.getLogger().info("RPGItems support enabled!");
				hasRpgItems = true;
			}
			if (Bukkit.getPluginManager().isPluginEnabled("MobArena")) {
				maHandler = new MobArenaHandler();
				new mmMobArenaConditions();
			}
			Main.nmsutils = new NMSUtils();
			this.volatilehandler = this.getVolatileHandler();
			this.mythicplayers = new MythicPlayers(this);
			Bukkit.getLogger().info("registered MythicPlayers!");
			new NaNpatch();
			Bukkit.getLogger().info("NaN patch applied!");
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTask(this.thiefhandler.taskid.getTaskId());
		this.thiefhandler = null;
		this.mythicplayers = null;
		this.mythicmobs = null;
		this.maHandler = null;
		this.wg = null;
		Main.wgf = null;
		Main.fflags = null;
		this.getServer().getPluginManager().disablePlugin(this);
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
		return (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
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
        		Bukkit.getLogger().warning("Server version not supported!");
        	}
        	ex.printStackTrace();
        }
        return vh;
    }
	
}
