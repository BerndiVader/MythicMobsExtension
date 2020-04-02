package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "targetnotcaster", author = "BerndiVader")
public class TargetnotCaster extends AbstractCustomCondition implements ITargetedEntitySkill {

	public TargetnotCaster(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		return data.getCaster().getEntity().getBukkitEntity().getEntityId() != entity.getBukkitEntity().getEntityId();
	}
}
