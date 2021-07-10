package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class RemoveBackBag extends SkillMechanic implements INoTargetSkill {
	PlaceholderString bag_name;
	boolean all;

	public RemoveBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		bag_name = mlc.getPlaceholderString(new String[] { "title", "name" }, BackBagHelper.str_name);
		all = mlc.getBoolean("all", false);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (all) {
			BackBagHelper.removeAll(data.getCaster().getEntity().getUniqueId());
		} else {
			BackBagHelper.remove(data.getCaster().getEntity().getUniqueId(), bag_name.get(data));
		}
		return true;
	}
}
