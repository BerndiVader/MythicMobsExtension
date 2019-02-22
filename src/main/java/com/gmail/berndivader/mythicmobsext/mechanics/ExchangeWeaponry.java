package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="exchange,exchangeweaponry",author="BerndiVader")
public 
class 
ExchangeWeaponry 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill 
{
	public ExchangeWeaponry(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(!target.isLiving()) return false;
		LivingEntity entity=(LivingEntity)target.getBukkitEntity();
		EntityEquipment equipment=entity.getEquipment();
		ItemStack main_hand=equipment.getItemInMainHand(),off_hand=equipment.getItemInOffHand();
		equipment.setItemInMainHand(isValidMaterial(off_hand)?off_hand.clone():new ItemStack(Material.AIR));
		equipment.setItemInOffHand(isValidMaterial(main_hand)?main_hand.clone():new ItemStack(Material.AIR));
		return true;
	}
	
	static boolean isValidMaterial(ItemStack stack) {
		return stack!=null&&stack.getType()!=Material.AIR;
	}

}
