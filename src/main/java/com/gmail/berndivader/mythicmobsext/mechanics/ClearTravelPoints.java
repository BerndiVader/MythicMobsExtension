package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="cleartravelpoints",author="BerndiVader")
public 
class
ClearTravelPoints 
extends
SkillMechanic
implements
INoTargetSkill
{
	boolean remove_points;
	
	public ClearTravelPoints(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		this.remove_points=mlc.getBoolean("removeagain",true);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Volatile.handler.clearTravelPoints(data.getCaster().getEntity().getBukkitEntity());
		return true;
	}
}
