package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class SameMythicSpawnerCondition
extends
mmCustomCondition
implements
IEntityComparisonCondition {
	private MobManager mobmanager=Main.mythicmobs.getMobManager();

	public SameMythicSpawnerCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		ActiveMob cm=this.mobmanager.getMythicMobInstance(caster),tm=this.mobmanager.getMythicMobInstance(target);
		if (cm!=null&&tm!=null) {
			MythicSpawner cs=cm.getSpawner(),ts=tm.getSpawner();
			if (cs!=null&&ts!=null) {
				return cs.getName().equals(ts.getName());
			}
		}
		return false;
	}
	
}
