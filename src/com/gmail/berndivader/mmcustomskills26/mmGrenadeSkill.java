package com.gmail.berndivader.mmcustomskills26;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmGrenadeSkill extends SkillMechanic implements ITargetedEntitySkill {

	protected Plugin plugin = Main.getPlugin();
	protected int size;
	protected int amount;
	protected int fuse;
	protected boolean fire;
	protected boolean breakBlocks;
	protected Block tblock;
	protected Location sloc;
	protected Random rnd;
	protected boolean ueffect;
	protected int utime;
	protected boolean undotnt;

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
		this.rnd = new Random();
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		SkillCaster caster = data.getCaster();
		if (!(caster instanceof ActiveMob)) {
			return false;
		}
		ActiveMob am = (ActiveMob) caster;
		tblock = target.getBukkitEntity().getLocation().getBlock();
		sloc = am.getLivingEntity().getEyeLocation();
		for (int a = 0; a < this.amount; a++) {
			Location spawnloc = sloc.clone();
			if (this.amount > 1) {
				spawnloc.setX(spawnloc.getX() + this.rnd.nextInt(2 * this.amount) - this.amount);
				spawnloc.setZ(spawnloc.getZ() + this.rnd.nextInt(2 * this.amount) - this.amount);
			}
			TNTPrimed grenade = (TNTPrimed) am.getLivingEntity().getWorld().spawnEntity(spawnloc,
					EntityType.PRIMED_TNT);
			if (grenade == null) {
				return false;
			}
			Vector aim = am.getLivingEntity().getLocation().getDirection();
			grenade.setVelocity(aim);
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
