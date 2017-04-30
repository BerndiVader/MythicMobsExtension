package com.gmail.berndivader.mmcustomskills26;

import java.util.UUID;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class ActivePlayer extends ActiveMob {

	public ActivePlayer(UUID uuid, AbstractEntity e, MythicMob type, int level) {
		super(uuid, e, type, level);
	}

}
