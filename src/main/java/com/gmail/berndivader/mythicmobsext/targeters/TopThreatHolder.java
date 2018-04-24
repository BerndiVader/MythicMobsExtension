package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

@ExternalAnnotation(name="tth,topthreatholder",author="BerndiVader")
public class TopThreatHolder 
extends 
IEntitySelector {
	
	public TopThreatHolder(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>targets=new HashSet<>();
		ActiveMob am=(ActiveMob)data.getCaster();
		if (am!=null&&am.hasThreatTable()) targets.add(am.getThreatTable().getTopThreatHolder());
		return targets;
	}
}
