package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "delmeta", author = "BerndiVader")
public class DeleteMetatagMechanic extends SetMetatagMechanic {

	public DeleteMetatagMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		String parsedTag = this.tag.get(data, target);
		if (parsedTag == null || parsedTag.isEmpty())
			return false;
		Entity bEntity = target.getBukkitEntity();
		if (bEntity.hasMetadata(parsedTag)) {
			bEntity.removeMetadata(parsedTag, Main.getPlugin());
			return true;
		}
		return false;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation location) {
		String parsedTag = this.tag.get(data);
		if (parsedTag == null || parsedTag.isEmpty())
			return false;
		Block target = BukkitAdapter.adapt(location).getBlock();
		if (target.hasMetadata(parsedTag)) {
			target.removeMetadata(parsedTag, Main.getPlugin());
			return true;
		}
		return false;
	}
}
