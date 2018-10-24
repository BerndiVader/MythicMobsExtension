package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="push",author="BerndiVader")
public 
class 
PushCasterMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill,
ITargetedLocationSkill
{
	
	float speed;
	
	public PushCasterMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		
		this.speed=mlc.getFloat("speed",1.0f);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		return castAtLocation(data, t.getLocation());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation l) {
		Location dest=BukkitAdapter.adapt(l);
		Entity caster=data.getCaster().getEntity().getBukkitEntity();
		Vector mod=dest.toVector().subtract(caster.getLocation().toVector());
		mod.normalize().multiply(speed);
		caster.setVelocity(caster.getVelocity().add(mod));
		return true;
	}
	
}
