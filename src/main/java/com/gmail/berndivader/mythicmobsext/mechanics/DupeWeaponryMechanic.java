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
		 * bit 001= mainhand
		 * bit 010= offhand
		 * bit 100= armor
		 * 
		 */
		what=(byte)mlc.getInteger("what",3);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(target.isLiving()&&data.getCaster().getEntity().isLiving()) {
			LivingEntity e=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			LivingEntity t=(LivingEntity)target.getBukkitEntity();
			ItemStack is;
			if((what&1)==1) {
				is=new ItemStack(t.getEquipment().getItemInMainHand());
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setItemInMainHand(is);
					if(removeFromTarget) t.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
				}
			}
			if((what&2)==2) {
				is=new ItemStack(t.getEquipment().getItemInOffHand());
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setItemInOffHand(is);
					if(removeFromTarget) t.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
				}
			}
			if((what&4)==4) {
				is=t.getEquipment().getHelmet()!=null?new ItemStack(t.getEquipment().getHelmet()):new ItemStack(Material.AIR);
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setHelmet(is);
					if(removeFromTarget) t.getEquipment().setHelmet(new ItemStack(Material.AIR));
				}
				is=t.getEquipment().getChestplate()!=null?new ItemStack(t.getEquipment().getChestplate()):new ItemStack(Material.AIR);
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setChestplate(is);
					if(removeFromTarget) t.getEquipment().setChestplate(new ItemStack(Material.AIR));
				}
				is=t.getEquipment().getLeggings()!=null?new ItemStack(t.getEquipment().getLeggings()):new ItemStack(Material.AIR);
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setLeggings(is);
					if(removeFromTarget) t.getEquipment().setLeggings(new ItemStack(Material.AIR));
				}
				is=t.getEquipment().getBoots()!=null?new ItemStack(t.getEquipment().getBoots()):new ItemStack(Material.AIR);
				if(is.getType()!=Material.AIR||!ignoreAir) {
					e.getEquipment().setBoots(is);
					if(removeFromTarget) t.getEquipment().setBoots(new ItemStack(Material.AIR));
				}
			}
		}
		return true;
	}

}
