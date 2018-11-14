package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.metadata.MetadataValue;

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
	
    private boolean targetPlayers = true;
    private boolean targetCreativeMode = false;
    private boolean targetSpectatorMode = false;
    private boolean targetCitizensNPCs = false;
    private boolean targetAnimals = true;
    private boolean targetCreatures = true;
    private boolean targetMonsters = true;
    private boolean targetWaterMobs = true;
    private boolean targetFlyingMobs = true;
    private boolean targetRider=true;
    private boolean targetSameFaction = true;
    private boolean sort_by_distance,nearest;
    private List<String> ignoreTypes = null;
	
	public ISelectorEntity(MythicLineConfig mlc) {
		super(mlc);
        String target = mlc.getString("target", null);
        String ignore = mlc.getString("ignore", null);
        String ignoreTypes = mlc.getString("ignoretype", null);
        if (target != null) {
            this.targetPlayers = false;
            this.targetCreativeMode = false;
            this.targetSpectatorMode = false;
            this.targetCitizensNPCs = false;
            this.targetAnimals = false;
            this.targetCreatures = false;
            this.targetMonsters = false;
            this.targetWaterMobs = false;
            this.targetFlyingMobs = false;
            if (target.contains("player")) {
                this.targetPlayers = true;
            }
            if (target.contains("creative")) {
                this.targetCreativeMode = true;
            }
            if (target.contains("spectator")) {
                this.targetSpectatorMode = true;
            }
            if (target.contains("npc")) {
                this.targetCitizensNPCs = true;
            }
            if (target.contains("animal")) {
                this.targetAnimals = true;
            }
            if (target.contains("monster")) {
                this.targetMonsters = true;
            }
            if (target.contains("creatures")) {
                this.targetCreatures = true;
            }
            if (target.contains("rider")) this.targetRider=true;
        }
        if (ignore != null) {
            if (ignore.contains("player")) {
                this.targetPlayers = false;
            }
            if (ignore.contains("creative")) {
                this.targetCreativeMode = false;
            }
            if (ignore.contains("spectator")) {
                this.targetSpectatorMode = false;
            }
            if (ignore.contains("npc")) {
                this.targetCitizensNPCs = false;
            }
            if (ignore.contains("animal")) {
                this.targetAnimals = false;
            }
            if (ignore.contains("monsters")) {
                this.targetMonsters = false;
            }
            if (ignore.contains("creatures")) {
                this.targetCreatures = false;
            }
            if (ignore.contains("faction")) {
                this.targetSameFaction = false;
            }
            if (ignore.contains("rider")) {
            	this.targetRider=false;
            }
            
        }
        if (ignoreTypes != null) {
            this.ignoreTypes = new ArrayList<String>();
            for (String s : ignoreTypes.split(",")) {
                this.ignoreTypes.add(s);
            }
        }
        this.targetPlayers = mlc.getBoolean("targetplayers", this.targetPlayers);
        this.targetCreativeMode = mlc.getBoolean("targetcreative", this.targetCreativeMode);
        this.targetSpectatorMode = mlc.getBoolean("targetspectator", this.targetSpectatorMode);
        this.targetCitizensNPCs = mlc.getBoolean("targetnpcs", this.targetCitizensNPCs);
        this.targetAnimals = mlc.getBoolean("targetanimals", this.targetAnimals);
        this.targetCreatures = mlc.getBoolean("targetcreatures", this.targetCreatures);
        this.targetSameFaction = mlc.getBoolean("targetsamefaction", this.targetSameFaction);
        this.sort_by_distance=mlc.getBoolean("sortbydistance",false);
        this.nearest=mlc.getBoolean("nearest");
        this.targetRider=mlc.getBoolean("targetrider",this.targetRider);
	}
	
	@Override
    public void filter(SkillMetadata data, boolean targetCreative) {
        Iterator<AbstractEntity>it=data.getEntityTargets().iterator();
        int size=data.getEntityTargets().size();
        Location caster_location=data.getCaster().getEntity().getBukkitEntity().getLocation();
        AbstractEntity nearest=null;
        for(int i1=0;i1<size;i1++) {
        	AbstractEntity e=it.next();
        	if(e==null) {
        		it.remove();
        		continue;
        	}
        	if(!this.targetPlayers) {
        		if(e.isPlayer()) {
        			it.remove();
        			continue;
        		}
        	} else {
				if (!this.targetCreativeMode && !targetCreative && e.isPlayer() && e.asPlayer().isInCreativeMode()) {
				    it.remove();
				    continue;
				}
				if (!this.targetSpectatorMode && !targetCreative && e.isPlayer() && e.asPlayer().isInSpectatorMode()) {
				    it.remove();
				    continue;
				}
        	}
			if (!this.targetAnimals && e.isAnimal()) {
			    it.remove();
			    continue;
			}
			if (!this.targetCreatures && e.isCreature()) {
			    it.remove();
			    continue;
			}
			if (!this.targetMonsters && e.isMonster()) {
			    it.remove();
			    continue;
			}
			if (!this.targetCitizensNPCs && e.isLiving() && e.getBukkitEntity().hasMetadata("NPC")) {
			    it.remove();
			    continue;
			}
			if (!this.targetSameFaction) {
			    ActiveMob am = (ActiveMob)data.getCaster();
			    if (e.isLiving() && e.getBukkitEntity().hasMetadata("Faction")) {
			        List<MetadataValue> md = e.getBukkitEntity().getMetadata("Faction");
		            for (MetadataValue v : md) {
		                if (!v.asString().equals(am.getFaction())) continue;
		                it.remove();
		                break;
		            }
			    }
			}
			if(this.nearest) {
				if(nearest==null||caster_location.distance(e.getBukkitEntity().getLocation())<caster_location.distance(nearest.getBukkitEntity().getLocation())) {
					nearest=e;
				}
			}
			if(!this.targetRider) {
				if(e.getBukkitEntity().getVehicle()==data.getCaster().getEntity().getBukkitEntity()) it.remove();
			}
        }
        if(this.nearest&&nearest!=null) {
        	data.getEntityTargets().clear();
        	data.getEntityTargets().add(nearest);
        }
        if(this.sort_by_distance&&data.getEntityTargets().size()>0) {
        	HashSet<AbstractEntity>targets=data.getEntityTargets();
        	Location source=data.getCaster().getEntity().getBukkitEntity().getLocation();
        	QuickSortPair[]pairs=new QuickSortPair[targets.size()];
        	Iterator<AbstractEntity>it1=targets.iterator();
        	int size1=targets.size();
        	for(int i1=0;i1<size1;i1++) {
        		AbstractEntity e=it1.next();
        		double distance=source.distance(e.getBukkitEntity().getLocation());
        		pairs[i1]=new QuickSortPair(distance,e);
        	}
        	pairs=QuickSort.sort(pairs,0,pairs.length-1);
        	HashSet<AbstractEntity>sorted_targets=new HashSet<>();
        	for(int i1=0;i1<size1;i1++) {
        		sorted_targets.add((AbstractEntity)pairs[i1].object);
        	}
        	data.setEntityTargets(sorted_targets);
        }
        
	}
	
}
