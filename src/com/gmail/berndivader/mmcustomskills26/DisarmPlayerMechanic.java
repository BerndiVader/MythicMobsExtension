package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class DisarmPlayerMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {

	public DisarmPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()) return false;
		Player p=(Player)target.getBukkitEntity();
		int es=p.getInventory().firstEmpty();
		if (!p.getItemInHand().getData().getItemType().equals(Material.AIR)
				&&es>-1) {
			ItemStack is=p.getItemInHand().clone();
			p.setItemInHand(new ItemStack(Material.AIR));
			p.getInventory().setItem(es, is.clone());
		}
		return true;
	}

}
