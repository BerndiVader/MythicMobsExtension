package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "custommessage,mmemessage,sendmessage", author = "BerndiVader")
public class MessageMechanic extends SkillMechanic implements ITargetedEntitySkill {
	PlaceholderString msg;

	public MessageMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.target_creative = true;
		String tmp = mlc.getString(new String[] { "msg", "m" }, null);
		if (tmp != null && (tmp.startsWith("\"") && tmp.endsWith("\""))) {
			tmp = tmp.substring(1, tmp.length() - 1);
			tmp = SkillString.parseMessageSpecialChars(tmp);
		} else {
			tmp = "Invalid msg format in config of: " + line;
		}
		msg = new PlaceholderString(tmp);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e1) {
		if (e1.isPlayer())
			e1.getBukkitEntity().sendMessage(this.msg.get(data, e1));
		return true;
	}
}
