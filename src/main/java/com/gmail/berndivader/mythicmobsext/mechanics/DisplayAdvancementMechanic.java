package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="sendtoast",author="BerndiVader")
public 
class 
DisplayAdvancementMechanic 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	Material material;
	String message,frame;
	
	public DisplayAdvancementMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		material=Material.STONE;
		try {
			material=Material.valueOf(mlc.getString("icon","stone").toUpperCase());
		} catch (Exception e) {
			Main.logger.warning("Wrong material type. Set to stone");
		}
		message=mlc.getString("message","Title");
		frame=mlc.getString("frame","GOAL").toUpperCase();
	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity a_target) {
		if(!a_target.isPlayer()) return false;
		Volatile.handler.sendPlayerAdvancement((Player)a_target.getBukkitEntity(),material,message,new String(),frame);
		return true;
	}
	
	

}
