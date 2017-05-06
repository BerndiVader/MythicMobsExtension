package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class mmHasFactionCondition extends SkillCondition implements IEntityCondition {

	private String[] faction;

	public mmHasFactionCondition(String line, MythicLineConfig mlc) {
		super(line);
		this.faction = mlc.getString(new String[]{"factions","faction","f"},"").toLowerCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity caster) {
		ActiveMob am = MythicMobs.inst().getMobManager().getMythicMobInstance(caster);
		if (am==null || !am.hasFaction()) return false;
		if (this.faction.length==1 && this.faction[0].length()==0) return true;
		return Arrays.asList(this.faction).contains(am.getFaction().toLowerCase());
	}
	
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		ActiveMob cam = MythicMobs.inst().getMobManager().getMythicMobInstance(caster);
		ActiveMob tam = MythicMobs.inst().getMobManager().getMythicMobInstance(target);
		if (cam==null || tam==null || !cam.hasFaction() || !tam.hasFaction()) return false;
		return cam.getFaction().toLowerCase().equals(tam.getFaction().toLowerCase());
	}
}
