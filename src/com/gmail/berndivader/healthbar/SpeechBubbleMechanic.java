package com.gmail.berndivader.healthbar;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class SpeechBubbleMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	private String text;
	private int ll,time;
	private float offset;

	public SpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.text=mlc.getString(new String[] {"text","t"},"");
		this.ll=mlc.getInteger(new String[] {"linelength","ll"},20);
		this.offset=mlc.getFloat(new String[] {"offset","yo"},2.1f);
		this.time=mlc.getInteger(new String[] {"time","ti"},20);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!data.getCaster().getEntity().isLiving()) return false;
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId())) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId());
			sb.remove();
		}
		LivingEntity entity=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		String txt=this.text;
		if (txt.startsWith("\"")
				&&txt.endsWith("\"")) {
			txt=txt.substring(1,txt.length()-1);
		}
		txt=SkillString.unparseMessageSpecialChars(txt);
		txt=SkillString.parseMobVariables(txt, data.getCaster(), target, data.getTrigger());
		new SpeechBubble(entity, this.offset, this.time, txt, 0, 0, false, this.ll);
		return true;
	}

}
