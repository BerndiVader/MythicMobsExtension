package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
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

@ExternalAnnotation(name = "exchange,exchangeweaponry", author = "BerndiVader")
public class ExchangeWeaponry extends SkillMechanic implements ITargetedEntitySkill {
	
	private final String destination;
	private final String where;
	
	public ExchangeWeaponry(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		this.destination = mlc.getString(new String[] { "destination", "d" }, "OFFHAND");
		this.where = mlc.getString(new String[] { "where", "w" }, "HAND");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isLiving())
			return false;
		LivingEntity entity = (LivingEntity) target.getBukkitEntity();
		EntityEquipment equipment = entity.getEquipment();
		
		switch (destination) {
			case "HAND":
				equipment.setItemInMainHand(getItem(where,equipment,true));
				break;
			case "OFFHAND":
				equipment.setItemInOffHand(getItem(where,equipment,true));
				break;
			case "HELMET":
				equipment.setHelmet(getItem(where,equipment,true));
				break;
			case "CHESTPLATE":
				equipment.setChestplate(getItem(where,equipment,true));
				break;
			case "LEGGINGS":
				equipment.setLeggings(getItem(where,equipment,true));
				break;
			case "BOOTS":
				equipment.setBoots(getItem(where,equipment,true));
				break;
		}
		return true;
	}

	static boolean isValidMaterial(ItemStack stack) {
		return stack != null && stack.getType() != Material.AIR;
	}
	
	public ItemStack getItem(String where, EntityEquipment equipment, boolean replace) {
		ItemStack iS = null;
		switch (where) {
			case "HAND":
				iS = equipment.getItemInMainHand().clone();
				if(replace) equipment.setItemInMainHand(getItem(destination,equipment, false));
				break;
			case "OFFHAND":
				iS = equipment.getItemInOffHand().clone();
				if(replace) equipment.setItemInOffHand(getItem(destination,equipment, false));
				break;
			case "HELMET":
				iS = equipment.getHelmet().clone();
				if(replace) equipment.setHelmet(getItem(destination,equipment, false));
				break;
			case "CHESTPLATE":
				iS = equipment.getChestplate().clone();
				if(replace) equipment.setChestplate(getItem(destination,equipment, false));
				break;
			case "LEGGINGS":
				iS = equipment.getLeggings().clone();
				if(replace) equipment.setLeggings(getItem(destination,equipment, false));
				break;
			case "BOOTS":
				iS = equipment.getBoots().clone();
				if(replace) equipment.setBoots(getItem(destination,equipment, false));
				break;
		}
		return iS;
	}

}
