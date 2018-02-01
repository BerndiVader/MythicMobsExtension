package main.java.com.gmail.berndivader.MythicPlayers.Mechanics;

import java.util.Optional;

import main.java.com.gmail.berndivader.MythicPlayers.ActivePlayer;
import main.java.com.gmail.berndivader.MythicPlayers.MythicPlayers;
import main.java.com.gmail.berndivader.MythicPlayers.PlayerManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmNormalPlayer extends SkillMechanic implements ITargetedEntitySkill {
	protected PlayerManager playermanager = MythicPlayers.inst().getPlayerManager();
	protected MobManager mobmanager = MythicPlayers.mythicmobs.getMobManager();

	public mmNormalPlayer(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!mobmanager.isActiveMob(target))
			return false;
		Optional<ActivePlayer> maybePlayer = playermanager.getActivePlayer(target.getUniqueId());
		if (maybePlayer.isPresent()) {
			playermanager.makeNormalPlayer(maybePlayer.get());
		}
		return true;
	}

}
