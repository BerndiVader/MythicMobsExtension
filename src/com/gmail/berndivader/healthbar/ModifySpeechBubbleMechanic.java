package com.gmail.berndivader.healthbar;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class ModifySpeechBubbleMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	private String text,id;
	private float offset;
	private double so,fo;
	private int ll;

	public ModifySpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.text=mlc.getString(new String[] {"text","t"},null);
		this.id=mlc.getString("id","bubble");
		this.ll=mlc.getInteger(new String[] {"linelength","ll"},20);
		if (text!=null) {
			if (text.startsWith("\"")
					&&text.endsWith("\"")) {
					this.text=text.substring(1,text.length()-1);
				}
		}
		this.offset=mlc.getFloat(new String[] {"offset","yo"},-999);
		this.so=mlc.getDouble("so",-999);
		this.fo=mlc.getDouble("fo",-999);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId().toString()+this.id)) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId().toString()+this.id);
			if (this.text!=null) {
				String s1=this.text;
				s1=SkillString.unparseMessageSpecialChars(s1);
				s1=SkillString.parseMobVariables(s1,data.getCaster(),target,data.getTrigger());
				sb.clearLines();
				sb.il1=0;
				sb.template=CustomSkillStuff.wrapStr(s1,ll);
				sb.lines();
			}
			if (this.offset!=-999) sb.offset=this.offset;
			if (this.so!=-999) {
				sb.sOffset=this.so;
				sb.useOffset=true;
			}
			if (this.fo!=-999) {
				sb.fOffset=this.fo;
				sb.useOffset=true;
			}
			return true;
		}
		return false;
	}

}
