package com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmWorldGuardDenySpawnFlagCondition extends SkillCondition implements ILocationCondition {
	private WorldGuardFlags wgf = Main.wgf;
	private String[] entities;

	public mmWorldGuardDenySpawnFlagCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[] { "action", "a" }, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.FALSE;
		}
		this.entities = mlc.getString(new String[] { "entitytypes", "entitytype", "types", "type", "t" }, "zombie")
				.toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractLocation location) {
		return wgf.checkRegionDenySpawnFlagAtLocation(BukkitAdapter.adapt(location), entities);
	}
}
