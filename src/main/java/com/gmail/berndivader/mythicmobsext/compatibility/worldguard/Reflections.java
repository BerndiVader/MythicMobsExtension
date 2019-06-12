package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

class
Reflections
{
	static int version;
	
	static Class<?>class_WorldGuardPlugin;
	static Class<?>class_WorldGuard;
	static Class<?>class_FlagRegistry;
	static Class<?>class_RegionContainer;
	static Class<?>class_RegionManager;
	static Class<?>class_ApplicableRegionSet;
	static Class<?>class_EntityType;
	
	static Method class_WorldGuardPlugin_getFlagRegistry;
	static Method class_WorldGuard_getRegionContainer;
	static Method class_RegionContainer_getRegionManager;
	static Method class_RegionManager_getApplicableRegions;
	static Method class_RegionManager_getRegion;
	static Method class_ApplicationRegionSet_getRegions;
	static Method class_EntityType_getName;

	static Object worldguard,instance,flag_registry;
	
	static {
		instance=Main.pluginmanager.getPlugin("WorldGuard");
		try(Scanner s=new Scanner(((Plugin)instance).getResource("plugin.yml")).useDelimiter("\\A")){
			while(s.hasNext()) {
				String s1=s.nextLine();
				if(s1.startsWith("version")) {
					version=Integer.parseInt(s1.substring(10,11));
					break;
				}
			}
		};
		try {
			load_classes();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		if(version>6) {
			try {
				instance=class_WorldGuardPlugin.getMethod("getInstance").invoke(instance);
				worldguard=class_WorldGuardPlugin.getMethod("getPlatform").invoke(instance);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		} else {
			worldguard=instance;
		}
		try {
			flag_registry=class_WorldGuardPlugin.getMethod("getFlagRegistry").invoke(instance);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
    static void load_classes() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
    	ClassLoader class_loader=Reflections.class.getClassLoader();
    	class_FlagRegistry=class_loader.loadClass("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");
    	class_RegionManager=class_loader.loadClass("com.sk89q.worldguard.protection.managers.RegionManager");
    	class_ApplicableRegionSet=class_loader.loadClass("com.sk89q.worldguard.protection.ApplicableRegionSet");
    	
    	switch(version) {
    	case 6:
    		class_WorldGuardPlugin=class_loader.loadClass("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
    		class_WorldGuard=class_WorldGuardPlugin;
        	class_RegionContainer=class_loader.loadClass("com.sk89q.worldguard.bukkit.RegionContainer");
        	class_RegionContainer_getRegionManager=class_RegionContainer.getMethod("get",World.class);
        	class_EntityType=class_loader.loadClass("org.bukkit.entity.EntityType");
    		break;
    	case 7:
    		class_WorldGuardPlugin=class_loader.loadClass("com.sk89q.worldguard.WorldGuard");
    		class_WorldGuard=class_loader.loadClass("com.sk89q.worldguard.internal.platform.WorldGuardPlatform");
        	class_RegionContainer=class_loader.loadClass("com.sk89q.worldguard.protection.regions.RegionContainer");
        	class_RegionContainer_getRegionManager=class_RegionContainer.getMethod("get",com.sk89q.worldedit.world.World.class);
        	class_EntityType=class_loader.loadClass("com.sk89q.worldedit.world.entity.EntityType");
    		break;
    	}
    	
		class_WorldGuardPlugin_getFlagRegistry=class_WorldGuardPlugin.getMethod("getFlagRegistry");
    	class_WorldGuard_getRegionContainer=class_WorldGuard.getMethod("getRegionContainer");
    	class_RegionManager_getApplicableRegions=class_RegionManager.getMethod("getApplicableRegions",Vector.class);
    	class_RegionManager_getRegion=class_RegionManager.getMethod("getRegion",String.class);
    	class_ApplicationRegionSet_getRegions=class_ApplicableRegionSet.getMethod("getRegions");
    	class_EntityType_getName=class_EntityType.getMethod("getName");
    }
    
	public static Object getFlagRegistry() {
		return flag_registry;
	}
	
	public static Object getApplicableRegions(World world,Vector vector) {
		Object region_manager=null;
		Object applicable_region_set=null;		
		Object regions=null;
		try {
			region_manager=class_RegionContainer_getRegionManager.invoke(class_WorldGuard_getRegionContainer.invoke(worldguard),version>6?new BukkitWorld(world):world);
			if(region_manager!=null) {
				applicable_region_set=class_RegionManager_getApplicableRegions.invoke(region_manager,vector);
				if(applicable_region_set!=null) {
					regions=class_ApplicationRegionSet_getRegions.invoke(applicable_region_set);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return regions;
	}
	
	public static Object getRegion(World world,String region_name) {
		Object region_manager=null;
		Object region=null;
		try {
			region_manager=class_RegionContainer_getRegionManager.invoke(class_WorldGuard_getRegionContainer.invoke(worldguard),version>6?new BukkitWorld(world):world);
			if(region_manager!=null) region=class_RegionManager_getRegion.invoke(region_manager,region_name);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return region;
	}
	
	public static String class_EntityType_getName(Object entity_type) {
		String s1=new String();
		try {
			s1=(String)class_EntityType_getName.invoke(entity_type);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		if(s1.startsWith("minecraft:"))s1=s1.substring(10);
		return s1;
	}
	
}