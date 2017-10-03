package com.gmail.berndivader.mmcustomskills26;

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

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmGrenadeSkill extends SkillMechanic
		implements
		ITargetedEntitySkill,
		ITargetedLocationSkill {

	protected Plugin plugin = Main.getPlugin();
	protected int size;
	protected int amount;
	protected int fuse;
	protected boolean fire;
	protected boolean breakBlocks;
	protected boolean ueffect;
	protected int utime;
	protected boolean undotnt;
	protected boolean ued;
	protected double hGain;
	private double gravity;

	public mmGrenadeSkill(CustomMechanic h, MythicLineConfig mlc) {
		super(h.getConfigLine(), mlc);

		ASYNC_SAFE = false;
		this.size = mlc.getInteger(new String[] { "size", "s" }, 2);
		this.amount = mlc.getInteger(new String[] { "amount", "a" }, 1);
		this.fuse = mlc.getInteger(new String[] { "fuse", "f" }, 80);
		this.fire = mlc.getBoolean(new String[] { "fire", "burn" }, false);
		this.utime = mlc.getInteger(new String[] { "undotime", "utime", "ut" }, 60);
		this.ueffect = mlc.getBoolean(new String[] { "effect", "undoeffect", "ueffect", "ue" }, true);
		this.breakBlocks = mlc.getBoolean(new String[] { "break", "breakblocks", "b" }, false);
		this.undotnt = mlc.getBoolean(new String[] { "undotnt", "undo", "u" }, true);
		this.ued=mlc.getBoolean(new String[]{"useeyedirection","ued"},false);
		this.hGain=mlc.getDouble(new String[]{"heighgain","g"},3.0D);
		this.gravity=mlc.getDouble(new String[]{"gravity"},0.0975);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.castAtLocation(data,target.getLocation());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation t) {
		if (!data.getCaster().getEntity().isLiving()
				||!data.getCaster().getEntity().getWorld().equals(t.getWorld())) return false;
		Location source = data.getCaster().getEntity().getBukkitEntity().getLocation().clone();
		Location target = BukkitAdapter.adapt(t);
		Vector v=this.ued?((LivingEntity)data.getCaster().getEntity().getBukkitEntity()).getEyeLocation().getDirection()
				:CustomSkillStuff.calculateTrajectory(source.toVector(),target.toVector(),this.hGain,this.gravity);
		for (int a = 0; a < this.amount; a++) {
			Location sl = source.clone();
			final TNTPrimed grenade = (TNTPrimed) sl.getWorld().spawnEntity(sl,
					EntityType.PRIMED_TNT);
			if (grenade == null) return false;
			grenade.setVelocity(v);
			grenade.setYield(size);
			grenade.setFuseTicks(this.fuse);
			grenade.setIsIncendiary(this.fire);
			grenade.setMetadata("customgrenade", new FixedMetadataValue(this.plugin, true));
			grenade.setMetadata("utime", new FixedMetadataValue(this.plugin, this.utime));
			grenade.setMetadata("noblkdmg", new FixedMetadataValue(this.plugin, this.breakBlocks));
			grenade.setMetadata("undotnt", new FixedMetadataValue(this.plugin, this.undotnt));
			grenade.setMetadata("ueffect", new FixedMetadataValue(this.plugin, this.ueffect));
		}
		return true;
	}
}
