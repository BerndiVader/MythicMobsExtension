package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public class CrosshairLocationTargeter
extends
ILocationSelector {
	int length;
	
	public CrosshairLocationTargeter(MythicLineConfig mlc) {
		length=mlc.getInteger("length",64);
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation>targets=new HashSet<>();
		if (data.getCaster().getEntity().isPlayer()) {
			targets.add(BukkitAdapter.adapt(getTargetedBlockLocation((Player)data.getCaster().getEntity().getBukkitEntity(),length)));
		}
		return targets;
	}
	
    private Location getTargetedBlockLocation(Player p1, int i1) {
        BlockIterator it1=new BlockIterator(p1,i1);
        Block b1=it1.next();
        while (it1.hasNext()) {
            b1=it1.next();
            if (b1.getType()==Material.AIR) continue;
        }
        return b1.getLocation();
    }	

}
