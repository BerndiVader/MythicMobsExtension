package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class SameMythicSpawnerCondition
extends
AbstractCustomCondition
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
