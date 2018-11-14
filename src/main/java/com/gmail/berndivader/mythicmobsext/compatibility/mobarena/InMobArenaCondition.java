package com.gmail.berndivader.mythicmobsext.compatibility.mobarena;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

import com.garbagemule.MobArena.MobArenaHandler;

public class InMobArenaCondition 
extends
AbstractCustomCondition
implements 
ILocationCondition {
	protected MobArenaHandler maHandler;

	public InMobArenaCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.maHandler = new MobArenaHandler();
	}

	@Override
	public boolean check(AbstractLocation location) {
		return maHandler.inRegion(BukkitAdapter.adapt(location));
	}
}
