package com.gmail.berndivader.mythicmobsext;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EquipFixMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private MythicMobs mythicmobs=Main.getPlugin().getMythicMobs();
	private String itemString;

	public EquipFixMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.itemString=mlc.getString(new String[] { "skull", "s" });
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!(target.getBukkitEntity() instanceof LivingEntity)) return false;
		getItem((LivingEntity)target.getBukkitEntity(),this.itemString);
		return true;
	}

	private boolean getItem(LivingEntity target, String item) {
		Optional<MythicItem> maybeItem;
		if ((maybeItem=this.mythicmobs.getItemManager().getItem(itemString)).isPresent()) {
			MythicItem mi=maybeItem.get();
			target.getEquipment().setHelmet(BukkitAdapter.adapt(mi.generateItemStack(1)));
			return true;
		}
		return false;
	}
}
