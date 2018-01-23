package com.gmail.berndivader.mythicmobsext.conditions.own;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class MoveToCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition {
	double dso,dfo;

	public MoveToCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		dso=mlc.getDouble("so",0);
		dfo=mlc.getDouble("fo",0);
	}
	
	@Override
	public boolean check(AbstractEntity var1) {
		Location l1=var1.getBukkitEntity().getLocation().clone();
		Vector so=Utils.getSideOffsetVector(l1.getYaw(),dso,false);
		Vector fo=Utils.getFrontBackOffsetVector(l1.getDirection(),dfo);
		if (var1.isPlayer()) {
			System.err.println(Utils.pl.get(var1.getUniqueId()).toString());
		} else {
			Main.getPlugin().getVolatileHandler().moveto((LivingEntity)var1.getBukkitEntity());
		}
		return false;
	}
	
}
