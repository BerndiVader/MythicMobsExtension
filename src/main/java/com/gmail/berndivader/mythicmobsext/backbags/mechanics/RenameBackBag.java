package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagInventory;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
RenameBackBag 
extends
SkillMechanic
implements
INoTargetSkill
{
	String bag_name,new_bag_name;
	
	public RenameBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		bag_name=mlc.getString(new String[] {"title","name"},BackBagHelper.str_name);
		new_bag_name=mlc.getString(new String[] {"new","newname"},BackBagHelper.str_name);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Entity entity=data.getCaster().getEntity().getBukkitEntity();
		if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
			BackBagInventory bag_inventory=BackBagHelper.getBagInventory(entity.getUniqueId(),Utils.parseMobVariables(bag_name,data,data.getCaster().getEntity(),data.getCaster().getEntity(),null));
			if(bag_inventory!=null) bag_inventory.setName(Utils.parseMobVariables(new_bag_name,data,data.getCaster().getEntity(),data.getCaster().getEntity(),null));
		}
		return true;
	}
}
