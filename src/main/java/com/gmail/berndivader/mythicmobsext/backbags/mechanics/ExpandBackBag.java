package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
ExpandBackBag 
extends
SkillMechanic
implements
INoTargetSkill,
ITargetedEntitySkill
{
	int size;
	String bag_name;
	
	public ExpandBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		size=mlc.getInteger("size",1);
		bag_name=mlc.getString(new String[] {"title","name"},BackBagHelper.str_name);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data,data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		BackBagHelper.expandBackBag(abstract_entity.getBukkitEntity(),Utils.parseMobVariables(bag_name,data,data.getCaster().getEntity(),abstract_entity,null),size);
		return true;
	}
}
