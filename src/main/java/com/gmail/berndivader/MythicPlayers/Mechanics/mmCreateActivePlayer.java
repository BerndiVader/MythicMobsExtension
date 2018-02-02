package com.gmail.berndivader.MythicPlayers.Mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.MythicPlayers.MythicPlayers;
import com.gmail.berndivader.MythicPlayers.PlayerManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCreateActivePlayer extends SkillMechanic implements ITargetedEntitySkill {
	protected MobManager mobmanager = MythicPlayers.mythicmobs.getMobManager();
	protected PlayerManager playermanager = MythicPlayers.inst().getPlayerManager();
	private String mobtype;

	public mmCreateActivePlayer(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.mobtype = mlc.getString(new String[] { "mobtype", "type", "mob", "t", "m" }, "Player");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		MythicMob mm = mobmanager.getMythicMob(this.mobtype);
		if (mm == null || !target.isPlayer() || !mm.isPersistent()
				|| playermanager.isActivePlayer(target.getUniqueId()))
			return false;
		return playermanager.createActivePlayer((LivingEntity) target.getBukkitEntity(), mm);
	}

}
