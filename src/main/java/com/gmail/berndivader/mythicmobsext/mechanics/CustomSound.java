package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "customsound", author = "BerndiVader")
public class CustomSound extends SkillMechanic implements ITargetedEntitySkill {
	String type;
	float volume, pitch;

	public CustomSound(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		type = mlc.getString("type", "block.chest.open");
		volume = mlc.getFloat("volume", 1f);
		pitch = mlc.getFloat("pitch", 1f);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity var2) {
		if (var2.isPlayer()) {
			return playSoundAtPlayer(this.type, this.volume, this.pitch, (Player) var2.getBukkitEntity());
		}
		return false;
	}

	private static boolean playSoundAtPlayer(String name, float volume, float pitch, Player player) {
		if (player.isOnline()) {
			player.playSound(player.getLocation(), name, volume, pitch);
			return true;
		}
		return false;
	}

}
