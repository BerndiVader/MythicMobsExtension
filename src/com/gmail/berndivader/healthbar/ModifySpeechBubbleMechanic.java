package com.gmail.berndivader.healthbar;

import java.util.ArrayList;

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
	private String text,id,s1;
	private float offset;
	private double so,fo;
	private int ll,i1;

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
		this.i1=mlc.getInteger("timer",-999);
		this.s1=mlc.getString("usecounter","").toLowerCase();
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId().toString()+this.id)) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId().toString()+this.id);
			if (this.text!=null) {
				String s1=this.text;
				s1=SkillString.unparseMessageSpecialChars(s1);
				s1=SkillString.parseMobVariables(s1,data.getCaster(),target,data.getTrigger());
				ArrayList<String>li1=new ArrayList<String>();
				String[]a2=s1.split("<nl>");
				for(int i=0;i<a2.length;i++) {
					String[]a4=CustomSkillStuff.wrapStr(a2[i],ll);
					for (int i1=0;i1<a4.length;i1++) {
						li1.add(a4[i1]);
					}
				}
				sb.template=li1.toArray(new String[li1.size()]);
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
			if (this.i1!=-999) sb.counter=this.i1;
			i(sb);
			return true;
		}
		return false;
	}
	
	private void i(SpeechBubble sb) {
		switch(this.s1) {
		case "true": {
			sb.uc1=true;
			break;
		}
		case "false": {
			sb.uc1=false;
			break;
		}
		}
	}


}
