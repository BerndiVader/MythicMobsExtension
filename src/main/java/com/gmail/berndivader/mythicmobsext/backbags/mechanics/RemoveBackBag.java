package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
RemoveBackBag 
extends
SkillMechanic
implements
INoTargetSkill
{
	String bag_name;
	boolean all;
	
	public RemoveBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		
		bag_name=mlc.getString(new String[] {"title","name"},BackBagHelper.str_name);
		all=mlc.getBoolean("all",false);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if(all) {
			BackBagHelper.removeAll(data.getCaster().getEntity().getUniqueId());
		} else {
			BackBagHelper.remove(data.getCaster().getEntity().getUniqueId(),Utils.parseMobVariables(bag_name,data,data.getCaster().getEntity(),data.getCaster().getEntity(),null));
		}
		return true;
	}
}
