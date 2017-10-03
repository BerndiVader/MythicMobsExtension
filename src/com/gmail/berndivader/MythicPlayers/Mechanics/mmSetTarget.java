package com.gmail.berndivader.MythicPlayers.Mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmSetTarget extends SkillMechanic implements INoTargetSkill {

	protected String[] filter;
	protected boolean targetself;

	public mmSetTarget(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.filter = mlc.getString(new String[] { "filter", "f" }, "").split(",");
		this.targetself = mlc.getBoolean(new String[] { "selfnotarget", "snt" }, false);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		LivingEntity le;
		if (data.getCaster().getEntity().isPlayer() && (data.getCaster() instanceof ActiveMob)) {
			ActiveMob am = (ActiveMob) data.getCaster();
			if (am.getThreatTable().size() > 0) {
				am.getThreatTable().clearTarget();
				am.getThreatTable().getAllThreatTargets().clear();
			}
			le = CustomSkillStuff.getTargetedEntity((Player) BukkitAdapter.adapt(data.getCaster().getEntity()));
			if (le != null) {
				am.getThreatTable().threatGain(BukkitAdapter.adapt(le), 99999999);
				am.getThreatTable().targetHighestThreat();
			} else if (this.targetself) {
				am.getThreatTable().threatGain(am.getEntity(), 99999999);
				am.getThreatTable().targetHighestThreat();
			}
		}
		return true;
	}

}
