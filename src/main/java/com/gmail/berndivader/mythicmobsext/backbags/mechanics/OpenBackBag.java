package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import java.util.ArrayList;
import java.util.List;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagViewer;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class OpenBackBag extends SkillMechanic implements INoTargetSkill, ITargetedEntitySkill {

	int size;
	ItemStack[] default_items;
	boolean view_only, temporary;
	PlaceholderString bag_name;
	List<Integer> excluded_slots;

	public OpenBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		size = mlc.getInteger("size", 9);
		view_only = mlc.getBoolean("viewonly", true);
		default_items = BackBagHelper.createDefaultItemStack(mlc.getString("items", null));
		bag_name = mlc.getPlaceholderString(new String[] { "name", "title" }, "BackBag");
		temporary = mlc.getBoolean("temporary", false);
		String[] temp = mlc.getString("excludedslots", "").split(",");
		excluded_slots = new ArrayList<>();
		for (int i1 = 0; i1 < temp.length; i1++) {
			try {
				if (!temp[i1].isEmpty()) {
					int slot = Integer.parseInt(temp[i1]);
					excluded_slots.add(slot);
				}
			} catch (Exception ex) {
				Main.logger.warning("Ignoring " + temp[i1] + " in skill " + skill + " its not a valid slot number.");
			}
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().isPlayer()) {
			Player player = (Player) data.getCaster().getEntity().getBukkitEntity();
			BackBagViewer bag = new BackBagViewer(player, size, default_items, bag_name.get(data), temporary);
			bag.viewBackBag(player, view_only, this.excluded_slots);
			return true;
		}
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if (abstract_entity.isPlayer()) {
			Entity holder = data.getCaster().getEntity().getBukkitEntity();
			Player viewer = (Player) abstract_entity.getBukkitEntity();
			BackBagViewer bag = new BackBagViewer(holder, size, default_items, bag_name.get(data, abstract_entity),
					temporary);
			bag.viewBackBag(viewer, view_only, this.excluded_slots);
			return true;
		}
		return false;
	}
}
