package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="breakblock_ext",author="BerndiVader")
public class BreakBlock 
extends
SkillMechanic
implements
ITargetedLocationSkill
{
	public BreakBlock(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation al) {
		Block block=((Location)BukkitAdapter.adapt(al)).getBlock();
		if(block!=null&&block.getType()!=Material.AIR) {
			block.breakNaturally();
			return true;
		}
		return false;
	}
}
