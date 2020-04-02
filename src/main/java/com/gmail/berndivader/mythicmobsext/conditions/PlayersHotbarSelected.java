package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "hotbarselected", author = "BerndiVader")
public class PlayersHotbarSelected extends AbstractCustomCondition implements IEntityCondition {
	RangedDouble slot;

	public PlayersHotbarSelected(String line, MythicLineConfig mlc) {
		super(line, mlc);
		slot = new RangedDouble(mlc.getString(new String[] { "slots", "slot" }, "0"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity.getBukkitEntity();
			return slot.equals(player.getInventory().getHeldItemSlot());
		}
		return false;
	}
}
