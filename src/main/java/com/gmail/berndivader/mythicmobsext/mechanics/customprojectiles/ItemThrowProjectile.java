package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.Bukkit;
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
import com.gmail.berndivader.mythicmobsext.utils.EntityCacheHandler;
import com.gmail.berndivader.mythicmobsext.utils.HitBox;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.util.BlockUtil;

@ExternalAnnotation(name = "throwitem", author = "BerndiVader")
public class ItemThrowProjectile extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {
	Optional<Skill> onTickSkill = Optional.empty(), onHitSkill = Optional.empty(), onEndSkill = Optional.empty(),
			onStartSkill = Optional.empty();
	ItemStack item_stack;
	int duration, tickInterval;
	float hitRadius, verticalHitRadius, YOffset, velocity;
	double sOffset, fOffset;
	boolean hitPlayers, hitNonPlayers, invunerable, lifetime, stopGround, gravity, stopBlock;
	short durability;

	public ItemThrowProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

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
		String skill_name = mlc.getString(new String[] { "ontickskill", "ontick", "ot", "skill", "s", "meta", "m" });
		if (skill_name != null)
			this.onTickSkill = Utils.mythicmobs.getSkillManager().getSkill(skill_name);
		if ((skill_name = mlc.getString(new String[] { "onhitskill", "onhit", "oh" })) != null)
			this.onHitSkill = Utils.mythicmobs.getSkillManager().getSkill(skill_name);
		if ((skill_name = mlc.getString(new String[] { "onstartskill", "onstart", "os" })) != null)
			this.onStartSkill = Utils.mythicmobs.getSkillManager().getSkill(skill_name);
		if ((skill_name = mlc.getString(new String[] { "onendskill", "onend", "oe" })) != null)
			this.onEndSkill = Utils.mythicmobs.getSkillManager().getSkill(skill_name);

		this.tickInterval = mlc.getInteger(new String[] { "interval", "int", "i" }, 1);
		this.hitRadius = mlc.getFloat(new String[] { "horizontalradius", "hradius", "hr", "r" }, 1.25f);
		this.verticalHitRadius = mlc.getFloat(new String[] { "verticalradius", "vradius", "vr" }, this.hitRadius);
		this.duration = mlc.getInteger(new String[] { "maxduration", "md" }, 60);
		this.velocity = mlc.getFloat(new String[] { "velocity", "speed" }, 1.5f);
		this.YOffset = mlc.getFloat(new String[] { "yoffset", "yo" }, 1.0f);
		this.sOffset = mlc.getDouble(new String[] { "soffset", "so" }, 0d);
		this.fOffset = mlc.getDouble(new String[] { "foffset", "fo" }, 0d);
		this.hitPlayers = mlc.getBoolean(new String[] { "hitplayers", "hp" }, true);
		this.hitNonPlayers = mlc.getBoolean(new String[] { "hitnonplayers", "hnp" }, true);
		this.stopGround = mlc.getBoolean(new String[] { "stopground", "sg" }, true);
		this.stopBlock = mlc.getBoolean("stopblock", false);
		this.gravity = mlc.getBoolean("gravity", true);
		this.invunerable = mlc.getBoolean(new String[] { "invulnerable", "inv" }, true);
		this.durability = (short) MathUtils.clamp(mlc.getInteger("durability", Short.MIN_VALUE), Short.MIN_VALUE,
				Short.MAX_VALUE);
		this.lifetime = mlc.getBoolean(new String[] { "lifetime", "lt" }, true);
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
		private boolean cancelled, iYaw, islocationtarget, lifetime;
		private SkillMetadata data;
		private Item item;
		private SkillCaster caster;
		private Entity owner;
		private Location currentLocation, target_location, lastLocation;
		private int taskId;
		private HashSet<LivingEntity> targets;
		private Map<LivingEntity, Long> immune;
		private int count, dur;

		public ProjectileRunner(SkillMetadata data, AbstractEntity target) {
			this(data, target, null);
		}

		public ProjectileRunner(SkillMetadata data, AbstractLocation target) {
			this(data, null, target);
		}

