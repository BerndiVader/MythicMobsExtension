package com.gmail.berndivader.mythicmobsext.conditions.own;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class HasMythicSpawnerCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	private MobManager mobmanager=Main.mythicmobs.getMobManager();
	private String[]names;
	

	public HasMythicSpawnerCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.names=mlc.getString(new String[] {"spawners","spawner","s","names","name","n"},"any").toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!this.mobmanager.isActiveMob(entity)) return false;
		ActiveMob am=this.mobmanager.getMythicMobInstance(entity);
		MythicSpawner ms=am.getSpawner();
		if (ms!=null) {
			String sn=ms.getName().toUpperCase();
			if (this.names.length==1&&this.names[0].equals("ANY")) return true;
			for(String s1:this.names) {
				if (s1.equals(sn)) return true;
			}
		};
		return false;
	}

}
