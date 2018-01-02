package com.gmail.berndivader.mythicmobsext;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EnderDragonDeathEffect 
extends SkillMechanic 
implements 
ITargetedEntitySkill, 
ITargetedLocationSkill {
	private PotionEffect po=new PotionEffect(PotionEffectType.INVISIBILITY,999999,4);

	public EnderDragonDeathEffect(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		this.playEnderEffect(BukkitAdapter.adapt(target));
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		this.playEnderEffect(target.getBukkitEntity().getLocation());
		return true;
	}

	private void playEnderEffect(Location l) {
		EnderDragon e=l.getWorld().spawn(l,EnderDragon.class);
		e.addPotionEffect(this.po);
		e.playEffect(EntityEffect.DEATH);
		e.setHealth(0);
		return;
	}
}
