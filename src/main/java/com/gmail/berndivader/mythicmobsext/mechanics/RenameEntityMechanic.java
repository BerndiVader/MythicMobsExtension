package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;

@ExternalAnnotation(name="renameentity",author="BerndiVader")
public class RenameEntityMechanic extends SkillMechanic
implements
ITargetedEntitySkill {
	
	String name;
	boolean v;
	public RenameEntityMechanic(String line, MythicLineConfig mlc) {
		super(line,mlc);
		name=mlc.getString(new String[] { "name", "n" }, "");
		if (name.charAt(0)=='"'&&name.charAt(name.length()-1)=='"') {
			name=name.substring(1,name.length()-1);
		}
		name=SkillString.parseMessageSpecialChars(name);
		this.v=mlc.getBoolean(new String[] { "visible", "v" }, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()&&!target.isPlayer()&&this.name!=null) {
			String n=Utils.parseMobVariables(this.name,data,data.getCaster().getEntity(),target,null);
			LivingEntity e = (LivingEntity)target.getBukkitEntity();
			e.setCustomName(n);
			e.setCustomNameVisible(this.v);
			return true;
		}
		return false;
	}
}
