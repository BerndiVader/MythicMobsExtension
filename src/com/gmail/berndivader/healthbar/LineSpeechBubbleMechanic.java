package com.gmail.berndivader.healthbar;

import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class LineSpeechBubbleMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	private String oline;
	private String nline;
	private String id;
	private String cmp;

	public LineSpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String ol=mlc.getString(new String[] {"oldline","ol"},null);
		String nl=mlc.getString(new String[] {"newline","nl"},null);
		this.id=mlc.getString("id","bubble");
		if (nl!=null) {
			if (nl.startsWith("\"") && nl.endsWith("\"")) {
				this.nline=SkillString.parseMessageSpecialChars((nl.substring(1,nl.length()-1)));
			} else {
				this.nline=nl;
			}
		}
		if (ol!=null) {
			if (ol.startsWith("\"") && ol.endsWith("\"")) {
				this.oline=SkillString.unparseMessageSpecialChars(ol.substring(1,ol.length()-1));
			} else {
				this.oline=ol;
			}
		}
		this.cmp=mlc.getString("mode","replace").toLowerCase();
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId().toString()+this.id)) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId().toString()+this.id);
			if (this.oline!=null) {
				String s2="";
				String s1=Utils.parseMobVariables(this.oline,data,target,data.getTrigger(),null);
				if (this.nline!=null) s2=Utils.parseMobVariables(this.nline,data,target,data.getTrigger(),null);
				if (this.cmp.equals("append")) {
					String[]arr1=sb.template;
					String[]arr2=new String[]{s2};
					String[]arr=new String[arr1.length+arr2.length];
					System.arraycopy(arr1,0,arr,0,arr1.length);
					System.arraycopy(arr2,0,arr,arr1.length,arr2.length);
					sb.template=arr;
					arr=null;arr1=null;arr2=null;
				} else {
					for(int i=0;i<sb.template.length;i++) {
						if (!sb.template[i].contains(s1)) continue;
						if (this.cmp.equals("replace")) {
							sb.template[i]=s2;
							continue;
						}
						if (this.cmp.equals("remove")) {
							String[]arr1=sb.template;
							String[]arr=new String[arr1.length-1];
							if (i>=0&&arr.length>0) {
				                System.arraycopy(arr1,0,arr,0,i);
				                System.arraycopy(arr1,i+1,arr,i,arr.length-i);								
							}
						    sb.template=arr;
						    i=-1;
						}
					}
				}
				sb.lines();
			}
			return true;
		}
		return false;
	}
}
