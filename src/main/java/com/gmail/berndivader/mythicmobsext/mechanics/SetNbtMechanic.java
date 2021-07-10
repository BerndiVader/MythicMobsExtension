package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "setnbt", author = "BerndiVader")
public class SetNbtMechanic extends SkillMechanic implements ITargetedEntitySkill, INoTargetSkill {
	PlaceholderString s1;

	public SetNbtMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		String tmp = mlc.getString("nbt");
		if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
			tmp = SkillString.parseMessageSpecialChars(tmp.substring(1, tmp.length() - 1));
		} else {
			tmp = "{}";
		}
		s1 = new PlaceholderString(tmp);
	}

	@Override
	public boolean cast(SkillMetadata var1) {
		Entity e = var1.getCaster().getEntity().getBukkitEntity();
		return setNbt(e, s1.get(var1));
	}

	@Override
	public boolean castAtEntity(SkillMetadata var1, AbstractEntity var2) {
		Entity e = var2.getBukkitEntity();
		return setNbt(e, s1.get(var1, var2));
	}

	boolean setNbt(Entity e1, String s1) {
		return Volatile.handler.addNBTTag(e1, s1);
	}

}
