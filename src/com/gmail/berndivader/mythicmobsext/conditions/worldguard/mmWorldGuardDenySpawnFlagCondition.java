package com.gmail.berndivader.mythicmobsext.conditions.worldguard;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmWorldGuardDenySpawnFlagCondition 
extends 
AbstractCustomCondition 
implements 
ILocationCondition {
	private WorldGuardFlags wgf = Main.wgf;
	private String[] entities;

	public mmWorldGuardDenySpawnFlagCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.entities = mlc.getString(new String[] { "entitytypes", "entitytype", "types", "type", "t" }, "zombie")
				.toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractLocation location) {
		return wgf.checkRegionDenySpawnFlagAtLocation(BukkitAdapter.adapt(location), entities);
	}
}
