package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.Enchant;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem.ACTION;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

@ExternalAnnotation(name="modifyitem",author="BerndiVader")
public 
class
ModifyItem 
extends
SkillMechanic
implements
ITargetedEntitySkill 
{
	String slot;
	ModdingItem modding_item;
	
	public ModifyItem(String skill, MythicLineConfig mlc) {
		super(skill,mlc);
		
		WhereEnum where=Utils.enum_lookup(WhereEnum.class,mlc.getString("what","HAND").toUpperCase());
		slot=mlc.getString("slot","-7331");
		
		ACTION action=Utils.enum_lookup(ACTION.class,mlc.getString("action","SET").toUpperCase());
		Material material=null;
		RandomDouble amount=null;
		List<Enchant>enchants=null;
		String[]lore=null;
		String name=null,bag_name=null,duration=null;
		
		String temp=mlc.getString("material");
		if(temp!=null) temp=temp.toUpperCase();
		material=Utils.enum_lookup(Material.class,temp);
		temp=SkillString.parseMessageSpecialChars(mlc.getString("lore"));
		if(temp!=null) lore=(temp.substring(1,temp.length()-1)).split(",");
		if((temp=SkillString.parseMessageSpecialChars(mlc.getString("name")))!=null) name=temp.substring(1,temp.length()-1);
		if((temp=mlc.getString("amount"))!=null) amount=new RandomDouble(temp);
		if((temp=mlc.getString("bagname"))!=null) bag_name=temp;
		if((temp=mlc.getString("duration"))!=null) duration=temp;
		if((temp=mlc.getString("enchants",null))!=null) {
			String[]arr1=temp.toUpperCase().split(",");
			int length=arr1.length;
			if(length>0) enchants=new ArrayList<>();
			for(int i1=0;i1<length;i1++) {
				String parse[]=arr1[i1].split(":");
				temp=parse[0];
				Enchantment ench;
				String level="1";
				if(parse.length>0) {
					try{
						level=parse[1];
					} catch(Exception ex) {
						Main.logger.warning("Error parsing level in "+line);
						level="1";
					}
				}
				if((ench=Enchantment.getByName(name))!=null) {
					enchants.add(new Enchant(ench,level));
				} else {
					Main.logger.warning("Ignore enchantment "+name+" in "+line);
				}
			}
		}
		modding_item=new ModdingItem(where,slot,action,material,lore,name,amount,enchants,duration,bag_name);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(target.isLiving()) {
			LivingEntity entity=(LivingEntity)target.getBukkitEntity();
			ItemStack item_stack=modding_item.getItemStackByWhere(data,target,entity);
			if(item_stack!=null) item_stack=modding_item.applyMods(data,target,item_stack);
			return true;
		}
		return false;
	}
}