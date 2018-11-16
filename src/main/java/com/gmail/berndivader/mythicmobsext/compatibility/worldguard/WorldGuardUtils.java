package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.gmail.berndivader.mythicmobsext.Main;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
	static WorldGuardPlugin worldguard;
	static FlagRegistry flag_registery;
	static String global_name="__global__";
	
	static {
		worldguard=(WorldGuardPlugin)Main.pluginmanager.getPlugin("WorldGuard");
		flag_registery=worldguard.getFlagRegistry();
	}
	
	/**
	 * @param location - location to check
	 * @param flag_name - name of the flag
	 * @param args - list goes in there if its a SetFlag
	 * @return boolean
	 */
	public static boolean checkFlagAtLocation(Location location, String flag_name, String args) {
		Flag<?>flag=flag_registery.get(flag_name);
		Set<ProtectedRegion>regions=worldguard.getRegionContainer().get(location.getWorld()).getApplicableRegions(new Vector(location.getX(),location.getY(),location.getZ())).getRegions();
		if(regions.size()==0) regions.add(worldguard.getRegionContainer().get(location.getWorld()).getRegion(global_name));
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
						EntityType bukkit_entity_type;
						for(ProtectedRegion region:regions) {
							Set<EntityType>entitytypes=(Set<EntityType>)region.getFlag(flag);
							if (entitytypes==null) continue;
							for(String type:args.split(",")) {
								try {
									bukkit_entity_type=EntityType.valueOf(type);
								} catch (Exception ex) {
									continue;
								}
								if (bukkit_entity_type!=null&&entitytypes.contains(bukkit_entity_type)) return true;
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
