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

@ExternalAnnotation(name = "exchange,exchangeweaponry", author = "BerndiVader")
public class ExchangeWeaponry extends SkillMechanic implements ITargetedEntitySkill {
	
	private final String destination;
	private final String where;
	
	public ExchangeWeaponry(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
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
				equipment.setItemInMainHand(getItem(where,equipment));
				break;
			case "OFFHAND":
				equipment.setItemInOffHand(getItem(where,equipment));
				break;
			case "HELMET":
				equipment.setHelmet(getItem(where,equipment));
				break;
			case "CHESTPLATE":
				equipment.setChestplate(getItem(where,equipment));
				break;
			case "LEGGINGS":
				equipment.setLeggings(getItem(where,equipment));
				break;
			case "BOOTS":
				equipment.setBoots(getItem(where,equipment));
				break;
		}
		return true;
	}

	static boolean isValidMaterial(ItemStack stack) {
		return stack != null && stack.getType() != Material.AIR;
	}
	
	public ItemStack getItem(String where, EntityEquipment equipment) {
		ItemStack iS = null;
		switch (where) {
			case "HAND":
				iS = equipment.getItemInMainHand().clone();
				equipment.setItemInMainHand(new ItemStack(Material.AIR));
				break;
			case "OFFHAND":
				iS = equipment.getItemInOffHand().clone();
				equipment.setItemInOffHand(new ItemStack(Material.AIR));
				break;
			case "HELMET":
				iS = equipment.getHelmet().clone();
				equipment.setHelmet(new ItemStack(Material.AIR));
				break;
			case "CHESTPLATE":
				iS = equipment.getChestplate().clone();
				equipment.setChestplate(new ItemStack(Material.AIR));
				break;
			case "LEGGINGS":
				iS = equipment.getLeggings().clone();
				equipment.setLeggings(new ItemStack(Material.AIR));
				break;
			case "BOOTS":
				iS = equipment.getBoots().clone();
				equipment.setBoots(new ItemStack(Material.AIR));
				break;
		}
		return iS;
	}

}
