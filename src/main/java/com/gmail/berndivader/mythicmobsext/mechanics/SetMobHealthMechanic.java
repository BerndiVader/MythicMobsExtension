package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.ConfigManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="setmobhealth",author="BerndiVader")
public class SetMobHealthMechanic 
extends
SkillMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill {
	private String r;
	private char m;
	private boolean b,b1;

	public SetMobHealthMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.r=mlc.getString(new String[] { "amount", "a", "health", "h" }, "20").toLowerCase();
		this.b=mlc.getBoolean(new String[] { "ignoremodifier", "im" }, true);
		this.b1=mlc.getBoolean(new String[] {"setcurrenthealth","sch"},true);
		this.m=mlc.getString(new String[] { "mode","m","set","s" }, "S").toUpperCase().charAt(0);
	}
	
	boolean c(SkillMetadata data,AbstractEntity t) {
		if (data.getCaster().getEntity().isLiving()) {
			ActiveMob am=(ActiveMob)data.getCaster();
			double h=20,mod=0;
			h=Utils.randomRangeDouble(Utils.parseMobVariables(r,data,data.getCaster().getEntity(),t,null));
			if (!b&&am!=null) {
				mod=ConfigManager.defaultLevelModifierHealth.startsWith("+")
                		? Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1))
                		: (ConfigManager.defaultLevelModifierHealth.startsWith("*") 
                		? h*Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1)) 
                		: h*Double.valueOf(ConfigManager.defaultLevelModifierHealth));
            }
			
			if (am!=null&&am.getLevel()>1&&mod>0.0) h+=mod*(am.getLevel()-1);
			LivingEntity e=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			switch(m) {
			case 'A':
				h+=e.getMaxHealth();
				break;
			case 'M':
				h=e.getMaxHealth()*h;
				break;
			case 'R':
				h=e.getMaxHealth()-h<1?1:e.getMaxHealth()-h;
				break;
			}
			h=Math.ceil(h);
			e.setMaxHealth(h);
			if (b1) e.setHealth(e.getMaxHealth());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return c(data,null);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		return c(data,t);
	}

}
