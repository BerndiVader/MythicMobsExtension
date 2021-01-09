package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class WorldGuardStateFlagCondition extends AbstractCustomCondition implements ILocationCondition {

	private String flagName;
	private boolean debug;

	public WorldGuardStateFlagCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.flagName = mlc.getString(new String[] { "flagname", "flag", "f" }, "mob-spawning");
		this.debug = mlc.getBoolean("debug", false);
	}

	@Override
	public boolean check(AbstractLocation location) {
		boolean b = WorldGuardUtils.checkRegionStateFlagAtLocation(BukkitAdapter.adapt(location), flagName,ACTION.toString().toLowerCase());
		if (this.debug)
			Main.logger.info("wgstateflag outcome: " + b);
		return b;
	}
}
