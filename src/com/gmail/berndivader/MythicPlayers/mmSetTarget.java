package com.gmail.berndivader.MythicPlayers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.MythicUtil;

public class mmSetTarget extends SkillMechanic implements INoTargetSkill {

	public mmSetTarget(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		LivingEntity le;
		if (data.getCaster().getEntity().isPlayer() && (data.getCaster() instanceof ActiveMob)) {
			ActiveMob am = (ActiveMob) data.getCaster();
			le = MythicUtil.getTargetedEntity((Player)BukkitAdapter.adapt(data.getCaster().getEntity()));
			if (am.getThreatTable().size()>0) {
				am.getThreatTable().clearTarget();
				am.getThreatTable().getAllThreatTargets().clear();
			}
			if (le!=null) {
				am.getThreatTable().threatGain(BukkitAdapter.adapt(le), 99999999);
				am.getThreatTable().targetHighestThreat();
			}
		}
		return true;
	}

}
