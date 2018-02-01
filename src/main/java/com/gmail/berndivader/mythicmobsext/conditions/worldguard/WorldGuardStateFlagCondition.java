package main.java.com.gmail.berndivader.mythicmobsext.conditions.worldguard;

import main.java.com.gmail.berndivader.mythicmobsext.Main;
import main.java.com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class WorldGuardStateFlagCondition
extends
AbstractCustomCondition
implements
ILocationCondition {

	private WorldGuardFlags wgf = Main.wgf;
	private String flagName;
	private boolean debug;

	public WorldGuardStateFlagCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.flagName = mlc.getString(new String[] { "flagname", "flag", "f" }, "mob-spawning");
		this.debug=mlc.getBoolean("debug",false);
	}

	@Override
	public boolean check(AbstractLocation location) {
		boolean b=wgf.checkRegionStateFlagAtLocation(BukkitAdapter.adapt(location), flagName);
		if (this.debug) Main.logger.info("wgstateflag outcome: "+b);
		return b;
	}
}
