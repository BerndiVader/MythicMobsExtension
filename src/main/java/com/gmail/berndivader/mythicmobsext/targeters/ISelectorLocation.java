package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;
import com.gmail.berndivader.mythicmobsext.utils.quicksort.QuickSort;
import com.gmail.berndivader.mythicmobsext.utils.quicksort.QuickSortPair;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public 
abstract 
class 
ISelectorLocation
extends
ILocationSelector {
	
	List<FilterEnum> filters;
    int length;
    float yaw_offset,pitch_offset;
    double side_offset,forward_offset,y_offset;
    boolean use_relative;
	
	public ISelectorLocation(MythicLineConfig mlc) {
		filters=new ArrayList<FilterEnum>();
		String[]parse=mlc.getString("filter","").toUpperCase().split(",");
        if(mlc.getBoolean("sortbydistance",false)) filters.add(FilterEnum.SORTBYDISTANCE);
        if(mlc.getBoolean("nearest",false)) filters.add(FilterEnum.NEAREST);
		for(int i1=0;i1<parse.length;i1++) {
			FilterEnum filter=FilterEnum.get(parse[i1]);
			if(!filters.contains(filter)) filters.add(filter);
		}
        this.length=mlc.getInteger("length",32);
        this.yaw_offset=mlc.getFloat("yaw",0f);
        this.pitch_offset=mlc.getFloat("pitch",0f);
        this.y_offset=mlc.getDouble("y",0d);
        this.side_offset=mlc.getDouble("side",0d);
        this.forward_offset=mlc.getDouble("forwrd",0d);
        this.use_relative=forward_offset!=0d||side_offset!=0d;
	}
	
    public void filter(SkillMetadata data) {
    	for(int i1=0;i1<filters.size();i1++) {
    		switch(filters.get(i1)) {
    		case NEAREST:
    			nearest(data);
    			break;
    		case SORTBYDISTANCE:
    			sortByDistance(data);
    			break;
    		case SHUFFLE:
    			shuffle(data);
    			break;
    		case DEFAULT:
    		default:
				break;
    		}
    	}
	}
    
    public HashSet<AbstractLocation>applyOffsets(HashSet<AbstractLocation>targets) {
		if (this.use_relative) {
			targets.stream().forEach(abstract_location->{
		    	Location location=BukkitAdapter.adapt(abstract_location);
		    	float yaw=location.getYaw();
		    	float pitch=location.getPitch();
				Vector soV=MathUtils.getSideOffsetVectorFixed(yaw,this.side_offset,false);
				Vector foV=MathUtils.getFrontBackOffsetVector(location.getDirection(),this.forward_offset);
				abstract_location.add(soV.getX()+foV.getX(),this.y_offset,soV.getZ()+foV.getZ());
				abstract_location.setYaw(yaw+this.yaw_offset);
				abstract_location.setPitch(pitch+this.pitch_offset);
			});
		}
		return targets;
    }
    
    void sortByDistance(SkillMetadata data) {
    	Location source=data.getCaster().getEntity().getBukkitEntity().getLocation();
    	AbstractLocation[]locations=data.getLocationTargets().toArray(new AbstractLocation[data.getLocationTargets().size()]);
    	int size=locations.length;
    	QuickSortPair[]pairs=new QuickSortPair[size];
    	for(int i1=0;i1<size;i1++) {
    		Location e=BukkitAdapter.adapt(locations[i1]);
    		double distance=source.distance(e);
    		pairs[i1]=new QuickSortPair(distance,e);
    	}
    	pairs=QuickSort.sort(pairs,0,pairs.length-1);
    	HashSet<AbstractLocation>sorted_targets=new HashSet<>();
    	for(int i1=0;i1<size;i1++) {
    		sorted_targets.add((AbstractLocation)pairs[i1].object);
    	}
    	data.setLocationTargets(sorted_targets);
    }
    
    void nearest(SkillMetadata data) {
    	AbstractLocation[]targets=data.getLocationTargets().toArray(new AbstractLocation[data.getLocationTargets().size()]);
        AbstractLocation caster_location=data.getCaster().getLocation();
        AbstractLocation nearest=null;
        for(int i1=0;i1<targets.length;i1++) {
        	AbstractLocation e=targets[i1];
			if(nearest==null||caster_location.distance(e)<caster_location.distance(nearest)) nearest=e;
        }
        if(nearest!=null) data.setLocationTarget(nearest);
    }
    
    void shuffle(SkillMetadata data) {
		List<AbstractLocation>shuffled_locations=Arrays.asList(new AbstractLocation[data.getLocationTargets().size()]);
		Collections.shuffle(shuffled_locations);
    	HashSet<AbstractLocation>sorted_targets=new HashSet<>();
    	int size=shuffled_locations.size();
    	for(int i1=0;i1<size;i1++) {
    		sorted_targets.add(shuffled_locations.get(i1));
    	}
    	data.setLocationTargets(sorted_targets);
    }
}
