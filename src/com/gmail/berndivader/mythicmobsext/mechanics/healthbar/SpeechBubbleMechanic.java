package com.gmail.berndivader.mythicmobsext.mechanics.healthbar;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.utils.Utils;

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
	private String id;
	private double offset;
	private double so;
	private double fo;
	private int ll;
	private int time;
	private boolean b1;
	private boolean b2;

	public SpeechBubbleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.id=mlc.getString("id","bubble");
		this.offset=mlc.getDouble(new String[] { "offset", "o" }, 2.1D);
		this.so=mlc.getDouble(new String[] { "sideoffset", "so"}, 0D);
		this.fo=mlc.getDouble(new String[] { "forwardoffset", "fo"}, 0D);
		this.ll=mlc.getInteger(new String[] { "linelength", "ll" }, 20);
		this.time=mlc.getInteger(new String[] { "counter", "c","time" }, 200);
		this.b1=mlc.getBoolean(new String[] { "animation", "anim", "a" }, true);  
		this.b2=mlc.getBoolean(new String[] { "usecounter", "uc" }, true);
		this.text=mlc.getString(new String[] { "display","text","t" }, "");
		if (text.startsWith("\"") && text.endsWith("\"")) {
			this.text=text.substring(1,text.length()-1);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!data.getCaster().getEntity().isLiving()) return false;
		if (HealthbarHandler.speechbubbles.containsKey(data.getCaster().getEntity().getUniqueId().toString()+this.id)) {
			SpeechBubble sb=HealthbarHandler.speechbubbles.get(data.getCaster().getEntity().getUniqueId().toString()+this.id);
			sb.remove();
		}
		LivingEntity entity=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		String txt=this.text;
		txt=SkillString.unparseMessageSpecialChars(txt);
		txt=Utils.parseMobVariables(txt,data,data.getCaster().getEntity(),target,null);
		Location l1=entity.getLocation().clone();
		ArrayList<String>li1=new ArrayList<String>();
		String[]a2=txt.split("<nl>");
		for(int i=0;i<a2.length;i++) {
			String[]a4=Utils.wrapStr(a2[i],ll);
			for (int i1=0;i1<a4.length;i1++) {
				li1.add(a4[i1]);
			}
		}
		String[]a1=li1.toArray(new String[li1.size()]);
		if (!b1) {
			if (this.so!=0d||this.fo!=0d) {
				l1.add(Utils.getSideOffsetVector(l1.getYaw(),this.so,false));
				l1.add(Utils.getFrontBackOffsetVector(l1.getDirection(),this.fo));
			}
			l1.add(0,(a1.length*0.25)+this.offset,0);
		} else {
			l1.add(0,entity.getEyeHeight(),0);
		}
		new SpeechBubble(entity,this.id,l1,this.offset,this.time,a1,this.so,this.fo,b1,this.ll,this.b2);
		return true;
	}

}
