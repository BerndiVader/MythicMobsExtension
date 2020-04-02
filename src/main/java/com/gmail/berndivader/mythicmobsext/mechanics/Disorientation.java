package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "disorientation", author = "BerndiVader")
public class Disorientation extends SkillMechanic implements ITargetedEntitySkill {

	byte state = 0;

	public Disorientation(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		state |= mlc.getBoolean("look", true) ? 0b1 : 0b0;
		state |= mlc.getBoolean("position", true) ? 0b10 : 0b00;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if (abstract_entity.isPlayer()) {
			Player player = (Player) abstract_entity.getBukkitEntity();
			if (player.hasMetadata(Utils.meta_DISORIENTATION)) {
				player.removeMetadata(Utils.meta_DISORIENTATION, Main.getPlugin());
			} else {
				player.setMetadata(Utils.meta_DISORIENTATION, new FixedMetadataValue(Main.getPlugin(), state));
			}
			return true;
		}
		return false;
	}

}
