package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

@ExternalAnnotation(name="custommessage,mmemessage,sendmessage",author="BerndiVader")
public class MessageMechanic 
extends 
SkillMechanic
implements 
ITargetedEntitySkill {
	String msg;

	public MessageMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.target_creative=true;
		msg=mlc.getString(new String[]{"msg","m"},null);
		if (msg!=null&&(msg.startsWith("\"")&&msg.endsWith("\""))) {
			msg=msg.substring(1,msg.length()-1);
			msg=SkillString.parseMessageSpecialChars(msg);
		} else {
			msg="Invalid msg format in config of: "+line;
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e1) {
		if (e1.isPlayer()) e1.getBukkitEntity().sendMessage(Utils.parseMobVariables(msg,data,data.getCaster().getEntity(),e1,null));
		return true;
	}
}
