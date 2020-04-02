package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class WorldGuardDenySpawnFlagCondition extends AbstractCustomCondition implements ILocationCondition {
	private String entities;

	public WorldGuardDenySpawnFlagCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.entities = mlc.getString(new String[] { "entitytypes", "entitytype", "types", "type", "t" }, "zombie")
				.toUpperCase();
	}

	@Override
	public boolean check(AbstractLocation location) {
		return WorldGuardUtils.checkRegionDenySpawnFlagAtLocation(BukkitAdapter.adapt(location), entities);
	}
}
