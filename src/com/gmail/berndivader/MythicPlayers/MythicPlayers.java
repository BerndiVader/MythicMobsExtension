package com.gmail.berndivader.MythicPlayers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class MythicPlayers {

	public static MythicMobs mythicmobs;
	private Plugin plugin;
	private static MythicPlayers core;
	private PlayerManager playermanager;

	public MythicPlayers(Plugin plugin) {
		core = this;
		mythicmobs = MythicMobs.inst();
		this.plugin = plugin;
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
			Bukkit.getLogger().info("Found MythicMobs, register MythicPlayers.");
			this.playermanager = new PlayerManager(this);
			this.plugin.getServer().getPluginManager().registerEvents(new MythicPlayerMythicMobsLoadEvent(),
					this.plugin);
		}
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
