package main.java.com.gmail.berndivader.mythicmobsext.conditions.mobarena;

import main.java.com.gmail.berndivader.mythicmobsext.Main;
import main.java.com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

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
		this.maHandler = Main.getPlugin().getMobArenaHandler();
	}

	@Override
	public boolean check(AbstractLocation location) {
		return maHandler.inRegion(BukkitAdapter.adapt(location));
	}
}
