package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
CreateBackBag 
extends
SkillMechanic
implements
INoTargetSkill
{
	int size;
	ItemStack[] default_items=null;
	String bag_name;
	boolean temporary,override;

	public CreateBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		size=mlc.getInteger("size",9);
		default_items=BackBagHelper.createDefaultItemStack(mlc.getString("items",null));
		bag_name=mlc.getString(new String[] {"title","name"},BackBagHelper.str_name);
		temporary=mlc.getBoolean("temporary",false);
		override=mlc.getBoolean("override",true);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return (new BackBag(data.getCaster().getEntity().getBukkitEntity(),size,default_items,Utils.parseMobVariables(bag_name,data,data.getCaster().getEntity(),data.getCaster().getEntity(),null),temporary,override))!=null;
	}

}
