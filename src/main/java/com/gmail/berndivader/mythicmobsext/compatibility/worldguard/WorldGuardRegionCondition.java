package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class WorldGuardRegionCondition extends AbstractCustomCondition implements ILocationCondition {

	List<String> region_names;
	boolean debug, use_priority;

	public WorldGuardRegionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);

		region_names = Arrays.asList(mlc.getString(new String[] { "regions", "region", "r" }, new String()).split(","));
		use_priority = mlc.getBoolean("usepriority", false);
	}

	@Override
	public boolean check(AbstractLocation al) {
		Set<ProtectedRegion> regions = WorldGuardUtils.getRegionsByLocation(BukkitAdapter.adapt(al));
		if (regions != null) {
			if (use_priority) {
				ProtectedRegion region = regions.stream().max(Comparator.comparing(ProtectedRegion::getPriority)).get();
				if (region_names.contains(region.getId()))
					return true;
			} else {
				for (ProtectedRegion region : regions) {
					if (region_names.contains(region.getId()))
						return true;
				}
			}
		}
		return false;
	}

}
