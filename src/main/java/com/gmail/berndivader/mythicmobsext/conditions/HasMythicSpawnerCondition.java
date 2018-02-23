package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

@ExternalAnnotation(name="hasspawner",author="BerndiVader")
public class HasMythicSpawnerCondition
extends
AbstractCustomCondition
implements
IEntityCondition {
	private String[]names;
	

	public HasMythicSpawnerCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.names=mlc.getString(new String[] {"spawners","spawner","s","names","name","n"},"any").toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!Utils.mobmanager.isActiveMob(entity)) return false;
		ActiveMob am=Utils.mobmanager.getMythicMobInstance(entity);
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
