package com.gmail.berndivader.mythicmobsext;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
	private long dt;
	
	public DisarmPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dt=mlc.getLong(new String[] {"dt","duration"},180);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()) {
			if (target.isLiving()) {
				final LivingEntity e=(LivingEntity)target.getBukkitEntity();
				if (!e.getEquipment().getItemInMainHand().getData().getItemType().equals(Material.AIR)) {
					final ItemStack is=e.getEquipment().getItemInMainHand().clone();
					e.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
					new BukkitRunnable() {
						@Override
						public void run() {
							if (e!=null&&!e.isDead()) e.getEquipment().setItemInMainHand(is.clone());
						}
					}.runTaskLater(Main.getPlugin(), dt);
				}
			} else {
				return false;
			}
		} else {
			Player p=(Player)target.getBukkitEntity();
			int es=p.getInventory().firstEmpty();
			if (!p.getEquipment().getItemInMainHand().getData().getItemType().equals(Material.AIR)
					&&es>-1) {
				ItemStack is=p.getEquipment().getItemInMainHand().clone();
				p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
				p.getInventory().setItem(es, is.clone());
			}
		}
		return true;
	}

}
