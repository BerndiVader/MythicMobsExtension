package com.gmail.berndivader.mmcustomskills26;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.berndivader.astar.AStar;
import com.gmail.berndivader.astar.PathfindAlgorithm;
import com.gmail.berndivader.astar.PathingResult;
import com.gmail.berndivader.astar.Tile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class AStarMechanic extends SkillMechanic 
implements
ITargetedEntitySkill,
ITargetedLocationSkill {

	public AStarMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		Location start=data.getCaster().getEntity().getBukkitEntity().getLocation(),
				 end=BukkitAdapter.adapt(target);
		AStar path=new AStar(start, end, 10, true, true, PathfindAlgorithm.A_STAR);
        List<Tile>route=path.iterate();
        PathingResult result=path.getPathingResult();	
        switch(result) {
        case SUCCESS:
        	this.changePathBlocksToDiamond(start,route);
        	break;
        case NO_PATH:
        	System.err.println("No path found!");
        	break;
        }
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return castAtLocation(data, target.getLocation());
	}
	
	private void changePathBlocksToDiamond(Location start, List<Tile> tiles){
	    for(Tile t:tiles){
	        t.getLocation(start).getBlock().setType(Material.DIAMOND_BLOCK);
	    }
	}	

}
