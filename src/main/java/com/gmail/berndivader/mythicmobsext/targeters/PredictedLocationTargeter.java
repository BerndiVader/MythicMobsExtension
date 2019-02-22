package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

@ExternalAnnotation(name="targetpredict,triggerpredict,selfpredict,ownerpredict",author="BerndiVader")
public class PredictedLocationTargeter 
extends 
ILocationSelector 
{
	String selector;
	double delta;

	public PredictedLocationTargeter(MythicLineConfig mlc) {
		selector=mlc.getLine().toLowerCase().split("predict")[0];
		delta=mlc.getDouble("delta",5d);
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		Entity ee=null;
		HashSet<AbstractLocation>targets=new HashSet<>();
		switch(selector) {
		case "target":
			ee=data.getEntityTargets().size()>0?data.getEntityTargets().iterator().next().getBukkitEntity()
					:data.getCaster().getEntity().getTarget()!=null?data.getCaster().getEntity().getTarget().getBukkitEntity():null;
			break;
		case "trigger":
			if (data.getTrigger()!=null) ee=data.getTrigger().getBukkitEntity();
			break;
		case "owner":
			ActiveMob am;
			if ((am=(ActiveMob)data.getCaster())!=null&&am.getOwner().isPresent()) {
				ee=NMSUtils.getEntity(data.getCaster().getEntity().getBukkitEntity().getWorld(),am.getOwner().get());
			}
			break;
		default:
			ee=data.getCaster().getEntity().getBukkitEntity();
			break;
		}
		if (ee!=null&&ee instanceof LivingEntity&&data.getCaster().getEntity().isLiving()) {
			Vec3D target_position=Volatile.handler.getPredictedMotion((LivingEntity)data.getCaster().getEntity().getBukkitEntity(),(LivingEntity)ee,5.0f);
//			System.err.println(target_position.toString());
			targets.add(BukkitAdapter.adapt(new Location(ee.getWorld(),target_position.getX(),target_position.getY(),target_position.getZ())));
		}
		return targets;
	}
}
