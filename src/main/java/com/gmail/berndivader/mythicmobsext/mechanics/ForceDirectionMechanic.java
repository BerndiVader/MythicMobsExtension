package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec2D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="forcedirection",author="BerndiVader")
public class ForceDirectionMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	BlockFace faceing;
	long duration;
	double noise;

	public ForceDirectionMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		faceing=BlockFace.valueOf(mlc.getString("faceing","north").toUpperCase());
		duration=mlc.getInteger("duration",1);
		noise=mlc.getDouble("noise",0.0d);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(!target.isPlayer()) return false;
		Player p=(Player)target.getBukkitEntity();
		Location source=p.getEyeLocation();
		double dx=0,dz=0;
		switch(faceing){
		case NORTH:
			dz=-1;
			break;
		case SOUTH:
			dz=1;
			break;
		case WEST:
			dx=-1;
			break;
		case EAST:
			dx=1;
			break;
		default:
			break;
		}
		final Location dd=source.clone().add(dx,0,dz);
		final long d=duration;
		final double noise=this.noise;
		new BukkitRunnable() {
			long count=0;
			@Override
			public void run() {
				double dx=0,dy=0,dz=0;
				if (p==null||p.isDead()||count>d) {
					this.cancel();
				} else {
					if(noise>0.0d) {
						ThreadLocalRandom r=ThreadLocalRandom.current();
						dx+=r.nextDouble(noise*-1,noise);
						dy+=r.nextDouble(noise*-1,noise);
						dz+=r.nextDouble(noise*-1,noise);
					}
					Vec2D v=Utils.lookAtVec(p.getEyeLocation(),dd.add(dx,dy,dz));
					Volatile.handler.playerConnectionLookAt(p,(float)v.getX(),(float)v.getY());
				}
				dd.subtract(dx, dy, dz);
				count++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);
		return true;
	}

}
