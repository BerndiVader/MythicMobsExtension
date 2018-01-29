package com.gmail.berndivader.mythicmobsext.cachedowners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class cachedOwnerSkill extends SkillMechanic 
implements
ITargetedEntitySkill {
	
	public cachedOwnerSkill(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        if (data.getCaster() instanceof ActiveMob) {
            ActiveMob am = (ActiveMob)data.getCaster();
            am.setOwner(target.getUniqueId());
            if (target.isPlayer() && data.getCaster().getEntity().getBukkitEntity() instanceof Wolf) {
                ((Wolf)data.getCaster().getEntity().getBukkitEntity()).setOwner((AnimalTamer)((Player)target.getBukkitEntity()));
                ((Wolf)data.getCaster().getEntity().getBukkitEntity()).setTamed(true);
            }
            CachedOwnerHandler.addCachedOwner(am.getUniqueId(), target.getUniqueId());
            return true;
        }
        return false;
    }
}
