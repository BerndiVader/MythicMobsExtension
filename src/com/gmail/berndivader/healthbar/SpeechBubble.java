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
	protected int counter;
	protected boolean useOffset,iYaw;
	
	public SpeechBubble(LivingEntity entity, String text) {
		this(entity,0d,-1,text,0d,0d,false,30);
	}
	public SpeechBubble(LivingEntity entity,String text,int ll) {
		this(entity,0d,-1,text,0d,0d,false,ll);
	}
	public SpeechBubble(LivingEntity entity, double offset,int showCounter, String text, double sOffset, double fOffset, boolean ignoreYaw,int ll) {
		super(entity.getLocation().add(0, offset, 0));
		this.fOffset=fOffset;
		this.sOffset=sOffset;
		this.iYaw=ignoreYaw;
		this.useOffset=fOffset!=0d||sOffset!=0d;
		if (this.useOffset) {
			Vector soV=CustomSkillStuff.getSideOffsetVector(entity.getLocation().getYaw(), this.sOffset, this.iYaw);
			Vector foV=CustomSkillStuff.getFrontBackOffsetVector(entity.getLocation().getDirection(),this.fOffset);
			this.getLocation().add(soV);
			this.getLocation().add(foV);
		}
		this.uuid=entity.getUniqueId();
		HealthbarHandler.speechbubbles.put(this.uuid, this);
		if (showCounter<1) showCounter=30;
		this.counter=showCounter*20;
		this.getVisibilityManager().setVisibleByDefault(true);
		this.counter=showCounter;
		this.template = CustomSkillStuff.wrapStr(text,ll);
		this.entity = entity;
		this.offset = offset;
		for(String l:this.template) {
			this.appendTextLine(l);
		}
	}

	@Override
	public boolean update() {
		if (this.isDeleted()) return false;
		Location l = this.entity.getLocation();
		World w = l.getWorld();
		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
		double o=this.offset+this.template.length*0.25;
		if (this.useOffset) {
			Vector soV=CustomSkillStuff.getSideOffsetVector(entity.getLocation().getYaw(), this.sOffset, this.iYaw);
			Vector foV=CustomSkillStuff.getFrontBackOffsetVector(entity.getLocation().getDirection(),this.fOffset);
			x+=soV.getX()+foV.getX();
			z+=soV.getZ()+foV.getZ();
		}
		this.teleport(w,x,y+o,z);
		this.counter--;
		if (this.counter<0) this.remove();
		return true;
	}
	
	public void remove() {
		HealthbarHandler.speechbubbles.remove(this.uuid);
		this.delete();
	}
	
}
