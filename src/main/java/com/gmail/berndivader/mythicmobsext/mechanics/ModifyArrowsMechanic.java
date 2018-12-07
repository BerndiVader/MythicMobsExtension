package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="modifyarrows",author="BerndiVader")
public class ModifyArrowsMechanic 
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private int a;
	private char m;

	public ModifyArrowsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.m=mlc.getString(new String[] { "mode", "m" }, "c").toUpperCase().charAt(0);
		this.a=mlc.getInteger(new String[] { "amount", "a" }, 1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			modifyArrowsAtEntity(target.getBukkitEntity(),this.a,this.m);
			return true;
		}
		return false;
	}
	
	/**
	 * @param entity {@link LivingEntity}
	 * @param amount {@link Integer}
	 * @param action {@link Char} <b>A</b>=add - <b>S</b>=sub - <b>C</b>=clear
	 */
	static void modifyArrowsAtEntity(Entity entity, int amount, char action) {
		int a=NMSUtils.getArrowsOnEntity((LivingEntity)entity);
		switch(action) {
		case 'A':
			amount+=a;
			break;
		case 'S':
			amount=a-amount;
			if(amount<0)a=0;
			break;
		case 'C':
			amount=0;
			break;
		}
		NMSUtils.setArrowsOnEntity((LivingEntity)entity,amount);
	}
	
	

}
