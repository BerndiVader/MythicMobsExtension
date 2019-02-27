package com.gmail.berndivader.mythicmobsext.cachedowners;

import java.util.UUID;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class RestoreCachedOwnerMechanic extends SkillMechanic 
implements
INoTargetSkill {
	
	public RestoreCachedOwnerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

    @Override
    public boolean cast(SkillMetadata data) {
        if (data.getCaster() instanceof ActiveMob) {
            ActiveMob am = (ActiveMob)data.getCaster();
            UUID owner_uuid=CachedOwnerHandler.getMobOwner(am.getUniqueId());
            if(owner_uuid!=null) {
            	AbstractEntity target=BukkitAdapter.adapt(NMSUtils.getEntity(owner_uuid));
                am.setOwner(owner_uuid);
                if (target!=null&&target.isPlayer()&&data.getCaster().getEntity().getBukkitEntity() instanceof Wolf) {
                    ((Wolf)data.getCaster().getEntity().getBukkitEntity()).setOwner((AnimalTamer)((Player)target.getBukkitEntity()));
                    ((Wolf)data.getCaster().getEntity().getBukkitEntity()).setTamed(true);
                }
                return true;
            }
        }
        return false;
    }
}
