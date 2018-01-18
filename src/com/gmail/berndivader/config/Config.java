package com.gmail.berndivader.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Config {

	public static ConfigurationSection UN;
	public static ConfigurationSection NaN;
	public static ConfigurationSection M_Players;
	public static ConfigurationSection M_Thiefs;
	public static ConfigurationSection M_Parrot;
    public static ConfigurationSection CO;
	public static ConfigurationSection WG;
	public static ConfigurationSection HD;

	public static void load(Plugin plugin) {
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
		    plugin.getLogger().info("Generating config.yml...");
			plugin.saveDefaultConfig();
		}

		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			plugin.getLogger().warning("The configuration is not a valid YAML file! Please check it with a tool like http://yaml-online-parser.appspot.com/");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().warning("Error while reading the file. Is it in use?");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getLogger().warning("Unhandled exception while reading the configuration!");
			return;
		}

		UN = config.getConfigurationSection("Configuration.UpdateNotification");
        NaN = config.getConfigurationSection("Configuration.Patches.NaN_Patch");
        M_Players = config.getConfigurationSection("Configuration.Modules.Mythic_Players");
        M_Thiefs = config.getConfigurationSection("Configuration.Modules.Mythic_Thiefs");
        CO = config.getConfigurationSection("Configuration.Modules.Cached_Owners");
        M_Parrot = config.getConfigurationSection("Configuration.Entities.Mythic_Parrot");
        WG = config.getConfigurationSection("Configuration.Compatibility.Worldguard");
        HD = config.getConfigurationSection("Configuration.Compatibility.Holographic_Displays");
	}

}
