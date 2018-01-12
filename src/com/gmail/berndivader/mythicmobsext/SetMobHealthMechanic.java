package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.io.ConfigManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetMobHealthMechanic 
extends
SkillMechanic 
implements
INoTargetSkill {
	private String r;
	private String m;
	private Boolean b;

	public SetMobHealthMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.r=mlc.getString(new String[] { "amount", "a", "health", "h" }, "20");
		this.b=mlc.getBoolean(new String[] { "ignoremodifier", "im" }, true);
		this.m=mlc.getString(new String[] { "mode", "m" }, "").toUpperCase();
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().isLiving()) {
			ActiveMob am=(ActiveMob)data.getCaster();
			double h=20,mod=0;
			if (this.r.contains("to")) {
				h=Utils.randomRangeDouble(this.r);
			} else {
				h=Double.parseDouble(this.r);
			}
			if (!b&&(data.getCaster() instanceof ActiveMob)) {
				mod=ConfigManager.defaultLevelModifierHealth.startsWith("ADD")
                		? Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1))
                		: (ConfigManager.defaultLevelModifierHealth.startsWith("MULTIPLY") 
                		? h * Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1)) 
                		: h * Double.valueOf(ConfigManager.defaultLevelModifierHealth));
            }
			if (am!=null&&am.getLevel()>1&&mod>0.0) h+=mod*(am.getLevel()-1);
			LivingEntity e=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			if (this.m.equals("ADD")) {
				h+=e.getMaxHealth();
			} else if (this.m.equals("MULTIPLY")) {
				h=e.getMaxHealth()*h;
			}
			h=Math.ceil(h);
			e.setMaxHealth(h);
			e.setHealth(h);
			return true;
		} else {
			return false;
		}
	}

}
