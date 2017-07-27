package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import net.minecraft.server.v1_12_R1.Material;

public class DeleteMetatagMechanic extends SetMetatagMechanic {

	public DeleteMetatagMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (this.tag == null || this.tag.isEmpty())
			return false;
		String parsedTag = SkillString.parseMobVariables(this.tag, data.getCaster(), target, data.getTrigger());
		Entity bEntity = target.getBukkitEntity();
		if (bEntity.hasMetadata(parsedTag)) {
			bEntity.removeMetadata(parsedTag, Main.getPlugin());
			return true;
		}
		return false;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation location) {
		Block target = BukkitAdapter.adapt(location).getBlock();
		if (target.getType().equals(Material.AIR) || this.tag == null || this.tag.isEmpty())
			return false;
		String parsedTag = SkillString.parseMobVariables(this.tag, data.getCaster(), null, data.getTrigger());
		if (target.hasMetadata(parsedTag)) {
			target.removeMetadata(parsedTag, Main.getPlugin());
			return true;
		}
		return false;
	}

}
