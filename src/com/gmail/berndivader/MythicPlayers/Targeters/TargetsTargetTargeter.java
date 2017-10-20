package com.gmail.berndivader.MythicPlayers.Targeters;

import java.util.HashSet;

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
		tt.forEach(t-> {
			AbstractEntity target;
			if ((target=t.getTarget())!=null) {
				targets.add(target);
			} else if (t.isPlayer()) {
				if ((target=BukkitAdapter.adapt(CustomSkillStuff.getTargetedEntity((Player)t.getBukkitEntity())))!=null) {
					targets.add(target);
				}
			}
		});
		return targets;
	}
}
