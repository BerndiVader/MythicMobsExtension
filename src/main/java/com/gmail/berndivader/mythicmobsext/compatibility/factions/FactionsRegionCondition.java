package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public 
class 
FactionsRegionCondition
extends
AbstractCustomCondition
implements
ILocationCondition 
{
	String[]regions;
	int size;

	public FactionsRegionCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		regions=mlc.getString(new String[] {"regions","region","factions","faction","r","f"},"").toLowerCase().split(",");
		size=regions.length;
	}

	@Override
	public boolean check(AbstractLocation target) {
		return FactionsSupport.inFaction(BukkitAdapter.adapt(target),regions);
	}
}