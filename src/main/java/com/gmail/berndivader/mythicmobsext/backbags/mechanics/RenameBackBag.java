package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagInventory;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class RenameBackBag extends SkillMechanic implements INoTargetSkill {
	PlaceholderString bag_name, new_bag_name;

	public RenameBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		bag_name = mlc.getPlaceholderString(new String[] { "title", "name" }, BackBagHelper.str_name);
		new_bag_name = mlc.getPlaceholderString(new String[] { "new", "newname" }, BackBagHelper.str_name);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Entity entity = data.getCaster().getEntity().getBukkitEntity();
		if (BackBagHelper.hasBackBag(entity.getUniqueId())) {
			BackBagInventory bag_inventory = BackBagHelper.getBagInventory(entity.getUniqueId(), bag_name.get(data));
			if (bag_inventory != null)
				bag_inventory.setName(new_bag_name.get(data));
		}
		return true;
	}
}
