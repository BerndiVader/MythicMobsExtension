package com.gmail.berndivader.mmcustomskills26.conditions.Factions;

import java.util.Arrays;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class mmHasFactionCondition extends SkillCondition implements IEntityCondition {
	protected MobManager mobmanager;
	private String[] faction;

	public mmHasFactionCondition(String line, MythicLineConfig mlc) {
		super(line);
		this.mobmanager = Main.getPlugin().getMobManager();
		this.faction = mlc.getString(new String[] { "factions", "faction", "f" }, "").toLowerCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity caster) {
		ActiveMob am = this.mobmanager.getMythicMobInstance(caster);
		if (am == null || !am.hasFaction())
			return false;
		if (this.faction.length == 1 && this.faction[0].length() == 0)
			return true;
		return Arrays.asList(this.faction).contains(am.getFaction().toLowerCase());
	}

	public boolean check(AbstractEntity caster, AbstractEntity target) {
		ActiveMob cam = this.mobmanager.getMythicMobInstance(caster);
		ActiveMob tam = this.mobmanager.getMythicMobInstance(target);
		if (cam == null || tam == null || !cam.hasFaction() || !tam.hasFaction())
			return false;
		return cam.getFaction().toLowerCase().equals(tam.getFaction().toLowerCase());
	}
}
