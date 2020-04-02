package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class ExpandBackBag extends SkillMechanic implements INoTargetSkill, ITargetedEntitySkill {
	int size;
	PlaceholderString bag_name;

	public ExpandBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;
		size = mlc.getInteger("size", 1);
		bag_name = mlc.getPlaceholderString(new String[] { "title", "name" }, BackBagHelper.str_name);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data, data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		BackBagHelper.expandBackBag(abstract_entity.getBukkitEntity(), bag_name.get(data, abstract_entity), size);
		return true;
	}
}
