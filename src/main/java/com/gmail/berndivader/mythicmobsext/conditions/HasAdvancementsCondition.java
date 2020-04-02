package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "advancement,hasadvancement,advancements,hasadvancements", author = "BerndiVader")
public class HasAdvancementsCondition extends AbstractCustomCondition implements IEntityCondition {
	HashSet<Advancement> advs;

	public HasAdvancementsCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		advs = new HashSet<>();
		String[] arr1 = mlc.getString(new String[] { "advancement", "adv", "advancements" }, "").toLowerCase()
				.split(",");
		for (Iterator<Advancement> iter = Bukkit.getServer().advancementIterator(); iter.hasNext();) {
			Advancement adv = iter.next();
			for (int i1 = 0; i1 < arr1.length; i1++) {
				if (adv.getKey().getKey().toLowerCase().equals(arr1[i1]))
					advs.add(adv);
			}
		}
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			Player p = (Player) entity.getBukkitEntity();
			for (Advancement adv : advs) {
				if (p.getAdvancementProgress(adv).isDone())
					return true;
			}
		}
		return false;
	}
}
