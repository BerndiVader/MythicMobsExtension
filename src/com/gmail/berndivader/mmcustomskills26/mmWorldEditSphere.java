package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmWorldEditSphere extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {
	
	private int mp;
	private Material material;
	private String size;
	private int undoticks;
	private boolean mask;
	private boolean self;
	
	// skill: wesphere{add=0;material=stone;size=2,2,2;undoticks=40;self=false;mask=false}

	public mmWorldEditSphere(CustomMechanic skill, MythicLineConfig mlc) {
		super(skill.getConfigLine(), mlc);
		this.mask = mlc.getBoolean("mask",false);
		this.ASYNC_SAFE=this.mask;
		this.mp = mlc.getInteger("add",0);
		this.material = Material.getMaterial(mlc.getString("material","STONE"));
		if (this.material==null) this.material = Material.getMaterial("STONE");
		this.size = mlc.getString("size","2,2,2");
		this.undoticks = mlc.getInteger("undoticks",20);
		this.self = mlc.getBoolean("self",false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity te = null;
		if (this.self) {
			SkillCaster caster = data.getCaster();
			te = BukkitAdapter.adapt(caster.getEntity());
		} else {
			te = BukkitAdapter.adapt(target);
		}
		if (te==null) return false;
		dosphere(te.getLocation());
		return true;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		if (target==null) return false;
		dosphere(BukkitAdapter.adapt(target));
		return true;
	}
	
	private void dosphere(Location l) {
		Sphere sphere =  new Sphere();
		Vector v = l.getDirection();
		Location fl = l.add(v.multiply(mp));
		sphere.drawsphere(fl, material, size, undoticks, mask);
	}
}
