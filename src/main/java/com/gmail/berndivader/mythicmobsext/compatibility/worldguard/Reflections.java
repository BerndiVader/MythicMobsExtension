package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.lang.reflect.Method;
import java.util.Scanner;

import com.gmail.berndivader.mythicmobsext.Main;

@SuppressWarnings("resource")
class
Reflections
{
	static int version;
	
	static Class<?>class_WorldGuard;
	static Class<?>class_WorldGuardPlugin;
	static Class<?>class_FlagRegistry;
	static Class<?>class_Flag;
	static Class<?>class_DefaultFlag;
	static Class<?>class_StateFlag;
	static Class<?>class_RegionManager;
	static Class<?>class_ApplicableRegionSet;
	
	static Method class_WorldGuard_getFlagRegistry;
	static Method class_WorldGuardPlugin_getFlagRegistry;
	static Method class_DefaultFlag_fuzzyMatchFlag;
	
	
	static {
		Scanner s=new Scanner(Main.pluginmanager.getPlugin("WorldGuard").getResource("plugin.yml")).useDelimiter("\\A");
		while(s.hasNext()) {
			String s1=s.nextLine();
			if(s1.startsWith("version")) {
				version=Integer.parseInt(s1.substring(10,11));
				break;
			}
		}
		
		try {
			load_classes();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

	}
	
    static void load_classes() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
    	ClassLoader class_loader=Reflections.class.getClassLoader();
    	
    	class_WorldGuardPlugin=class_loader.loadClass("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
    	class_FlagRegistry=class_loader.loadClass("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
    	class_Flag=class_loader.loadClass("com.sk89q.worldguard.protection.flags.Flag");
    	class_StateFlag=class_loader.loadClass("com.sk89q.worldguard.protection.flags.StateFlag");
    	class_RegionManager=class_loader.loadClass("com.sk89q.worldguard.protection.managers.RegionManager");
    	class_ApplicableRegionSet=class_loader.loadClass("com.sk89q.worldguard.protection.ApplicableRegionSet");
    	
    	switch(version) {
    	case 6:
        	class_DefaultFlag=class_loader.loadClass("com.sk89q.worldguard.protection.flags.DefaultFlag");
    		
			class_DefaultFlag_fuzzyMatchFlag=class_DefaultFlag.getMethod("fuzzyMatchFlag",class_FlagRegistry,String.class);
			class_WorldGuardPlugin_getFlagRegistry=class_WorldGuardPlugin.getMethod("getFlagRegistry");
    		break;
    	case 7:
    		class_WorldGuard=class_loader.loadClass("com.sk89q.worldguard.bukkit.WorldGuard");
    		break;
    	}
    	
    }	
    
	
	public static void getFlagByString(String flag_name) {
	}
	
}