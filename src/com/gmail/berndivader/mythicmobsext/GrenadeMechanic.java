package com.gmail.berndivader.mythicmobsext;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class GrenadeMechanic 
extends 
SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill {
	private Plugin plugin = Main.getPlugin();
	private Integer size;
	private Integer amount;
	private Integer fuse;
	private Integer utime;
	private Double hGain;
	private Double gravity;
	private Boolean fire;
	private Boolean breakBlocks;
	private Boolean ueffect;
	private Boolean undotnt;
	private Boolean ued;

	public GrenadeMechanic(String h, MythicLineConfig mlc) {
		super(h, mlc);
		ASYNC_SAFE = false;
		this.size=mlc.getInteger(new String[] { "size", "s" }, 2);
		this.amount=mlc.getInteger(new String[] { "amount", "a" }, 1);
		this.fuse=mlc.getInteger(new String[] { "fuse", "f" }, 80);
		this.fire=mlc.getBoolean(new String[] { "fire", "burn", "bf" }, false);
		this.utime=mlc.getInteger(new String[] { "undotime", "utime", "ut" }, 60);
		this.ueffect=mlc.getBoolean(new String[] { "effect", "undoeffect", "ueffect", "ue" }, true);
		this.breakBlocks=mlc.getBoolean(new String[] { "break", "breakblocks", "b" }, false);
		this.undotnt=mlc.getBoolean(new String[] { "undotnt", "undo", "u" }, true);
		this.ued=mlc.getBoolean(new String[] { "useeyedirection", "ued" }, false);
		this.hGain=mlc.getDouble(new String[] { "heightgain", "hg" }, 3.0D);
		this.gravity=mlc.getDouble(new String[] { "gravity", "g" }, 0.0975);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.castAtLocation(data,target.getLocation());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation t) {
		if (!data.getCaster().getEntity().isLiving()||!data.getCaster().getEntity().getWorld().equals(t.getWorld())) return false;
		Location source=data.getCaster().getEntity().getBukkitEntity().getLocation().clone();
		Location target=BukkitAdapter.adapt(t);
		Vector v=this.ued?((LivingEntity)data.getCaster().getEntity().getBukkitEntity()).getEyeLocation().getDirection()
				:Utils.calculateTrajectory(source.toVector(),target.toVector(),this.hGain,this.gravity);
		for (int a=0;a<this.amount;a++) {
			Location sl=source.clone();
			final TNTPrimed grenade=(TNTPrimed)sl.getWorld().spawnEntity(sl,EntityType.PRIMED_TNT);
			if (grenade==null) continue;
			grenade.setVelocity(v);
			grenade.setYield(size);
			grenade.setFuseTicks(this.fuse);
			grenade.setIsIncendiary(this.fire);
			grenade.setMetadata("customgrenade", new FixedMetadataValue(this.plugin, true));
			grenade.setMetadata("utime", new FixedMetadataValue(this.plugin, this.utime));
			grenade.setMetadata("noblkdmg", new FixedMetadataValue(this.plugin, this.breakBlocks));
			grenade.setMetadata("undotnt", new FixedMetadataValue(this.plugin, this.undotnt));
			grenade.setMetadata("ueffect", new FixedMetadataValue(this.plugin, this.ueffect));
			Main.entityCache.add(grenade);
		}
		return true;
	}
}
