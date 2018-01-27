package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class SetNbt 
extends 
SkillMechanic
implements
ITargetedEntitySkill,
INoTargetSkill {
	String s1;

	public SetNbt(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		s1=mlc.getString("nbt");
		if (s1.startsWith("\"")&&s1.endsWith("\"")) {
			s1=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
		} else {
			s1="{}";
		}
	}

	@Override
	public boolean cast(SkillMetadata var1) {
		Entity e=var1.getCaster().getEntity().getBukkitEntity();
		return setNbt(e,Utils.parseMobVariables(s1,var1,var1.getCaster().getEntity(),null,null));
	}

	@Override
	public boolean castAtEntity(SkillMetadata var1, AbstractEntity var2) {
		Entity e=var2.getBukkitEntity();
		return setNbt(e,Utils.parseMobVariables(s1,var1,var1.getCaster().getEntity(),var2,null));
	}
	
	boolean setNbt(Entity e1,String s1) {
		return Main.getPlugin().getVolatileHandler().addNBTTag(e1,s1);
	}

}
