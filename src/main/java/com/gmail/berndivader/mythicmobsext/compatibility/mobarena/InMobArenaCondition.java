package com.gmail.berndivader.mythicmobsext.compatibility.mobarena;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class InMobArenaCondition 
extends
AbstractCustomCondition
implements 
ILocationCondition 
{
	public InMobArenaCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractLocation location) {
		return MobArenaSupport.mobarena.inRegion(BukkitAdapter.adapt(location));
	}
}
