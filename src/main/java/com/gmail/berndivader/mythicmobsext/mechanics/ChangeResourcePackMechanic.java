package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "changeresourcepack", author = "BerndiVader")
public class ChangeResourcePackMechanic extends SkillMechanic implements ITargetedEntitySkill {

	String url, hash;

	public ChangeResourcePackMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		url = mlc.getString("url", "");
		hash = mlc.getString("hash", "mme");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		if (entity.isPlayer()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Volatile.handler.changeResPack((Player) entity.getBukkitEntity(), url, hash);
				}
			}.runTaskAsynchronously(Main.getPlugin());
			return true;
		}
		return false;
	}

}
