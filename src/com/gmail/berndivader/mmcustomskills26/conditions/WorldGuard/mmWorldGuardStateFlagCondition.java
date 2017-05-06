package com.gmail.berndivader.mmcustomskills26.conditions.WorldGuard;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmWorldGuardStateFlagCondition extends SkillCondition implements ILocationCondition {

	private WorldGuardFlags wgf = Main.wgf;
	private String flagName;
	
	public mmWorldGuardStateFlagCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action","a"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
		this.flagName = mlc.getString(new String[]{"flagname","flag","f"},"mob-spawning");
	}

	@Override
	public boolean check(AbstractLocation location) {
		return wgf.checkRegionStateFlagAtLocation(BukkitAdapter.adapt(location),  flagName);
	}
}
