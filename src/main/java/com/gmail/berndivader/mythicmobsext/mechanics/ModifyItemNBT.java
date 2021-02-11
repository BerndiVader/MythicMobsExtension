package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;
import io.lumine.xikage.mythicmobs.skills.variables.VariableMechanic;

@ExternalAnnotation(name = "modifyitemnbt,setitemnbt", author = "Seyarada")
public class ModifyItemNBT extends VariableMechanic implements ITargetedEntitySkill {

	private final String where;
	private final String NBTkey;
	private final PlaceholderString NBTvalue;
	private SkillMetadata skill;
	private AbstractEntity abstract_entity;
	
	public ModifyItemNBT(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.where = mlc.getString(new String[] { "where", "w" }, "HAND");
		this.NBTkey = mlc.getString(new String[] { "key", "k" }, "Hello");
		this.NBTvalue = PlaceholderString.of(mlc.getString(new String[] { "value", "v" }, "World"));
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		skill = data; abstract_entity = target;
		if (!target.isLiving())
			return false;
		LivingEntity entity = (LivingEntity) target.getBukkitEntity();
		EntityEquipment equipment = entity.getEquipment();
		
		switch (where) {
			case "HAND":
				equipment.setItemInMainHand(setItemNBT(equipment.getItemInMainHand().clone()));
				break;
			case "OFFHAND":
				equipment.setItemInOffHand(setItemNBT(equipment.getItemInOffHand().clone()));
				break;
			case "HELMET":
				equipment.setHelmet(setItemNBT(equipment.getHelmet().clone()));
				break;
					case "CHESTPLATE":
				equipment.setChestplate(setItemNBT(equipment.getChestplate().clone()));
				break;
			case "LEGGINGS":
				equipment.setLeggings(setItemNBT(equipment.getLeggings().clone()));
				break;
			case "BOOTS":
				equipment.setBoots(setItemNBT(equipment.getBoots().clone()));
				break;
		}
		return true;
	}
	
	
	public ItemStack setItemNBT(ItemStack iS) {
	    Map<String, Object> tags = new HashMap<String, Object>();
	    tags.put(NBTkey, NBTvalue.get(skill, abstract_entity));
	    iS = MythicItem.addItemNBT(iS, "Base", tags);
		return iS;
	}
}
