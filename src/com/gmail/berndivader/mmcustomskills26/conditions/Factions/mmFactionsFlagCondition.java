package com.gmail.berndivader.mmcustomskills26.conditions.Factions;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmFactionsFlagCondition extends SkillCondition implements ILocationCondition {
	private FactionsFlags fflags = Main.fflags;
	private String flagName;

	public mmFactionsFlagCondition(String line, MythicLineConfig mlc) {
		super(line);
		this.flagName = mlc.getString(new String[] { "flagtype", "flag", "f" }, "monster").toLowerCase();
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[] { "action", "a" }, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
	}

	@Override
	public boolean check(AbstractLocation target) {
		return this.fflags.checkFlag(BukkitAdapter.adapt(target), this.flagName);
	}
}
