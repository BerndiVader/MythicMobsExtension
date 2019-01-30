package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

import org.bukkit.Location;

import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.quicksort.QuickSort;
import com.gmail.berndivader.mythicmobsext.utils.quicksort.QuickSortPair;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public 
abstract 
class 
ISelectorEntity
extends
IEntitySelector {
	
    private boolean sort_by_distance,nearest;
    String filter_faction[];
    int length;
	
	public ISelectorEntity(MythicLineConfig mlc) {
		super(mlc);
        this.sort_by_distance=mlc.getBoolean("sortbydistance",false);
        this.nearest=mlc.getBoolean("nearest");
        this.filter_faction=mlc.getString("filterfaction",new String()).split(",");
        this.length=mlc.getInteger("length",32);
	}
	
	@Override
    public void filter(SkillMetadata data, boolean targetCreative) {
		super.filter(data,targetCreative);
        if(this.sort_by_distance&&data.getEntityTargets().size()>0) {
        	int size=data.getEntityTargets().size();
        	Location source=data.getCaster().getEntity().getBukkitEntity().getLocation();
        	QuickSortPair[]pairs=new QuickSortPair[size];
        	Iterator<AbstractEntity>it1=data.getEntityTargets().iterator();
        	for(int i1=0;i1<size;i1++) {
        		AbstractEntity e=it1.next();
        		double distance=source.distance(e.getBukkitEntity().getLocation());
        		pairs[i1]=new QuickSortPair(distance,e);
        	}
        	pairs=QuickSort.sort(pairs,0,pairs.length-1);
        	HashSet<AbstractEntity>sorted_targets=new HashSet<>();
        	for(int i1=0;i1<size;i1++) {
        		sorted_targets.add((AbstractEntity)pairs[i1].object);
        	}
        	data.setEntityTargets(sorted_targets);
        }
        Iterator<AbstractEntity>it=data.getEntityTargets().iterator();
		if(filter_faction.length>0&&filter_faction[0].length()>0) {
			while(it.hasNext()) {
            	AbstractEntity e=it.next();
            	if(e==null) {
            		it.remove();
            		continue;
            	}
            	Optional<ActiveMob>optional=Utils.mobmanager.getActiveMob(e.getUniqueId());
            	if(optional.isPresent()) {
            		String faction_name=optional.get().getFaction();
            		for(int i1=0;i1<filter_faction.length;i1++) {
                    	if(faction_name!=null&&faction_name.equals(filter_faction[i1])) it.remove();
            		}
            	}
            }
		}
        if(this.nearest) {
            Location caster_location=data.getCaster().getEntity().getBukkitEntity().getLocation();
            AbstractEntity nearest=null;
            while(it.hasNext()) {
            	AbstractEntity e=it.next();
            	if(e==null) {
            		it.remove();
            		continue;
            	}
   				if(nearest==null||caster_location.distance(e.getBukkitEntity().getLocation())<caster_location.distance(nearest.getBukkitEntity().getLocation())) {
   					nearest=e;
    			}
            }
            if(nearest!=null) {
            	data.getEntityTargets().clear();
            	data.getEntityTargets().add(nearest);
            }
        }
	}
}
