package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.Chunk;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name="samechunk",author="BerndiVader")
public class SameChunk
extends
AbstractCustomCondition
implements
IEntityComparisonCondition
{
	public SameChunk(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity abstract_caster, AbstractEntity abstract_target) {
		int x,x1,z,z1;
		Chunk chunk=abstract_caster.getBukkitEntity().getLocation().getChunk();
		x=chunk.getX();
		z=chunk.getZ();
		chunk=abstract_target.getBukkitEntity().getLocation().getChunk();
		x1=chunk.getX();
		z1=chunk.getZ();
		return x==x1&&z==z1;
	}

}
