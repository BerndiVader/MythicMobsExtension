/*
Internal Scripts file. Will be rewritten all the time. Better place your 
scripts into Includes.js or use load in Includes.js to load them.
*/


load("nashorn:mozilla_compat.js");
var Bukkit = org.bukkit.Bukkit;
var mythicmobs = Bukkit.getPluginManager().getPlugin("MythicMobs");
var mythicmobsext = Bukkit.getPluginManager().getPlugin("MythicMobsExtension");
var path=mythicmobsext.getDataFolder()+java.io.File.separator;

load(path+"Includes.js");
