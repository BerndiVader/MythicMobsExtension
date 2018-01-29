package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public class TargetMotionTargeter 
extends 
IEntitySelector {
	String selector;
	int length;

	public TargetMotionTargeter(MythicLineConfig mlc) {
		super(mlc);
		selector=mlc.getLine().toLowerCase().split("motion")[0];
		length=mlc.getInteger("length",10);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		switch(selector) {
		case "target":
			
		}
		return null;
	}

}
