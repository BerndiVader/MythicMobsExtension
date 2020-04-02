package com.gmail.berndivader.MythicPlayers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class MythicPlayers {

	static String pluginName = "MythicPlayers";

	public static MythicMobs mythicmobs;
	private Plugin plugin;
	private static MythicPlayers core;
	private PlayerManager playermanager;

	public MythicPlayers(Plugin plugin) {
		core = this;
		mythicmobs = MythicMobs.inst();
		this.plugin = plugin;
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
			this.playermanager = new PlayerManager(this);
			this.plugin.getServer().getPluginManager().registerEvents(new MythicPlayerMythicMobsLoadEvent(),
					this.plugin);
		}
		Main.logger.info("using " + pluginName);
	}

	public static MythicPlayers inst() {
		return core;
	}

	public PlayerManager getPlayerManager() {
		return playermanager;
	}

	public Plugin plugin() {
		return this.plugin;
	}

}
