package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public
class
OpenBackBag 
extends
SkillMechanic
implements
INoTargetSkill,
ITargetedEntitySkill
{
	
	int size;
	ItemStack[] default_items;
	boolean view_only,temporary;
	PlaceholderString bag_name;

	public OpenBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		size=mlc.getInteger("size",9);
		view_only=mlc.getBoolean("viewonly",true);
		default_items=BackBagHelper.createDefaultItemStack(mlc.getString("items",null));
		bag_name=mlc.getPlaceholderString(new String[] {"name","title"},"BackBag");
		temporary=mlc.getBoolean("temporary",false);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if(data.getCaster().getEntity().isPlayer()) {
			Player player=(Player)data.getCaster().getEntity().getBukkitEntity();
			BackBag bag=new BackBag(player,size,default_items,bag_name.get(data),temporary);
			bag.viewBackBag(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			Entity holder=data.getCaster().getEntity().getBukkitEntity();
			Player viewer=(Player)abstract_entity.getBukkitEntity();
			BackBag bag=new BackBag(holder,size,default_items,bag_name.get(data,abstract_entity),temporary);
			bag.viewBackBag(viewer,view_only);
			return true;
		}
		return false;
	}
}
