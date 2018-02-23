package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

@ExternalAnnotation(name="triggerdirection,targetdirection,ownerdirection",author="BerndiVader")
public class TriggerDirectionTargeter
extends
ILocationSelector {
	private int length;
	private char c;

	public TriggerDirectionTargeter(MythicLineConfig mlc) {
		this.length=mlc.getInteger(new String[] {"length","l"},10);
		c(mlc.getLine().toLowerCase().charAt(1));
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation>targets=new HashSet<>();
		AbstractLocation l=null;
		switch(c) {
		case 'a':
			if (data.getCaster().getEntity().getTarget()!=null&&data.getCaster().getEntity().getTarget().isLiving()) {
				l=data.getCaster().getEntity().getTarget().getEyeLocation();
			}
		case 'r':
			if (data.getTrigger().isLiving()) {
				l=data.getTrigger().getEyeLocation();
			}
		case 'w':
			ActiveMob am=Utils.mobmanager.getMythicMobInstance(data.getCaster().getEntity());
			if (am!=null&&am.getOwner().isPresent()) {
				Entity o=NMSUtils.getEntity(am.getEntity().getBukkitEntity().getWorld(),am.getOwner().get());
				if (o instanceof LivingEntity) l=BukkitAdapter.adapt(((LivingEntity)o).getEyeLocation());
			}
		}
		if (l!=null) {
			AbstractVector v=l.getDirection().clone().multiply(this.length);
			l.add(v);
			targets.add(l);
		}
		return targets;
	}
	
	private void c(char c) {
		this.c=c;
	}

}
