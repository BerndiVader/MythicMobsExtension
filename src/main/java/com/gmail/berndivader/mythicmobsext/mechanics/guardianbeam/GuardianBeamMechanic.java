package com.gmail.berndivader.mythicmobsext.mechanics.guardianbeam;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="guardianbeam",author="BerndiVader")
public class GuardianBeamMechanic
extends 
SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill {
	int duration;
	double forwardOffset;
	double sideOffset;
	double yOffset;
	
	public GuardianBeamMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		duration=mlc.getInteger("duration",1);
		forwardOffset=mlc.getDouble("forward",1);
		sideOffset=mlc.getDouble("side",0);
		yOffset=mlc.getDouble("yoffset",1);
		
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		return cast(data,e.getLocation(),e.getBukkitEntity());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation l) {
		return cast(data,l,null);

	}
	public boolean cast(SkillMetadata data, AbstractLocation l,Entity e) {
		
		final Entity caster=data.getCaster().getEntity().getBukkitEntity();
		final Location end=BukkitAdapter.adapt(l);
		
		Vector foV=Utils.getFrontBackOffsetVector(caster.getLocation().getDirection(),forwardOffset);
		final Beam beam=new Beam(caster.getLocation().add(foV).add(0,yOffset,0),end.clone());
		beam.start();
		
		new BukkitRunnable() {
			int t=0;
			Location ol=caster.getLocation().add(Utils.getFrontBackOffsetVector(caster.getLocation().getDirection(),forwardOffset)).add(0,yOffset,0);
			@Override
			public void run() {
				if(t<duration) {
					Location l1=caster.getLocation().add(Utils.getFrontBackOffsetVector(caster.getLocation().getDirection(),forwardOffset)).add(0,yOffset,0);
					Location l=ol.subtract(l1);
					l.setYaw(caster.getLocation().getYaw());
					l.setPitch(caster.getLocation().getPitch());
					beam.setStartingPosition(l);
					ol=l1;
				}else {
					if(beam.isActive()) beam.stop();
					this.cancel();
				}
				t++;
			}
		}.runTaskTimer(Main.getPlugin(),0l,0l);
		
		return false;
	}
	
}
