package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.utils.EntityCacheHandler;
import com.gmail.berndivader.mythicmobsext.utils.HitBox;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "itemfloating", author = "BerndiVader")
public class IStatueMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {
	Optional<Skill> onTickSkill = Optional.empty(), onHitSkill = Optional.empty(), onEndSkill = Optional.empty(),
			onStartSkill = Optional.empty();
	ItemStack item_stack;
	String onTickSkillName, onHitSkillName, onEndSkillName, onStartSkillName;
	int tickInterval, duration;
	float ticksPerSecond, hitRadius, verticalHitRadius, YOffset;
	double sOffset, fOffset;
	boolean hitTarget = true, hitPlayers = false, hitNonPlayers = false, hitTargetOnly = false, invunerable, lifetime;
	short durability;

	public IStatueMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		String i = mlc.getString(new String[] { "item", "i" }, "DIRT");
		Optional<MythicItem> optional = Utils.mythicmobs.getItemManager().getItem(i);
		if (optional.isPresent()) {
			item_stack = BukkitAdapter.adapt(optional.get().generateItemStack(1));
		} else {
			try {
				item_stack = new ItemStack(Material.valueOf(i.toUpperCase()));
			} catch (Exception e) {
				item_stack = new ItemStack(Material.DIRT);
			}
		}
		this.onTickSkillName = mlc.getString(new String[] { "ontickskill", "ontick", "ot", "skill", "s", "meta", "m" });
		this.onHitSkillName = mlc.getString(new String[] { "onhitskill", "onhit", "oh" });
		this.onEndSkillName = mlc.getString(new String[] { "onendskill", "onend", "oe" });
		this.onStartSkillName = mlc.getString(new String[] { "onstartskill", "onstart", "os" });
		this.tickInterval = mlc.getInteger(new String[] { "interval", "int", "i" }, 4);
		this.ticksPerSecond = 20.0f / (float) this.tickInterval;
		this.hitRadius = mlc.getFloat(new String[] { "horizontalradius", "hradius", "hr", "r" }, 1.25f);
		this.duration = mlc.getInteger(new String[] { "maxduration", "md" }, 10);
		this.verticalHitRadius = mlc.getFloat(new String[] { "verticalradius", "vradius", "vr" }, this.hitRadius);
		this.YOffset = mlc.getFloat(new String[] { "yoffset", "yo" }, 1.0f);
		this.sOffset = mlc.getDouble(new String[] { "soffset", "so" }, 0d);
		this.fOffset = mlc.getDouble(new String[] { "foffset", "fo" }, 0d);
		this.hitPlayers = mlc.getBoolean(new String[] { "hitplayers", "hp" }, false);
		this.hitNonPlayers = mlc.getBoolean(new String[] { "hitnonplayers", "hnp" }, false);
		this.hitTarget = mlc.getBoolean(new String[] { "hittarget", "ht" }, true);
		this.hitTargetOnly = mlc.getBoolean("hittargetonly", false);
		this.invunerable = mlc.getBoolean(new String[] { "invulnerable", "inv" }, true);
		this.lifetime = mlc.getBoolean(new String[] { "lifetime", "lt" }, true);
		this.durability = (short) MathUtils.clamp(mlc.getInteger("durability", Short.MIN_VALUE), Short.MIN_VALUE,
				Short.MAX_VALUE);

