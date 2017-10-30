package com.gmail.berndivader.MythicPlayers.Targeters;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public class TargetsTargetTargeter 
extends
IEntitySelector {

	public TargetsTargetTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>targets=new HashSet<>();
		HashSet<AbstractEntity>tt=data.getEntityTargets();
		Iterator<AbstractEntity>it=tt.iterator();
		while (it.hasNext()) {
			AbstractEntity target=it.next();
			if (target!=null) {
				if (target.isPlayer()) {
					AbstractEntity pt=BukkitAdapter.adapt(CustomSkillStuff.getTargetedEntity((Player)target.getBukkitEntity()));
					if (pt!=null) targets.add(pt);
				} else {
					targets.add(target);
				}
			}
		}
		return targets;
	}
}
