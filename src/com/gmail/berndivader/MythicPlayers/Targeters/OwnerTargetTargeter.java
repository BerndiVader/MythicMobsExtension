package com.gmail.berndivader.MythicPlayers.Targeters;

import java.util.HashSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public class OwnerTargetTargeter extends IEntitySelector {
	protected MobManager mobmanager=Main.mythicmobs.getMobManager();

	public OwnerTargetTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>targets=new HashSet<>();
		if (mobmanager.isActiveMob(data.getCaster().getEntity())) {
			ActiveMob am=mobmanager.getMythicMobInstance(data.getCaster().getEntity());
			if (am.getOwner().isPresent()) {
				Entity owner=NMSUtils.getEntity(am.getEntity().getBukkitEntity().getWorld(),am.getOwner().get());
				if (owner!=null) {
					if (owner instanceof Creature) {
						targets.add(BukkitAdapter.adapt(((Creature)owner).getTarget()));
					} else if (owner instanceof Player) {
						AbstractEntity pt=BukkitAdapter.adapt(CustomSkillStuff.getTargetedEntity((Player)owner));
						if (pt!=null) targets.add(pt);
					} else if (owner.getLastDamageCause()!=null) {
						targets.add(BukkitAdapter.adapt(owner.getLastDamageCause().getEntity()));
					}
				}
			}
		}
		return targets;
	}
}
