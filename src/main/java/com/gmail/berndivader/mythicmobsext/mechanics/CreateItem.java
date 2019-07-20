package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="giveitem",author="BerndiVader")
public 
class
CreateItem 
extends
SkillMechanic
implements
ITargetedEntitySkill,
INoTargetSkill
{
	
	static ItemManager itemmanager=Utils.mythicmobs.getItemManager();
	
	String item_name,bag_name,click_skill;
	HoldingItem holding;
	boolean override;
	int amount;
	
	public CreateItem(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		holding=new HoldingItem();
		this.holding.setWhere(mlc.getString("to","inventory"));
		this.holding.setSlot(mlc.getString("slot","-1"));
		this.holding.setBagName(mlc.getString("bagname"));
		
		this.item_name=mlc.getString("item");
		this.amount=mlc.getInteger("amount",1);
		this.override=mlc.getBoolean("override",true);
		this.click_skill=mlc.getString("clickskill");
	}

	@Override
	public boolean cast(SkillMetadata data) {
		castAtEntity(data,data.getCaster().getEntity());
		return false;
	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		HoldingItem holding=this.holding.clone();
		if(holding!=null) {
			if(item_name==null||!abstract_entity.isLiving()) return false;
			holding.parseSlot(data,abstract_entity);
			if(bag_name!=null) holding.setBagName(Utils.parseMobVariables(this.bag_name,data,data.getCaster().getEntity(),abstract_entity,null));
			ItemStack item_stack=itemmanager.getItemStack(this.item_name);
			if(item_stack!=null) {
				item_stack.setAmount(amount);
				if(this.click_skill!=null) {
					item_stack=NMSUtils.makeReal(item_stack);
					NMSUtils.setMeta(item_stack,Utils.meta_CLICKEDSKILL,this.click_skill);
				}
				HoldingItem.giveItem((LivingEntity)abstract_entity.getBukkitEntity(),holding,item_stack,override);
				return true;
			}
		}
		return false;
	}

}
