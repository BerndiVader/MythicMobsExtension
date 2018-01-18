package com.gmail.berndivader.mythicmobsext;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.entity.LivingEntity;

public class RenameEntityMechanic extends SkillMechanic
implements
ITargetedEntitySkill {
	
	String name;
	boolean v;
	public RenameEntityMechanic(String line, MythicLineConfig mlc) {

		super(line,mlc);
		this.name=mlc.getString(new String[] { "name", "n" }, "");
		this.v=mlc.getBoolean(new String[] { "visible", "v" }, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()&&!target.isPlayer()&&this.name!=null) {
			String n= SkillString.parseMobVariables(this.name,data.getCaster(),target,data.getTrigger());
			LivingEntity e = (LivingEntity)target.getBukkitEntity();
			e.setCustomName(n);
			e.setCustomNameVisible(this.v);
			return true;
		}
		return false;
	}
}
