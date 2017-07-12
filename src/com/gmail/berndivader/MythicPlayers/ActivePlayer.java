package com.gmail.berndivader.MythicPlayers;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class ActivePlayer extends ActiveMob {

	public ActivePlayer(UUID uuid, AbstractEntity e, MythicMob type, int level) {
		super(uuid, e, type, level);
	}

	public Optional<ActiveMob> getActiveMob() {
		return MythicMobs.inst().getMobManager().getActiveMob(this.getUniqueId());
	}
	
	public Player getPlayer() {
		return BukkitAdapter.adapt(this.getEntity().asPlayer());
	}
	
}
