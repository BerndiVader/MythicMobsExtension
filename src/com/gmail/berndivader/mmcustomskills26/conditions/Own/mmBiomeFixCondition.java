package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashSet;
import java.util.Set;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractBiome;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmBiomeFixCondition extends mmCustomCondition implements
ILocationCondition {
	protected Set<AbstractBiome> biome = new HashSet<AbstractBiome>();

	public mmBiomeFixCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String b = mlc.getString(new String[]{"biome", "b"}, "PLAINS");
		for (String s : b.split(",")) {
			this.biome.add(new AbstractBiome(s));
	    }
	}

	@Override
	public boolean check(AbstractLocation l) {
	    return this.biome.contains(l.getBiome());
	}
}