		if (this.onTickSkillName != null) {
			this.onTickSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onTickSkillName);
		}
		if (this.onHitSkillName != null) {
			this.onHitSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onHitSkillName);
		}
		if (this.onEndSkillName != null) {
			this.onEndSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onEndSkillName);
		}
		if (this.onStartSkillName != null) {
			this.onStartSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onStartSkillName);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			new ProjectileRunner(data, target);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		try {
			new ProjectileRunner(data, target);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private class ProjectileRunner implements IParentSkill, Runnable {
		private Handler vh;
		private boolean cancelled, useOffset, iYaw, islocationtarget, lifetime;
		private SkillMetadata data;
		private Item item;
		private SkillCaster caster;
		private Entity owner;
		private Location currentLocation, oldLocation;
		private int taskId;
		private HashSet<LivingEntity> targets;
		private Map<LivingEntity, Long> immune;
		private double sOffset, fOffset, yOffset;
		private int count, dur;
		private Vector vEmpty = new Vector(0d, 0d, 0d);

		public ProjectileRunner(SkillMetadata data, AbstractEntity target) {
			this(data, target, null);
		}

		public ProjectileRunner(SkillMetadata data, AbstractLocation target) {
			this(data, null, target);
		}

		private ProjectileRunner(SkillMetadata data, AbstractEntity target, AbstractLocation location) {
			this.vh = Volatile.handler;
			this.islocationtarget = target == null && location != null;
			this.currentLocation = islocationtarget ? BukkitAdapter.adapt(location)
					: target.getBukkitEntity().getLocation();
			this.cancelled = false;
			this.data = data;
			this.data.setCallingEvent(this);
			this.caster = data.getCaster();
			this.owner = this.caster.getEntity().getBukkitEntity();
			this.yOffset = IStatueMechanic.this.YOffset;
			this.sOffset = IStatueMechanic.this.sOffset;
			this.fOffset = IStatueMechanic.this.fOffset;
			this.lifetime = IStatueMechanic.this.lifetime;
			this.count = 0;
			if (IStatueMechanic.this.YOffset != 0.0f) {
				this.currentLocation.setY(this.currentLocation.getY() + this.yOffset);
			}
			this.useOffset = IStatueMechanic.this.fOffset != 0d || IStatueMechanic.this.sOffset != 0d;
			if (this.useOffset) {
				Vector soV = MathUtils.getSideOffsetVectorFixed(this.currentLocation.getYaw(), this.sOffset, this.iYaw);
				Vector foV = MathUtils.getFrontBackOffsetVector(this.currentLocation.getDirection(), this.fOffset);
				this.currentLocation.add(soV);
				this.currentLocation.add(foV);
			}
			this.targets = new HashSet<LivingEntity>();
			this.immune = new HashMap<LivingEntity, Long>();
			this.item = this.currentLocation.getWorld().dropItem(this.currentLocation,
					new ItemStack(IStatueMechanic.this.item_stack));
			EntityCacheHandler.add(this.item);
			this.item.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setInvulnerable(true);
			this.item.setGravity(false);
			this.item.setTicksLived(Integer.MAX_VALUE);
			this.item.setPickupDelay(Integer.MAX_VALUE);
			if (durability > Short.MIN_VALUE)
				this.item.getItemStack().setDurability(IStatueMechanic.this.durability);
			vh.teleportEntityPacket(this.item);
			vh.changeHitBox((Entity) this.item, 0, 0, 0);
			if (IStatueMechanic.this.onStartSkill.isPresent()
					&& IStatueMechanic.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = data.deepClone();
				sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
				sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
				IStatueMechanic.this.onStartSkill.get().execute(sData);
			}
			this.taskId = TaskManager.get().scheduleTask(this, 0, 1);
		}

		@Override
		public void run() {
			if (this.cancelled) {
				return;
			}
			if (this.count > IStatueMechanic.this.duration || item == null || owner == null || owner.isDead()) {
				this.stop();
				return;
			}
			this.oldLocation = this.currentLocation.clone();
			if (!this.islocationtarget)
				this.currentLocation = this.owner.getLocation().add(0d, this.yOffset, 0d);
			if (this.dur > IStatueMechanic.this.tickInterval) {
				HitBox hitBox = new HitBox(this.currentLocation, IStatueMechanic.this.hitRadius,
						IStatueMechanic.this.verticalHitRadius);
				ListIterator<Entity> lit1 = Volatile.handler.getNearbyEntities(item, 1).listIterator();
				while (lit1.hasNext()) {
					Entity ee = lit1.next();
					if (ee == this.owner || ee == item || ee.isDead() || !(ee instanceof LivingEntity)
							|| !hitBox.contains(ee.getLocation().add(0, 0.6, 0))
							|| this.immune.containsKey((LivingEntity) ee) || ee.hasMetadata(Utils.noTargetVar)
							|| (ee instanceof Player) && ee != item && !IStatueMechanic.this.hitPlayers
							|| !IStatueMechanic.this.hitNonPlayers && ee != item)
						continue;
					LivingEntity t1 = (LivingEntity) ee;
					this.targets.add(t1);
					this.immune.put(t1, System.currentTimeMillis());
					break;
				}
				Iterator<Map.Entry<LivingEntity, Long>> iter = this.immune.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<LivingEntity, Long> entry = iter.next();
					if (entry.getValue() >= System.currentTimeMillis() - 2000)
						continue;
					iter.remove();
				}
				if (this.targets.size() > 0) {
					this.doHit((HashSet) this.targets.clone());
				}
				this.targets.clear();
				if (IStatueMechanic.this.onTickSkill.isPresent()
						&& IStatueMechanic.this.onTickSkill.get().isUsable(this.data)) {
					SkillMetadata sData = this.data.deepClone();
					AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
					HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
					targets.add(location);
					sData.setLocationTargets(targets);
					sData.setOrigin(location);
					IStatueMechanic.this.onTickSkill.get().execute(sData);
				}
				this.dur = 0;
			}
			if (!this.islocationtarget) {
				double x = this.currentLocation.getX();
				double z = this.currentLocation.getZ();
				Location l = this.owner.getLocation();
				l.setPitch(0f);
				Vector soV = MathUtils.getSideOffsetVectorFixed(this.owner.getLocation().getYaw(), this.sOffset,
						this.iYaw);
				Vector foV = MathUtils.getFrontBackOffsetVector(l.getDirection(), this.fOffset);
				x += soV.getX() + foV.getX();
				z += soV.getZ() + foV.getZ();
				this.currentLocation.setX(x);
				this.currentLocation.setZ(z);
				this.item.setVelocity(this.currentLocation.toVector().subtract(this.oldLocation.toVector()));
				NMSUtils.setLocation(this.item, this.oldLocation.getX(), this.oldLocation.getY(),
						this.oldLocation.getZ(), this.oldLocation.getYaw(), this.oldLocation.getPitch());
			} else {
				this.item.setVelocity(this.vEmpty);
				NMSUtils.setLocation(this.item, this.currentLocation.getX(), this.currentLocation.getY(),
						this.currentLocation.getZ(), this.currentLocation.getYaw(), this.currentLocation.getPitch());
			}
			if (this.lifetime)
				this.count++;
			this.dur++;
		}

		public void doHit(HashSet<AbstractEntity> targets) {
			if (IStatueMechanic.this.onHitSkill.isPresent()
					&& IStatueMechanic.this.onHitSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
				IStatueMechanic.this.onHitSkill.get().execute(sData);
			}
		}

		public void stop() {
			if (IStatueMechanic.this.onEndSkill.isPresent()
					&& IStatueMechanic.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				IStatueMechanic.this.onEndSkill.get().execute(sData.setOrigin(BukkitAdapter.adapt(this.currentLocation))
						.setLocationTarget(BukkitAdapter.adapt(this.currentLocation)));
			}
			TaskManager.get().cancelTask(this.taskId);
			this.item.remove();
			this.cancelled = true;
		}

		@Override
		public void setCancelled() {
		}

		@Override
		public boolean getCancelled() {
			return false;
		}
	}

}
