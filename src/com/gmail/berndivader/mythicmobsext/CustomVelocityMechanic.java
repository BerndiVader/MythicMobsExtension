package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CustomVelocityMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	protected Double velocityX;
	protected Double velocityY;
	protected Double velocityZ;
	protected VelocityMode mode;
	protected Boolean debug;

	public CustomVelocityMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.ASYNC_SAFE=false;
		this.debug=mlc.getBoolean("debug",false);
		this.velocityX = mlc.getDouble(new String[]{"velocityx", "vx", "x"}, 0.0D);
		this.velocityY = mlc.getDouble(new String[]{"velocityy", "vy", "y"}, 0.0D);
		this.velocityZ = mlc.getDouble(new String[]{"velocityz", "vz", "z"}, 0.0D);
		String strMode = mlc.getString(new String[]{"mode", "m"}, "SET", new String[0]);
		switch (strMode.toUpperCase()) {
			case "ADD": {
				this.mode = VelocityMode.ADD;
				break;
			} case "MULTIPLY": {
				this.mode = VelocityMode.MULTIPLY;
				break;
			} default: {
				this.mode = VelocityMode.SET;
			}
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity e = target.getBukkitEntity();
		Vector v = e.getVelocity().clone();
		Vector vb = v.clone();
		if (this.mode.equals((Object)VelocityMode.SET)) {
			v = new Vector(this.velocityX, this.velocityY, this.velocityZ);
		} else if (this.mode.equals((Object)VelocityMode.ADD)) {
			v.setX(v.getX()+this.velocityX);
			v.setY(v.getY()+this.velocityY);
			v.setZ(v.getZ()+this.velocityZ);
		} else if (this.mode.equals((Object)VelocityMode.MULTIPLY)) {
			v.setX(v.getX()*this.velocityX);
			v.setY(v.getY()*this.velocityY);
			v.setZ(v.getZ()*this.velocityZ);
		}
		if (debug) System.out.println(
        		"dx:"+v.getX()
        		+" dy:"+v.getY()
        		+" dz:"+v.getZ()
        		+" len:"+v.length()
        		);
		if (Double.isNaN(v.length()) || Double.isInfinite(v.length())) v=vb;
		e.setVelocity(v);
		return true;
	}
	
	static enum VelocityMode {
		SET,
		ADD,
		MULTIPLY;
		private VelocityMode() {
		}
	}
}
