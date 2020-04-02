package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "crosshairlocation", author = "BerndiVader")
public class CrosshairLocationTargeter extends ISelectorLocation {

	public CrosshairLocationTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation> targets = new HashSet<>();
		if (data.getCaster().getEntity().isPlayer()) {
			AbstractLocation location = BukkitAdapter
					.adapt(getTargetedBlockLocation((Player) data.getCaster().getEntity().getBukkitEntity(), length));
			if (location != null) {
				targets.add(location);
			}
		}
		return applyOffsets(targets);
	}

	static Location getTargetedBlockLocation(Player p1, int i1) {
		Block b1 = null;
		try {
			BlockIterator it1 = new BlockIterator(p1, i1);
			Block b2;
			b1 = it1.next();
			while (it1.hasNext()) {
				b2 = b1;
				b1 = it1.next();
				if (b1.getType() != Material.AIR) {
					b1 = b2;
					break;
				}
			}
		} catch (IllegalStateException ex) {
			return null;
		}
		Location l = null;
		if (b1 != null) {
			l = b1.getLocation().clone();
			l.setPitch(p1.getLocation().getPitch());
			l.setYaw(p1.getLocation().getYaw());
		}
		return l;
	}

}
