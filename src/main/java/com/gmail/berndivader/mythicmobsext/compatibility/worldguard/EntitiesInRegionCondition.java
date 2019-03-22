package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.BoundingBox;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public
class 
EntitiesInRegionCondition 
extends 
AbstractCustomCondition 
implements
ILocationCondition 
{
	
	List<String>region_names;
	List<String>region_names_exclude;
	List<EntityType>types;
	boolean debug,use_priority,all_types;
	
	public EntitiesInRegionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		region_names=Arrays.asList(mlc.getString(new String[] {"regions","region","r"},new String()).split(","));
		region_names_exclude=Arrays.asList(mlc.getString("exclude","__global__").split(","));
		String temp[]=mlc.getString("types","ANY").toUpperCase().split(",");
		if(!(all_types=temp[0].equals("ANY"))) {
			for(String s1:temp) {
			}
		}
		use_priority=mlc.getBoolean("usepriority",false);
	}

	@Override
	public boolean check(AbstractLocation al) {
		Set<ProtectedRegion>regions=WorldGuardUtils.getRegionsByLocation(BukkitAdapter.adapt(al));
		if(regions!=null) {
			World world=BukkitAdapter.adapt(al).getWorld();
			regions=regions.stream().filter(r->!region_names_exclude.contains(r.getId())).collect(Collectors.toSet());
			if(use_priority) {
				ProtectedRegion region=regions.stream().max(Comparator.comparing(ProtectedRegion::getPriority)).get();
				if(region_names.contains(region.getId())||region_names.contains("ANY")) {
					BlockVector min_point=region.getMinimumPoint();
					BlockVector max_point=region.getMaximumPoint();
					BoundingBox box=new BoundingBox(min_point.getX(),min_point.getY(),min_point.getZ(),max_point.getX(),max_point.getY(),max_point.getZ());
				}
			} else {
				for(ProtectedRegion region:regions) {
					if(region_names.contains(region.getId())) return true;
				}
			}
		}
		return false;
	}

}