		private ProjectileRunner(SkillMetadata data, AbstractEntity target, AbstractLocation location) {
			this.cancelled = false;
			this.data = data;
			this.data.setCallingEvent(this);
			this.caster = data.getCaster();
			this.owner = this.caster.getEntity().getBukkitEntity();
			this.islocationtarget = target == null && location != null;
			this.target_location = islocationtarget ? BukkitAdapter.adapt(location)
					: target.getBukkitEntity().getLocation();
			this.currentLocation = data.getCaster().getEntity().getBukkitEntity().getLocation();

			this.lifetime = ItemThrowProjectile.this.lifetime;
			this.count = 0;

			this.currentLocation.setY(this.currentLocation.getY() + ItemThrowProjectile.this.YOffset);
			if (ItemThrowProjectile.this.fOffset != 0d || ItemThrowProjectile.this.sOffset != 0d) {
				Vector offset;
				offset = MathUtils.getSideOffsetVectorFixed(this.currentLocation.getYaw(),
						ItemThrowProjectile.this.sOffset, this.iYaw);
				offset.add(MathUtils.getFrontBackOffsetVector(this.currentLocation.getDirection(),
						ItemThrowProjectile.this.fOffset));
				this.currentLocation.add(offset);
			}
			this.targets = new HashSet<LivingEntity>();
			this.immune = new HashMap<LivingEntity, Long>();

			float speed = ItemThrowProjectile.this.velocity;
			final Vector final_distance_sq = target_location.toVector().subtract(currentLocation.toVector())
					.normalize();
			Vector velocity = final_distance_sq.multiply(speed);

			this.item = this.currentLocation.getWorld().dropItem(this.currentLocation,
					new ItemStack(ItemThrowProjectile.this.item_stack));
			this.item.setVelocity(velocity);
			EntityCacheHandler.add(this.item);
			this.item.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setInvulnerable(true);
			this.item.setTicksLived(Integer.MAX_VALUE);
			this.item.setPickupDelay(Integer.MAX_VALUE);
			this.item.setGravity(ItemThrowProjectile.this.gravity);
			if (durability > Short.MIN_VALUE)
				this.item.getItemStack().setDurability(ItemThrowProjectile.this.durability);
			if (ItemThrowProjectile.this.onStartSkill.isPresent()
					&& ItemThrowProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = data.deepClone();
				sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
				sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
				ItemThrowProjectile.this.onStartSkill.get().execute(sData);
			}

			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this, 1l, 1l);
		}

		@Override
		public void run() {

			this.lastLocation = currentLocation.clone();
			this.currentLocation = this.item.getLocation();

			if (this.cancelled)
				return;
			if (this.count > ItemThrowProjectile.this.duration || this.item == null
					|| !BlockUtil.isPathable(this.currentLocation.getBlock())
					|| (item.isOnGround() && ItemThrowProjectile.this.stopGround)) {
				this.stop();
				return;
			}

			if (ItemThrowProjectile.this.stopBlock) {
				if (Volatile.handler.velocityChanged(item)) {
					this.stop();
					return;
				}
			}

			if (this.dur > ItemThrowProjectile.this.tickInterval) {
				HitBox hitBox = new HitBox(this.currentLocation, ItemThrowProjectile.this.hitRadius,
						ItemThrowProjectile.this.verticalHitRadius);
				ListIterator<Entity> lit1 = Volatile.handler.getNearbyEntities(item, 1).listIterator();
				while (lit1.hasNext()) {
					Entity ee = lit1.next();
					if (ee == this.owner || ee == item || ee.isDead() || !(ee instanceof LivingEntity)
							|| !hitBox.contains(ee.getLocation().add(0, 0.6, 0))
							|| this.immune.containsKey((LivingEntity) ee) || ee.hasMetadata(Utils.noTargetVar)
							|| (ee instanceof Player) && ee != item && !ItemThrowProjectile.this.hitPlayers
							|| !ItemThrowProjectile.this.hitNonPlayers && ee != item)
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
					if (ItemThrowProjectile.this.stopGround) {
						this.stop();
						return;
					}
				}
				this.targets.clear();
				if (ItemThrowProjectile.this.onTickSkill.isPresent()
						&& ItemThrowProjectile.this.onTickSkill.get().isUsable(this.data)) {
					SkillMetadata sData = this.data.deepClone();
					AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
					HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
					targets.add(location);
					sData.setLocationTargets(targets);
					sData.setOrigin(location);
					ItemThrowProjectile.this.onTickSkill.get().execute(sData);
				}
				this.dur = 0;
			}
			if (this.lifetime)
				this.count++;
			this.dur++;
		}

		void doHit(HashSet<AbstractEntity> targets) {
			if (ItemThrowProjectile.this.onHitSkill.isPresent()
					&& ItemThrowProjectile.this.onHitSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
				ItemThrowProjectile.this.onHitSkill.get().execute(sData);
			}
		}

		void stop() {
			if (ItemThrowProjectile.this.onEndSkill.isPresent()
					&& ItemThrowProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				ItemThrowProjectile.this.onEndSkill.get()
						.execute(sData.setOrigin(BukkitAdapter.adapt(this.currentLocation))
								.setLocationTarget(BukkitAdapter.adapt(this.currentLocation)));
			}
			this.item.remove();
			this.setCancelled();
			Bukkit.getScheduler().cancelTask(this.taskId);
		}

		@Override
		public void setCancelled() {
			this.cancelled = true;
		}

		@Override
		public boolean getCancelled() {
			return this.cancelled;
		}
	}
}
