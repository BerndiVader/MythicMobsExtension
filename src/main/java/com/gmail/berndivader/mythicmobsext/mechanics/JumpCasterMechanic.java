package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="jumpex",author="BerndiVader")
public 
class 
JumpCasterMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill
{
	
	float speed,force,G;
	boolean debug,default_g;
	
	public JumpCasterMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	
		this.speed=mlc.getFloat("speed",1f);
		this.G=mlc.getFloat("gravity",0.1955f);
		
		this.debug=mlc.getBoolean("debug",false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if(data.getCaster().getEntity().isLiving()&&t.isLiving()) {
			LivingEntity caster=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			LivingEntity target=(LivingEntity)t.getBukkitEntity();
			
			Vec3D target_position=Volatile.handler.getPredictedMotion(caster,target,0.0f);
			Vector direction=Utils.calculateDirectionVector(target_position,speed,G);
			
			if(Float.isFinite((float)direction.getX())&&Float.isFinite((float)direction.getZ())) {
				caster.setVelocity(direction.multiply(speed));
			} else {
				if(debug) Main.logger.warning("Target cannot be reached with the given speed and gravity");
				return false;
			}
		}
		return true;
	}
	
}
