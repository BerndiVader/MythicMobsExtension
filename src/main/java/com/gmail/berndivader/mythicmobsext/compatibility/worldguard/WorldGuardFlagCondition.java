package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public
class 
WorldGuardFlagCondition 
extends 
AbstractCustomCondition 
implements
ILocationCondition {
	
	String flag_name;
	String args;

	public WorldGuardFlagCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		
		flag_name=mlc.getString("flag","mob-spawning");
		args=mlc.getString("args",new String()).toLowerCase();
	}

	@Override
	public boolean check(AbstractLocation location) {
		return WorldGuardUtils.checkFlagAtLocation(BukkitAdapter.adapt(location),flag_name,args);
	}

}
