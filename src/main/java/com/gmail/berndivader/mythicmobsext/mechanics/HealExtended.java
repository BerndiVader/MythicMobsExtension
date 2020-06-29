package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "heal", author = "BerndiVader")
public 
class 
HealExtended 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill, 
INoTargetSkill
{
	double healByDistance;
	boolean reduceHealByDistance;
	boolean power;
	boolean percentage;
	boolean caster;
	boolean current;
	boolean loss;
	RegainReason reason;
	PlaceholderString amountPlaceholder;
	
	public HealExtended(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		amountPlaceholder=PlaceholderString.of(mlc.getString("amount","0"));
		power=mlc.getBoolean("power",false);
		percentage=mlc.getBoolean("percent",false);
		caster=mlc.getBoolean("caster",false);
		current=mlc.getBoolean("current",false);
		if ((loss=mlc.getBoolean("loss",false))) current=false;
		
		healByDistance=mlc.getDouble("dec",0)*-1;
		reduceHealByDistance=(healByDistance=mlc.getDouble("inc",healByDistance))<0;
		healByDistance=Math.abs(healByDistance);
		
		String reasonString=mlc.getString("reason","custom").toUpperCase();
		reason=RegainReason.CUSTOM;
		for(RegainReason value:RegainReason.values()) {
			if(value.toString().equals(reasonString)) {
				reason=value;
				break;
			}
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_target) {
		if(!abstract_target.isLiving()||!abstract_target.isValid()||!abstract_target.isDead()) return false;
		
		LivingEntity t=(LivingEntity)abstract_target.getBukkitEntity();
		LivingEntity c=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		
		double amount=new RandomDouble(amountPlaceholder.get(data,abstract_target)).rollDouble();
		AbstractEntity abstract_caster=data.getCaster().getEntity();
		
		if(percentage) {
			amount=current?caster?c.getHealth()*amount:t.getHealth()*amount
					:loss?caster?(c.getMaxHealth()-c.getHealth())*amount:(t.getMaxHealth()-t.getHealth())*amount
					:caster?c.getMaxHealth()*amount:t.getMaxHealth()*amount;
		}
		
		if(power) amount=amount*data.getPower();
		
		if(healByDistance>0) {
			int distance=(int)Math.sqrt(MathUtils.distance3D(abstract_caster.getBukkitEntity().getLocation().toVector(),abstract_target.getBukkitEntity().getLocation().toVector()));
			amount=reduceHealByDistance?amount-(amount*(distance*healByDistance)):amount+(amount*(distance*healByDistance));
		}
		
		EntityRegainHealthEvent event=new EntityRegainHealthEvent(t,amount,reason);
		Bukkit.getPluginManager().callEvent(event);
		System.err.println(amount+":"+event.getAmount());
		if(!event.isCancelled()) t.setHealth(event.getAmount());
		return true;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data,data.getCaster().getEntity());
	}
}
