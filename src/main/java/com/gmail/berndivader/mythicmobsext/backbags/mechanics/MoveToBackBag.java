package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
MoveToBackBag 
extends
SkillMechanic
implements
INoTargetSkill,
ITargetedEntitySkill
{
	int backbag_slot,slot;
	WhereEnum what;
	boolean override;
	String meta_name;
	HoldingItem holding;
	
	public MoveToBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		what=WhereEnum.get(mlc.getString("what","head"));
		slot=mlc.getInteger("slot",-1);
		backbag_slot=mlc.getInteger("bagslot",-1);
		override=mlc.getBoolean("override",true);
		meta_name=mlc.getString("meta","");
		holding=new HoldingItem();
		holding.setWhere(what);
		holding.setSlot(slot);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data,data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isLiving()&&BackBagHelper.hasBackBag(abstract_entity.getUniqueId())) {
			LivingEntity holder=(LivingEntity)abstract_entity.getBukkitEntity();
			if(holder.hasMetadata(meta_name)) holder.removeMetadata(meta_name,Main.getPlugin());
			BackBag bag=new BackBag(holder);
			Inventory inventory=bag.getInventory();
			List<ItemStack>stack=HoldingItem.getContents(holding,holder);
			for(int i1=0;i1<stack.size();i1++) {
				ItemStack old_item=stack.get(i1);
				if(old_item==null) continue;
				ItemStack new_item=old_item.clone();
				int tmp_slot=backbag_slot;
				if(backbag_slot==-1) {
					if((tmp_slot=inventory.firstEmpty())>-1) {
						inventory.addItem(new_item);
						setMetaValue(old_item,holder,meta_name,tmp_slot);
					}
				} else {
					if(backbag_slot>inventory.getSize()) continue;
					if(override) {
						inventory.setItem(backbag_slot,new_item);
						setMetaValue(old_item,holder,meta_name,backbag_slot);
					} else if(inventory.getItem(backbag_slot)==null||inventory.getItem(backbag_slot).getType()==Material.AIR){
						inventory.setItem(backbag_slot,new_item);
						setMetaValue(old_item,holder,meta_name,backbag_slot);
					}
				}
			}
		}
		return true;
	}
	
	static void setMetaValue(ItemStack old_item,LivingEntity holder,String meta_name,int backbag_slot) {
		old_item.setAmount(0);
		old_item.setType(Material.AIR);
		if(meta_name.length()>0) {
			holder.setMetadata(meta_name,new FixedMetadataValue(Main.getPlugin(),backbag_slot));
		}
	}
}
