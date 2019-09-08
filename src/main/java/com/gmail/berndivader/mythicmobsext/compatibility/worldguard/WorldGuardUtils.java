package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Set;

import org.bukkit.Location;

import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public
class
WorldGuardUtils 
{
	static FlagRegistry flag_registery;
	static String global_name="__global__";
	
	static {
		flag_registery=(FlagRegistry)Reflections.getFlagRegistry();
	}
	
	/**
	 * @param location - location to collect regions from
	 * @return Set<ProtectedRegion> - set of applicable regions
	 */
	static Set<ProtectedRegion> getRegionsByLocation(Location location) {
		Set<ProtectedRegion>regions=(Set<ProtectedRegion>)Reflections.getApplicableRegions(location.getWorld(),new Vec3D(location.getX(),location.getY(),location.getZ()));
		regions.add((ProtectedRegion)Reflections.getRegion(location.getWorld(), global_name));
		return regions;
	}
	
	/**
	 * @param location - location to check
	 * @param flag_name - name of the flag
	 * @param args - list goes in there if its a SetFlag
	 * @return boolean
	 */
	public static boolean checkFlagAtLocation(Location location, String flag_name, String args) {
		Flag<?>flag=flag_registery.get(flag_name);
		Set<ProtectedRegion>regions=(Set<ProtectedRegion>)Reflections.getApplicableRegions(location.getWorld(),new Vec3D(location.getX(),location.getY(),location.getZ()));
		if(regions.size()==0) regions.add((ProtectedRegion)Reflections.getRegion(location.getWorld(), global_name));
		boolean bl1=false;
		switch(flag.getClass().getSimpleName()) {
			case"StateFlag":
				for(ProtectedRegion region:regions) {
					State state=region.getFlag((StateFlag)flag);
					bl1=state!=null&&state==State.ALLOW?true:false;
				}
				break;
			case"SetFlag":
				switch(((SetFlag<?>)flag).getType().getClass().getSimpleName()) {
					case"EntityTypeFlag":
						for(ProtectedRegion region:regions) {
							Set<Object>entitytypes=(Set<Object>)region.getFlag(flag);
							if (entitytypes==null) continue;
							for(Object o1:entitytypes) {
								String arr[]=args.split(",");
								for(int i1=0;i1<arr.length;i1++) {
									if(arr[i1].equals(Reflections.class_EntityType_getName(o1))) return true;
								}
							}
						}
						break;
					}
				break;
			case"StringFlag":
				for(ProtectedRegion region:regions) {
					String string=region.getFlag((StringFlag)flag);
					if(string.equals(args)) return true;
				}
			break;
		}
		return bl1;
	}
	
	public static boolean checkRegionStateFlagAtLocation(Location l, String f) {
		return checkFlagAtLocation(l,f,"");
	}

	public static boolean checkRegionDenySpawnFlagAtLocation(Location l, String entitylist) {
		return checkFlagAtLocation(l,"deny-spawn",entitylist);
	}
	
}
