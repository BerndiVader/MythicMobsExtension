package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class showHealthBarMechanic extends SkillMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill {
	
	protected String config;
	protected Scoreboard s;
	
	public showHealthBarMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return registerHealthBar(target.getBukkitEntity(),this.config);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return registerHealthBar(data.getCaster().getEntity().getBukkitEntity(), this.config);
	}
	
	private boolean registerHealthBar(Entity entity, String config) {
        if (this.s.getObjective("health") != null) {
            this.s.getObjective("health").unregister();
        }
        Objective o = this.s.registerNewObjective("health", "health");
        o.setDisplayName((Object)ChatColor.RED + "\u2764");
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
        return true;
	}

}
