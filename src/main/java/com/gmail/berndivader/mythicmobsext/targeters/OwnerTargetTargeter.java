package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.TargeterAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

@TargeterAnnotation(name="ownertarget",author="BerndiVader")
public class OwnerTargetTargeter extends IEntitySelector {

	public OwnerTargetTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>targets=new HashSet<>();
		if (Utils.mobmanager.isActiveMob(data.getCaster().getEntity())) {
			ActiveMob am=Utils.mobmanager.getMythicMobInstance(data.getCaster().getEntity());
			if (am.getOwner().isPresent()) {
				Entity owner=NMSUtils.getEntity(am.getEntity().getBukkitEntity().getWorld(),am.getOwner().get());
				if (owner!=null) {
					AbstractEntity pt;
					if (owner instanceof Creature) {
						if ((pt=BukkitAdapter.adapt(((Creature)owner).getTarget()))!=null)targets.add(pt);
					} else if (owner instanceof Player) {
						if((pt=BukkitAdapter.adapt(Utils.getTargetedEntity((Player)owner)))!=null)targets.add(pt);
					} else if (owner.getLastDamageCause()!=null) {
						targets.add(BukkitAdapter.adapt(owner.getLastDamageCause().getEntity()));
					}
				}
			}
		}
		return targets;
	}
}
