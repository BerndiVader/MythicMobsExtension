package com.gmail.berndivader.mmcustomskills26;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class Main extends JavaPlugin {
	
	private BukkitTask taskid;
	private static ThiefHandler thiefhandler = new ThiefHandler();
	private Iterator<Thief> ti;

	private static Plugin plugin;
	public static MythicMobs mm;
	
	@Override
	public void onEnable() {
		plugin = this;
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
			mm = MythicMobs.inst();
			getServer().getPluginManager().registerEvents(new UndoBlockListener(), this);
			getServer().getPluginManager().registerEvents(new mmCustomSkills26(), this);
			getServer().getPluginManager().registerEvents(new ThiefDamageEvent(), this);

			taskid = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
	    		public void run() {
	    			ti = thiefhandler.getThiefs().iterator();
	    			while (ti.hasNext()) {
	    				Thief thief = ti.next();if (!mm.getMobManager().isActiveMob(thief.getUuid())) {ti.remove();}}
	    		}}, 1200L, 1200L);
		}
	}
	
	@Override
	public void onDisable() {
    	Bukkit.getServer().getScheduler().cancelTask(taskid.getTaskId());
    	thiefhandler = null;
	}
	public static Plugin getPlugin() {return plugin;}
	public static Set<Thief> getThiefs() {return thiefhandler.getThiefs();}
	public static ThiefHandler thiefhandler() {return thiefhandler;}
}
