package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
DropBackBagItem 
extends
SkillMechanic
implements
INoTargetSkill,
ITargetedLocationSkill
{
	int slot;
	boolean all;
	List<ItemStack>items;
	static ItemManager itemmanager=Utils.mythicmobs.getItemManager();
	
	public DropBackBagItem(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		slot=mlc.getInteger("slot",-1);
		all=mlc.getBoolean("dropall",false);
		
		items=new ArrayList<ItemStack>();
		
		String[]parse_list=mlc.getString("items").split(",");
		for(int i1=0;i1<parse_list.length;i1++) {
			String[]parse_item=parse_list[i1].split(":");
			int amount=1;
			if(parse_item.length>1) amount=Integer.parseInt(parse_item[1]);
			ItemStack item=itemmanager.getItemStack(parse_item[0]);
			if(item!=null) {
				item.setAmount(amount);
				items.add(item.clone());
			}
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return false;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation abstract_location) {
		return false;
	}
}
