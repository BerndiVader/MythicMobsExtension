package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "targetstarget", author = "BerndiVader")
public class TargetsTargetTargeter extends ISelectorEntity {
	public TargetsTargetTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity> targets = new HashSet<>();
		Collection<AbstractEntity> tt = data.getEntityTargets();
		for (AbstractEntity target : tt) {
			if (target != null) {
				if (target.isPlayer()) {
					AbstractEntity pt = BukkitAdapter
							.adapt(Utils.getTargetedEntity((Player) target.getBukkitEntity(), length));
					if (pt != null)
						targets.add(pt);
				} else if (target.getTarget() != null) {
					targets.add(target.getTarget());
				}
			}
		}
		return this.applyOffsets(targets);
	}
}
