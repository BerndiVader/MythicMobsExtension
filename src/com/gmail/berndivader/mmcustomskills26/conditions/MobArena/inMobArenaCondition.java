package com.gmail.berndivader.mmcustomskills26.conditions.MobArena;

import com.garbagemule.MobArena.MobArenaHandler;
import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class inMobArenaCondition extends mmCustomCondition implements ILocationCondition {
	protected MobArenaHandler maHandler;

	public inMobArenaCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.maHandler = Main.getPlugin().getMobArenaHandler();
	}

	@Override
	public boolean check(AbstractLocation location) {
		return maHandler.inRegion(BukkitAdapter.adapt(location));
	}
}
