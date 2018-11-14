package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="delmeta",author="BerndiVader")
public class DeleteMetatagMechanic 
extends 
SetMetatagMechanic {

	public DeleteMetatagMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (this.tag == null || this.tag.isEmpty())
			return false;
		String parsedTag=Utils.parseMobVariables(this.tag,data,data.getCaster().getEntity(),target,null);
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
		if (this.tag == null || this.tag.isEmpty()) return false;
		String parsedTag=Utils.parseMobVariables(this.tag,data,data.getCaster().getEntity(),null,location);
		if (target.hasMetadata(parsedTag)) {
			target.removeMetadata(parsedTag, Main.getPlugin());
			return true;
		}
		return false;
	}
}
