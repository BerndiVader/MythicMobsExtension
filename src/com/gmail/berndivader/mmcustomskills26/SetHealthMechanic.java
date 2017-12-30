package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.io.ConfigManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class SetHealthMechanic 
extends
SkillMechanic 
implements
INoTargetSkill {
	private String r,m;
	private boolean b;

	public SetHealthMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.r=mlc.getString(new String[] {"amount","a","health","h"},"20");
		this.b=mlc.getBoolean(new String[] {"ihm","ignoremodifier"},true);
		this.m=mlc.getString(new String[] {"set"},"");
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().isLiving()) {
			ActiveMob am=(ActiveMob)data.getCaster();
			double h=20,mod=0;
			if (this.r.contains("to")) {
				h=CustomSkillStuff.randomRangeDouble(this.r);
			} else {
				h=Double.parseDouble(this.r);
			}
			if (!b&&(data.getCaster() instanceof ActiveMob)) {
                mod=ConfigManager.defaultLevelModifierHealth.startsWith("+")
                		? Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1))
                		: (ConfigManager.defaultLevelModifierHealth.startsWith("*") 
                		? h * Double.valueOf(ConfigManager.defaultLevelModifierHealth.substring(1)) 
                		: h * Double.valueOf(ConfigManager.defaultLevelModifierHealth));
            }
			if (am!=null&&am.getLevel()>1&&mod>0.0) h+=mod*(am.getLevel()-1);
			LivingEntity e=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
			if (this.m.equals("+")) {
				h+=e.getMaxHealth();
			} else if (this.m.equals("*")) {
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
