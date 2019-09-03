package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
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
	static Class<?>class_Vector;
	static Class<?>class_BlockVector;
	static Class<?>class_ProtectedRegion;
	
	static Method class_WorldGuardPlugin_getFlagRegistry;
	static Method class_WorldGuard_getRegionContainer;
	static Method class_RegionContainer_getRegionManager;
	static Method class_RegionManager_getApplicableRegions;
	static Method class_RegionManager_getRegion;
	static Method class_ApplicationRegionSet_getRegions;
	static Method class_EntityType_getName;
	static Method class_Vector_at;
	static Method class_BlockVector_getX;
	static Method class_BlockVector_getY;
	static Method class_BlockVector_getZ;
	static Method class_ProtectedRegion_getMinimumPoint;
	static Method class_ProtectedRegion_getMaximumPoint;
	
	
	static Constructor<?> class_Vector_constructor;

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
    	class_ProtectedRegion=class_loader.loadClass("com.sk89q.worldguard.protection.regions.ProtectedRegion");
    	class_ProtectedRegion_getMinimumPoint=class_ProtectedRegion.getMethod("getMinimumPoint");
    	class_ProtectedRegion_getMaximumPoint=class_ProtectedRegion.getMethod("getMaximumPoint");
   	
    	switch(version) {
    	case 6:
    		class_WorldGuardPlugin=class_loader.loadClass("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
    		class_WorldGuard=class_WorldGuardPlugin;
        	class_RegionContainer=class_loader.loadClass("com.sk89q.worldguard.bukkit.RegionContainer");
        	class_RegionContainer_getRegionManager=class_RegionContainer.getMethod("get",World.class);
        	class_EntityType=class_loader.loadClass("org.bukkit.entity.EntityType");
        	class_Vector=class_loader.loadClass("com.sk89q.worldedit.Vector");
        	class_BlockVector=class_loader.loadClass("com.sk89q.worldedit.BlockVector");
        	
        	class_Vector_constructor=class_Vector.getConstructor(double.class,double.class,double.class);
        	
        	class_BlockVector_getX=class_BlockVector.getMethod("getX");
        	class_BlockVector_getX=class_BlockVector.getMethod("getY");
        	class_BlockVector_getX=class_BlockVector.getMethod("getZ");
        	
    		break;
    	case 7:
    		class_WorldGuardPlugin=class_loader.loadClass("com.sk89q.worldguard.WorldGuard");
    		class_WorldGuard=class_loader.loadClass("com.sk89q.worldguard.internal.platform.WorldGuardPlatform");
        	class_RegionContainer=class_loader.loadClass("com.sk89q.worldguard.protection.regions.RegionContainer");
        	class_RegionContainer_getRegionManager=class_RegionContainer.getMethod("get",com.sk89q.worldedit.world.World.class);
        	class_EntityType=class_loader.loadClass("com.sk89q.worldedit.world.entity.EntityType");
        	class_Vector=class_loader.loadClass("com.sk89q.worldedit.math.Vector3");
        	class_BlockVector=class_loader.loadClass("com.sk89q.worldedit.math.BlockVector3");
        	
        	class_Vector_at=class_BlockVector.getMethod("at",double.class,double.class,double.class);
        	
        	class_BlockVector_getX=class_BlockVector.getMethod("getX");
        	class_BlockVector_getY=class_BlockVector.getMethod("getY");
        	class_BlockVector_getZ=class_BlockVector.getMethod("getZ");
        	
    		break;
    	}
    	
		class_WorldGuardPlugin_getFlagRegistry=class_WorldGuardPlugin.getMethod("getFlagRegistry");
    	class_WorldGuard_getRegionContainer=class_WorldGuard.getMethod("getRegionContainer");
    	class_RegionManager_getApplicableRegions=class_RegionManager.getMethod("getApplicableRegions",class_BlockVector);
    	class_RegionManager_getRegion=class_RegionManager.getMethod("getRegion",String.class);
    	class_ApplicationRegionSet_getRegions=class_ApplicableRegionSet.getMethod("getRegions");
    	class_EntityType_getName=class_EntityType.getMethod("getName");
    }
    
	public static Object getFlagRegistry() {
		return flag_registry;
	}
	
	public static Object getApplicableRegions(World world,Vec3D vector) {
		Object vector_object=null;
		if(version==6) {
			try {
				vector_object=class_Vector_constructor.newInstance(vector.getX(),vector.getY(),vector.getZ());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}else {
			try {
				vector_object=class_Vector_at.invoke(null,vector.getX(),vector.getY(),vector.getZ());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		Object region_manager=null;
		Object applicable_region_set=null;		
		Object regions=null;
		try {
			region_manager=class_RegionContainer_getRegionManager.invoke(class_WorldGuard_getRegionContainer.invoke(worldguard),version>6?new BukkitWorld(world):world);
			if(region_manager!=null) {
				applicable_region_set=class_RegionManager_getApplicableRegions.invoke(region_manager,vector_object);
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
	
	public static Vec3D getMinimumPoint(Object protected_region) {
		Object vector_object=null;
		double dx=0,dy=0,dz=0;
		try {
			vector_object=class_ProtectedRegion_getMinimumPoint.invoke(protected_region);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		try {
			dx=(int)class_BlockVector_getX.invoke(vector_object);
			dy=(int)class_BlockVector_getY.invoke(vector_object);
			dz=(int)class_BlockVector_getZ.invoke(vector_object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Vec3D(dx,dy,dz);
	}
	
	public static Vec3D getMaximumPoint(Object protected_region) {
		Object vector_object=null;
		double dx=0,dy=0,dz=0;
		try {
			vector_object=class_ProtectedRegion_getMaximumPoint.invoke(protected_region);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		try {
			dx=(int)class_BlockVector_getX.invoke(vector_object);
			dy=(int)class_BlockVector_getY.invoke(vector_object);
			dz=(int)class_BlockVector_getZ.invoke(vector_object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Vec3D(dx,dy,dz);
	}
	
	
}