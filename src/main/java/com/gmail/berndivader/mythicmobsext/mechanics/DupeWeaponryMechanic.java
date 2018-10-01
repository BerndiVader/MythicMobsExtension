package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="dupeweaponry",author="BerndiVader")
public 
class 
DupeWeaponryMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	boolean removeFromTarget;
	boolean ignoreAir;
	byte what;

	public DupeWeaponryMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		removeFromTarget=mlc.getBoolean(new String[] {"removefromtarget","rft"},false);
		ignoreAir=mlc.getBoolean(new String[] {"ignoreair","ia"},true);
		
		/*
		 * bit 01= mainhand
		 * bit 10= offhand
		 * 
		 */
		what=(byte)mlc.getInteger("what",3);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(target.isLiving()&&data.getCaster().getEntity().isLiving()) {
			ItemStack mainHand	=new ItemStack(((LivingEntity)target.getBukkitEntity()).getEquipment().getItemInMainHand());
			ItemStack offHand	=new ItemStack(((LivingEntity)target.getBukkitEntity()).getEquipment().getItemInOffHand());
			LivingEntity e=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			if((what&1)==1) {
				if(mainHand.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setItemInMainHand(mainHand);
					if(removeFromTarget) ((LivingEntity)target.getBukkitEntity()).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
				}
			}
			if((what&2)==2) {
				if(offHand.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setItemInOffHand(offHand);
					if(removeFromTarget) ((LivingEntity)target.getBukkitEntity()).getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
				}
			}
		}
		return true;
	}

}
