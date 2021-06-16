package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "targetgrid", author = "BerndiVader")
public class TargetsGrid extends ISelectorEntity {
	public TargetsGrid(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity> targets = new HashSet<>();
		return this.applyOffsets(targets);
	}
}
