package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="addtravelpoint",author="BerndiVader")
public 
class
AddTravelPoint 
extends
SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill
{
	
	public AddTravelPoint(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		return castAtLocation(data,abstract_entity.getLocation());
	}
	
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation abstract_location) {
		Volatile.handler.addTravelPoint(data.getCaster().getEntity().getBukkitEntity(),new Vec3D(abstract_location.getX(),abstract_location.getY(),abstract_location.getZ()));
		return true;
	}
	
	
}
