package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "renameentity", author = "BerndiVader")
public class RenameEntityMechanic extends SkillMechanic implements ITargetedEntitySkill {
	PlaceholderString name;
	boolean v;

	public RenameEntityMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String tmp = mlc.getString(new String[] { "name", "n" }, "");
		if (tmp.charAt(0) == '"' && tmp.charAt(tmp.length() - 1) == '"') {
			tmp = tmp.substring(1, tmp.length() - 1);
		}
		name = new PlaceholderString(SkillString.parseMessageSpecialChars(tmp));
		this.v = mlc.getBoolean(new String[] { "visible", "v" }, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving() && !target.isPlayer() && this.name != null) {
			String n = this.name.get(data, target);
			LivingEntity e = (LivingEntity) target.getBukkitEntity();
			e.setCustomName(n);
			e.setCustomNameVisible(this.v);
			return true;
		}
		return false;
	}
}
