package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
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
	String types[];
	boolean debug,use_priority,all_types,all_regions;
	Predicate<Entity>entities;
	RangedDouble amount;
	
	public EntitiesInRegionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		amount=new RangedDouble(mlc.getString("amount",">0"));
		region_names=Arrays.asList(mlc.getString(new String[] {"regions","region","r"},new String()).toUpperCase().split(","));
		region_names_exclude=Arrays.asList(("__global__,"+mlc.getString("exclude",new String())).toUpperCase().split(","));
		types=mlc.getString("types","ANY").toUpperCase().split(",");
		all_types=types[0].equals("ANY");
		all_regions=region_names.contains("ANY");
		use_priority=mlc.getBoolean("usepriority",false);
		entities=entity->{
			if(all_types) return true;
			int size=types.length;
			for(int i1=0;i1<size;i1++) {
				String type=types[i1];
				if(type.startsWith("MYTHICMOB")) {
					String temp[]=type.split(":");
					if(temp.length>1) {
						String mythicmob_type=temp[1];
						if (mythicmob_type.equals("ANY")&&Utils.mobmanager.isActiveMob(entity.getUniqueId())) return true;
						ActiveMob am=Utils.mobmanager.getMythicMobInstance(entity);
						if(am!=null&&am.getType().getInternalName().toUpperCase().equals(mythicmob_type)) return true;
					}
				} else {
					switch(type) {
						case "ENTITY":
							return entity instanceof Entity;
						case "LIVING":
							return entity instanceof LivingEntity;
						case "PLAYER":
							return entity instanceof Player;
						case "ANIMAL":
							return entity instanceof Animals;
						case "MONSTER":
							return entity instanceof Monster;
						case "CREATURE":
							return entity instanceof Creature;
						default:
							if(entity.getClass().getSimpleName().toUpperCase().contains(type)) return true;
							break;
					}
				}
			}
			return false;
		};
	}

	@Override
	public boolean check(AbstractLocation al) {
		Set<ProtectedRegion>regions_set=WorldGuardUtils.getRegionsByLocation(BukkitAdapter.adapt(al));
		int size=0;
		if(regions_set!=null) {
			World world=BukkitAdapter.adapt(al).getWorld();
			List<ProtectedRegion>regions=regions_set.stream().filter(r->!region_names_exclude.contains(r.getId().toUpperCase())).collect(Collectors.toList());
			if(use_priority) {
				Optional<ProtectedRegion>maybe=regions.stream().max(Comparator.comparing(ProtectedRegion::getPriority));
				if(maybe.isPresent()) {
					ProtectedRegion region=maybe.get();
					if(all_regions||region_names.contains(region.getId().toUpperCase())) {
						size+=sumEntityInRegion(world,region);
					}
				}
			} else {
				if(!all_regions) {
					regions=regions.stream().filter(r->region_names.contains(r.getId().toUpperCase())).collect(Collectors.toList());
				}
				size+=sumEntityInRegions(world,regions);
			}
		}
		return amount.equals(size);
	}
	
	int sumEntityInRegion(World world,ProtectedRegion region) {
//		BlockVector min_point=region.getMinimumPoint();
//		BlockVector max_point=region.getMaximumPoint();
//		BoundingBox box=new BoundingBox(min_point.getX(),min_point.getY(),min_point.getZ(),max_point.getX(),max_point.getY(),max_point.getZ());
//		return world.getNearbyEntities(box,entities).size();
		return 0;
	}
	
	int sumEntityInRegions(World world,List<ProtectedRegion>regions) {
		if(regions.isEmpty()) return 0;
		ProtectedRegion first_region=regions.get(0);
//		BlockVector min_point=first_region.getMinimumPoint();
//		BlockVector max_point=first_region.getMaximumPoint();
//		double min_x=min_point.getX(),min_y=min_point.getY(),min_z=min_point.getZ(),max_x=max_point.getX(),max_y=max_point.getY(),max_z=max_point.getZ();
		int size=regions.size();
		for(int i1=1;i1<size;i1++) {
			ProtectedRegion region=regions.get(i1);
//			min_point=region.getMinimumPoint();
//			max_point=region.getMaximumPoint();
//			if(min_point.getX()<min_x) min_x=min_point.getX();
//			if(min_point.getY()<min_y) min_y=min_point.getY();
//			if(min_point.getZ()<min_z) min_z=min_point.getZ();
//			if(max_point.getX()>max_x) max_x=max_point.getX();
//			if(max_point.getY()>max_y) max_y=max_point.getY();
//			if(max_point.getZ()>max_z) max_z=max_point.getZ();
		}
//		BoundingBox box=new BoundingBox(min_x,min_y,min_z,max_x,max_y,max_z);
//		return world.getNearbyEntities(box,entities).size();
		return 0;
	}
}
