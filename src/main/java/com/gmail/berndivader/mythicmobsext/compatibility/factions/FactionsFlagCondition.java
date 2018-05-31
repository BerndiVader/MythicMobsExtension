package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class FactionsFlagCondition
extends
AbstractCustomCondition
implements
ILocationCondition {
	private FactionsFlags fflags = Main.fflags;
	private String flagName;

	public FactionsFlagCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.flagName = mlc.getString(new String[] { "flagtype", "flag", "f" }, "monster").toLowerCase();
	}

	@Override
	public boolean check(AbstractLocation target) {
		return this.fflags.checkFlag(BukkitAdapter.adapt(target), this.flagName);
	}
}
