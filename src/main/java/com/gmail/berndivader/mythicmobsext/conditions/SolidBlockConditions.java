package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.block.BlockFace;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class SolidBlockConditions
extends
AbstractCustomCondition
implements
ILocationCondition {
	private char c;
	public SolidBlockConditions(String line, MythicLineConfig mlc) {
		super(line, mlc);
		c=line.toUpperCase().charAt(0);
	}

	@Override
	public boolean check(AbstractLocation a) {
		switch(c) {
		case 'O': 
	        return BukkitAdapter.adapt(a).getBlock().getRelative(BlockFace.DOWN).getType().isSolid();
		case 'I':
			return BukkitAdapter.adapt(a).getBlock().getType().isSolid();
		}
		return false;
	}

}
