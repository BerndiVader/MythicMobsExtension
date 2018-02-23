package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.astar.*;
import com.gmail.berndivader.mythicmobsext.astar.AStar.InvalidPathException;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="astar",author="BerndiVader")
public class AStarMechanic extends SkillMechanic 
implements
ITargetedEntitySkill,
ITargetedLocationSkill {
	int range;

	public AStarMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.range=mlc.getInteger("r",10);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		Location start=data.getCaster().getEntity().getBukkitEntity().getLocation().subtract(0, 1, 0),
				 end=BukkitAdapter.adapt(target).subtract(0,1,0);
		AStar astar;
		try {
			astar = new AStar(start, end, this.range);
	        List<Tile>route=astar.iterate();
	        if (route==null) {
	        	System.err.println("route=null");
	        	return false;
	        }
	       	this.changePathBlocksToDiamond(start,route);
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return castAtLocation(data, target.getLocation());
	}
	
	private void changePathBlocksToDiamond(Location start, List<Tile> tiles){
		List<Player>players=start.getWorld().getPlayers();
		Iterator<Tile>it1=tiles.iterator();
		while(it1.hasNext()) {
			Tile t=it1.next();
			for (Player p:players) {
				p.sendBlockChange(new Location(p.getWorld(), (start.getBlockX() + t.getX()), (start.getBlockY() + t.getY()), (start.getBlockZ() + t.getZ())),
						Material.DIAMOND_BLOCK, (byte) 0);			
			}
		}
	}	

}
