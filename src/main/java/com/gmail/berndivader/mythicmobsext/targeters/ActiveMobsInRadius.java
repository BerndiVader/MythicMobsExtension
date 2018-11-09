package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name="amir,activemobsinradius",author="BerndiVader")
public class ActiveMobsInRadius 
extends 
ISelectorEntity
{
	
	String[]ml;
	boolean all=false;
	RangedDouble a;
	double r;
	
	public ActiveMobsInRadius(MythicLineConfig mlc) {
		super(mlc);
		ml=mlc.getString(new String[] { "mobtypes", "types", "mobs", "mob", "type", "t", "m" }, "ALL").toUpperCase().split(",");
		if (ml.length==1&&(ml[0].equals("ALL")||ml[0].equals("ANY"))) {
			this.all=true;
		}
		this.a=new RangedDouble(mlc.getString(new String[] { "amount","a" }, "0"),false);
		this.r=Math.pow(mlc.getDouble(new String[] { "radius", "r" },5),2d);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		double radius=this.r;
		UUID id=data.getCaster().getEntity().getUniqueId();
		HashSet<AbstractEntity>targets=new HashSet<>();
		Location l=BukkitAdapter.adapt(data.getCaster().getLocation());
		for (Iterator<LivingEntity> it=l.getWorld().getLivingEntities().iterator();it.hasNext();) {
			LivingEntity e=it.next();
			if (e.getUniqueId().equals(id)||e.getWorld()!=l.getWorld()) continue;
			double diffsq=l.distanceSquared(e.getLocation());
			if (diffsq<=radius) {
				ActiveMob am=Utils.mobmanager.getMythicMobInstance(e);
				if (am==null) continue;
				if (this.all) {
					targets.add(am.getEntity());
				} else {
					for(String s1:ml) {
						if (s1.equals(am.getType().getInternalName().toUpperCase())) {
							targets.add(am.getEntity());
							break;
						}
					}
				}
				am=null;
			}
		}
		return targets;
	}

}
