package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

@ExternalAnnotation(name="entitydistance",author="BerndiVader")
public class EntitiesByDistance 
extends 
IEntitySelector {
	boolean reverse;

	public EntitiesByDistance(MythicLineConfig mlc) {
		super(mlc);
		reverse=mlc.getBoolean("reverse",false);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
		if (data.getEntityTargets()!=null&&data.getEntityTargets().size()>0) {
			AbstractEntity[]entities=(AbstractEntity[])data.getEntityTargets().toArray();
		}
		return targets;
	}
}
