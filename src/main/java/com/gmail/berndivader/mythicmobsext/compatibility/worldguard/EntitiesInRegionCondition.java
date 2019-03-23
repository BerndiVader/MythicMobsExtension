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
import org.bukkit.util.BoundingBox;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.sk89q.worldedit.BlockVector;
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
	boolean debug,use_priority,all_types;
	Predicate<Entity>entities;
	RangedDouble amount;
	
	public EntitiesInRegionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		amount=new RangedDouble(mlc.getString("amount",">0"));
		region_names=Arrays.asList(mlc.getString(new String[] {"regions","region","r"},new String()).toUpperCase().split(","));
		region_names_exclude=Arrays.asList(("__global__,"+mlc.getString("exclude",new String())).toUpperCase().split(","));
		types=mlc.getString("types","ANY").toUpperCase().split(",");
		all_types=types[0].equals("ANY");
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
		Set<ProtectedRegion>regions=WorldGuardUtils.getRegionsByLocation(BukkitAdapter.adapt(al));
		int size=0;
		if(regions!=null) {
			World world=BukkitAdapter.adapt(al).getWorld();
			regions=regions.stream().filter(r->!region_names_exclude.contains(r.getId().toUpperCase())).collect(Collectors.toSet());
			if(use_priority) {
				Optional<ProtectedRegion>maybe=regions.stream().max(Comparator.comparing(ProtectedRegion::getPriority));
				if(maybe.isPresent()) {
					ProtectedRegion region=maybe.get();
					if(region_names.contains(region.getId().toUpperCase())||region_names.contains("ANY")) {
						size+=sumEntityInRegion(world,region);
					}
				}
			} else {
				for(ProtectedRegion region:regions) {
					if(region_names.contains(region.getId().toUpperCase())) {
						size+=sumEntityInRegion(world,region);
					}
				}
			}
		}
		return amount.equals(size);
	}
	
	int sumEntityInRegion(World world,ProtectedRegion region) {
		BlockVector min_point=region.getMinimumPoint();
		BlockVector max_point=region.getMaximumPoint();
		BoundingBox box=new BoundingBox(min_point.getX(),min_point.getY(),min_point.getZ(),max_point.getX(),max_point.getY(),max_point.getZ());
		return world.getNearbyEntities(box,entities).size();
	}

}
