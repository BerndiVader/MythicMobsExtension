package com.gmail.berndivader.healthbar;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

public class SpeechBubble
extends
CraftHologram {
	protected LivingEntity entity;
	protected UUID uuid;
	protected double offset,sOffset,fOffset;
	protected String[] template;
	protected TextLine textline;
	protected int counter,maxlines;
	protected boolean useOffset;
	
	public SpeechBubble(LivingEntity entity, String[] text) {
		this(entity,entity.getLocation(),0d,-1,text,0d,0d,false,30);
	}
	public SpeechBubble(LivingEntity entity,String[] text,int ll) {
		this(entity,entity.getLocation(),0d,-1,text,0d,0d,false,ll);
	}
	public SpeechBubble(LivingEntity entity,Location l1,double offset,int showCounter, String[] text, double sOffset, double fOffset, boolean b1,int ll) {
		super(l1);
		this.fOffset=fOffset;
		this.sOffset=sOffset;
		this.maxlines=-1;
		this.useOffset=fOffset!=0d||sOffset!=0d;
		if (this.useOffset&&b1) {
			Vector soV=CustomSkillStuff.getSideOffsetVector(entity.getLocation().getYaw(), this.sOffset,false);
			Vector foV=CustomSkillStuff.getFrontBackOffsetVector(entity.getLocation().getDirection(),this.fOffset);
			this.getLocation().add(soV);
			this.getLocation().add(foV);
		}
		this.uuid=entity.getUniqueId();
		HealthbarHandler.speechbubbles.put(this.uuid, this);
		this.counter=showCounter<1?60:showCounter*20;
		this.getVisibilityManager().setVisibleByDefault(true);
		this.counter=showCounter;
		this.template=text;
		this.entity=entity;
		this.offset=offset;
		for(String l:this.template) {
			this.appendTextLine(l);
		}
	}

	@Override
	public boolean update() {
		if (this.isDeleted()) return false;
		Location l = this.entity.getLocation();
		World w=l.getWorld();
		double dx=l.getX();
		double dy=l.getY();
		double dz=l.getZ();
		double do1=(this.getLinesLength()*0.25)+this.offset;
		if (this.useOffset) {
			Vector soV=CustomSkillStuff.getSideOffsetVector(entity.getLocation().getYaw(), this.sOffset,false);
			Vector foV=CustomSkillStuff.getFrontBackOffsetVector(entity.getLocation().getDirection(),this.fOffset);
			dx+=soV.getX()+foV.getX();
			dz+=soV.getZ()+foV.getZ();
		}
		this.teleport(w,dx,dy+do1,dz);
		this.counter--;
		if (this.counter<0) this.remove();
		return true;
	}
	
	public void remove() {
		HealthbarHandler.speechbubbles.remove(this.uuid);
		this.delete();
	}
	
}
