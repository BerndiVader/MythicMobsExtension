package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;
import java.util.HashSet;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class HasMythicSpawnerCondition
extends
mmCustomCondition
implements
IEntityCondition {
	private MobManager mobmanager=Main.mythicmobs.getMobManager();
	private HashSet<String> names=new HashSet<>();
	

	public HasMythicSpawnerCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.names.addAll(Arrays.asList(mlc.getString(new String[] {"spawners","spawner","s","names","name","n"},"any").toUpperCase().split(",")));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!this.mobmanager.isActiveMob(entity)) return false;
		ActiveMob am=this.mobmanager.getMythicMobInstance(entity);
		MythicSpawner ms=am.getSpawner();
		if (ms!=null) {
			String sn=ms.getName().toUpperCase();
			if (this.names.contains("ANY")||this.names.contains(sn)) return true;
		};
		return false;
	}

}
