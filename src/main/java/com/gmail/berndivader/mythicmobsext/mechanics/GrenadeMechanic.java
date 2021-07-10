package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.EntityCacheHandler;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

@ExternalAnnotation(name = "grenade", author = "BerndiVader")
public class GrenadeMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {
	private Plugin plugin = Main.getPlugin();
	private int size;
	private int amount;
	private int fuse;
	private int utime;
	private double hGain;
	private double gravity;
	private boolean fire;
	private boolean breakBlocks;
	private boolean ueffect;
	private boolean undotnt;
	private boolean ued;

	public GrenadeMechanic(String h, MythicLineConfig mlc) {
		super(h, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		this.size = mlc.getInteger(new String[] { "size", "s" }, 2);
		this.amount = mlc.getInteger(new String[] { "amount", "a" }, 1);
		this.fuse = mlc.getInteger(new String[] { "fuse", "f" }, 80);
		this.fire = mlc.getBoolean(new String[] { "fire", "burn", "bf" }, false);
		this.utime = mlc.getInteger(new String[] { "undotime", "utime", "ut" }, 60);
		this.ueffect = mlc.getBoolean(new String[] { "effect", "undoeffect", "ueffect", "ue" }, true);
		this.breakBlocks = mlc.getBoolean(new String[] { "break", "breakblocks", "b" }, false);
		this.undotnt = mlc.getBoolean(new String[] { "undotnt", "undo", "u" }, true);
		this.ued = mlc.getBoolean(new String[] { "useeyedirection", "ued" }, false);
		this.hGain = mlc.getDouble(new String[] { "heightgain", "hg" }, 3.0D);
		this.gravity = mlc.getDouble(new String[] { "gravity", "g" }, 0.0975);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.castAtLocation(data, target.getLocation());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation t) {
		if (!data.getCaster().getEntity().isLiving() || !data.getCaster().getEntity().getWorld().equals(t.getWorld()))
			return false;
		Location source = data.getCaster().getEntity().getBukkitEntity().getLocation().clone();
		Location target = BukkitAdapter.adapt(t);
		Vector v = this.ued
				? ((LivingEntity) data.getCaster().getEntity().getBukkitEntity()).getEyeLocation().getDirection()
				: MathUtils.calculateVelocity(source.toVector(), target.toVector(), this.gravity, this.hGain);
		try {
			v.checkFinite();
		} catch (IllegalArgumentException e) {
			return false;
		}
		for (int a = 0; a < this.amount; a++) {
			Location sl = source.clone();
			final TNTPrimed grenade = (TNTPrimed) sl.getWorld().spawnEntity(sl, EntityType.PRIMED_TNT);
			if (grenade == null)
				continue;
			grenade.setVelocity(v);
			grenade.setYield(size);
			grenade.setFuseTicks(this.fuse);
			grenade.setIsIncendiary(this.fire);
			grenade.setMetadata("customgrenade", new FixedMetadataValue(this.plugin, true));
			grenade.setMetadata("utime", new FixedMetadataValue(this.plugin, this.utime));
			grenade.setMetadata("noblkdmg", new FixedMetadataValue(this.plugin, this.breakBlocks));
			grenade.setMetadata("undotnt", new FixedMetadataValue(this.plugin, this.undotnt));
			grenade.setMetadata("ueffect", new FixedMetadataValue(this.plugin, this.ueffect));
			EntityCacheHandler.add(grenade);
		}
		return true;
	}
}
