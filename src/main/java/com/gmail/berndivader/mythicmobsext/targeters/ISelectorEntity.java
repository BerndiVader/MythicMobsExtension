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

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public 
abstract 
class 
ISelectorEntity
extends
IEntitySelector {
	
	List<FilterEnum> filters;
    int length;
    float yaw_offset,pitch_offset;
    double side_offset,forward_offset,y_offset;
    boolean use_relative;
	
	public ISelectorEntity(MythicLineConfig mlc) {
		super(mlc);
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
	
	@Override
    public void filter(SkillMetadata data, boolean targetCreative) {
		super.filter(data,targetCreative);
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
	
    public HashSet<AbstractEntity>applyOffsets(HashSet<AbstractEntity>targets) {
		if (this.use_relative) {
			targets.stream().forEach(abstract_entity->{
		    	Location location=abstract_entity.getBukkitEntity().getLocation();
		    	if(length!=0) location.add(location.getDirection().clone().multiply(this.length));
		    	float yaw=location.getYaw();
		    	float pitch=location.getPitch();
				Vector soV=MathUtils.getSideOffsetVectorFixed(yaw,this.side_offset,false);
				Vector foV=MathUtils.getFrontBackOffsetVector(location.getDirection(),this.forward_offset);
				abstract_entity.getLocation().add(soV.getX()+foV.getX(),this.y_offset+soV.getY()+foV.getY(),soV.getZ()+foV.getZ());
				abstract_entity.getLocation().setYaw(yaw+this.yaw_offset);
				abstract_entity.getLocation().setPitch(pitch+this.pitch_offset);
			});
		}
		return targets;
    }
    
    static void sortByDistance(SkillMetadata data) {
    	AbstractLocation source=data.getCaster().getEntity().getLocation();
    	AbstractEntity[]targets=data.getEntityTargets().toArray(new AbstractEntity[data.getEntityTargets().size()]);
    	int size=targets.length;
    	QuickSortPair[]pairs=new QuickSortPair[size];
    	for(int i1=0;i1<size;i1++) {
    		AbstractEntity e=targets[i1];
    		double distance=source.distance(e.getLocation());
    		pairs[i1]=new QuickSortPair(distance,e);
    	}
    	pairs=QuickSort.sort(pairs,0,pairs.length-1);
    	HashSet<AbstractEntity>sorted_targets=new HashSet<>();
    	for(int i1=0;i1<size;i1++) {
    		sorted_targets.add((AbstractEntity)pairs[i1].object);
    	}
    	data.setEntityTargets(sorted_targets);
    }
    
    static void nearest(SkillMetadata data) {
    	AbstractEntity[]targets=data.getEntityTargets().toArray(new AbstractEntity[data.getEntityTargets().size()]);
        AbstractLocation caster_location=data.getCaster().getLocation();
        AbstractEntity nearest=null;
        for(int i1=0;i1<targets.length;i1++) {
        	AbstractEntity e=targets[i1];
			if(nearest==null||caster_location.distance(e.getLocation())<caster_location.distance(nearest.getLocation())) nearest=e;
        }
        if(nearest!=null) data.setEntityTarget(nearest);
    }
    
    static void shuffle(SkillMetadata data) {
		List<AbstractEntity>shuffled_targets=Arrays.asList(new AbstractEntity[data.getEntityTargets().size()]);
		Collections.shuffle(shuffled_targets);
    	HashSet<AbstractEntity>sorted_targets=new HashSet<>();
    	int size=shuffled_targets.size();
    	for(int i1=0;i1<size;i1++) {
    		sorted_targets.add(shuffled_targets.get(i1));
    	}
    	data.setEntityTargets(sorted_targets);
    }
	
	
	
}
