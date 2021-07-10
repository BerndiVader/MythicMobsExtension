package com.gmail.berndivader.mythicmobsext.mechanics;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;

@ExternalAnnotation(name = "resettarget,settarget_ext", author = "BerndiVader")
public class ResetTargetMechanic extends SkillMechanic implements ITargetedEntitySkill, INoTargetSkill {
	boolean event, trigger, set;
	TargetReason reason;

	public ResetTargetMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);

		set = line.charAt(0) == 's';
		event = mlc.getBoolean("event", false);
		trigger = mlc.getBoolean("trigger", false);
		try {
			reason = TargetReason.valueOf(mlc.getString("reason", "custom").toUpperCase());
		} catch (Exception ex) {
			ex.printStackTrace();
			reason = TargetReason.CUSTOM;
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return this.castAtEntity(data, data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			if (data.getCaster().getEntity().isCreature()) {
				Creature creature = (Creature) data.getCaster().getEntity().getBukkitEntity();
				creature.setTarget(set ? (LivingEntity) target.getBukkitEntity() : null);
			} else {
				NMSUtils.setGoalTarget(data.getCaster().getEntity().getBukkitEntity(),
						set ? target.getBukkitEntity() : null, reason, event);
			}
			if (trigger && Utils.mobmanager.isActiveMob(data.getCaster().getEntity())) {
				new TriggeredSkill(SkillTrigger.TARGETCHANGE,
						Utils.mobmanager.getMythicMobInstance(data.getCaster().getEntity()), target, true);
			}
			return true;
		}
		return false;
	}
}
