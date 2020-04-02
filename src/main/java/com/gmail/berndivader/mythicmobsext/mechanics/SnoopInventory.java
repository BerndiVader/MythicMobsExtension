package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import com.gmail.berndivader.mythicmobsext.backbags.InventoryViewer;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "snoopinventory,openinventory", author = "BerndiVader")
public class SnoopInventory extends SkillMechanic implements ITargetedEntitySkill {

	boolean view_only, snoop;
	InventoryType type;

	public SnoopInventory(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;

		snoop = mlc.getLine().toLowerCase().startsWith("snoop");
		view_only = mlc.getBoolean("viewonly", true);
		try {
			type = InventoryType.valueOf(mlc.getString("type", "PLAYER").toUpperCase());
		} catch (Exception ex) {
			type = InventoryType.PLAYER;
		}

	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if (snoop) {
			if (data.getCaster().getEntity().isPlayer() && abstract_entity.isPlayer()) {
				Player caster = (Player) data.getCaster().getEntity().getBukkitEntity();
				Player victim = (Player) abstract_entity.getBukkitEntity();
				new InventoryViewer(victim, caster);
			}
		} else {
			if (data.getCaster().getEntity().isPlayer()) {
				Player caster = (Player) data.getCaster().getEntity().getBukkitEntity();
				new InventoryViewer(caster, caster, view_only, type);
			}
		}
		return true;
	}

}
