package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="jumpto",author="BerndiVader")
public 
class 
JumpCasterExtMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill,
ITargetedLocationSkill
{
	
	double height,gravity,speed;
	boolean use_speed,use_gravity;
	
	public JumpCasterExtMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		
		height=mlc.getDouble("height",2d);
		use_gravity=(gravity=mlc.getDouble("gravity",-1337))!=-1337;
		use_speed=(speed=mlc.getDouble("speed",-1337))!=-1337;
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		return castAtLocation(data,t.getLocation());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation l) {
		Entity entity=data.getCaster().getEntity().getBukkitEntity();
		Location destination=BukkitAdapter.adapt(l);

		if(!use_gravity) {
			System.err.println("use gravity");
			gravity=Utils.getGravity(entity.getType());
		}
        entity.setVelocity(MathUtils.calculateVelocity(entity.getLocation().toVector(),destination.toVector(),gravity,height));
		
		return true;
	}
	
}
