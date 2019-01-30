package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class FactionsFlagCondition
extends
AbstractCustomCondition
implements
ILocationCondition 
{
	String flagName;

	public FactionsFlagCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		flagName=mlc.getString(new String[] {"flagtype","flag","f"},"monster").toLowerCase();
	}

	@Override
	public boolean check(AbstractLocation target) {
		return FactionsSupport.checkRegionFlag(BukkitAdapter.adapt(target),this.flagName);
	}
}
