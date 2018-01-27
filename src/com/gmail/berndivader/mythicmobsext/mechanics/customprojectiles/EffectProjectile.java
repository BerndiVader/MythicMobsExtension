package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.utils.Utils;

public class EffectProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {
	protected Optional<Skill> onBounceSkill = Optional.empty();
	protected String onBounceSkillName;

	public EffectProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.mythicmobs = Utils.mythicmobs;
		this.onBounceSkillName = mlc.getString(new String[] { "onbounceskill", "onbounce", "ob" });
		if (this.onBounceSkillName != null) {
			this.onBounceSkill = this.skillmanager.getSkill(this.onBounceSkillName);
		}
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		try {
			new ProjectileTracker(data, target.clone().add(0.0, this.targetYOffset, 0.0));
			return true;
		} catch (Exception ex) {
			this.mythicmobs.handleException(ex);
			return false;
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.castAtLocation(data, target.getLocation().add(0.0, target.getEyeHeight() / 2.0, 0.0));
	}

	public class ProjectileTracker implements IParentSkill, Runnable {
		private SkillMetadata data;
		private boolean cancelled;
		private SkillCaster am;
		private float power;
		private float gravity;
		private long startTime;
		private AbstractLocation startLocation;
		private AbstractLocation currentLocation;
		private AbstractVector currentVelocity;
		private int currentX;
		private int currentZ;
		private int taskId;
		private Set<AbstractEntity> inRange;
		private HashSet<AbstractEntity> targets;
		private Map<AbstractEntity, Long> immune;
		private boolean eyedir;
		private float currentBounce, bounceReduce;

		public ProjectileTracker(SkillMetadata data, AbstractLocation target) {

			float noise;
			this.cancelled = false;
			this.gravity = 0.0f;
			this.inRange = ConcurrentHashMap.newKeySet();
			this.targets = new HashSet<AbstractEntity>();
			this.immune = new HashMap<AbstractEntity, Long>();
			this.cancelled = false;
			this.data = data;
			this.data.setCallingEvent(this);
			this.am = data.getCaster();
			this.power = data.getPower();
			this.startTime = System.currentTimeMillis();
			this.eyedir = EffectProjectile.this.eyedir;
			this.bounceReduce = EffectProjectile.this.bounceReduce;
			this.currentBounce = EffectProjectile.this.projectileVelocity;
			double velocity = 0.0;

			if (EffectProjectile.this.type == ProjectileType.METEOR) {
				this.startLocation = target.clone();
				this.startLocation.add(0.0, EffectProjectile.this.heightFromSurface, 0.0);
				if (EffectProjectile.this.projectileGravity <= 0.0f) {
					this.gravity = EffectProjectile.this.projectileVelocity;
					this.gravity = this.gravity > 0.0f ? this.gravity / EffectProjectile.this.ticksPerSecond : 0.0f;
				} else {
					this.gravity = EffectProjectile.this.projectileGravity > 0.0f
							? EffectProjectile.this.projectileGravity / EffectProjectile.this.ticksPerSecond : 0.0f;
				}
				velocity = 0.0;
			} else {
				this.startLocation = EffectProjectile.this.sourceIsOrigin ? data.getOrigin().clone()
						: data.getCaster().getEntity().getLocation().clone();
				velocity = EffectProjectile.this.projectileVelocity / EffectProjectile.this.ticksPerSecond;
				if (EffectProjectile.this.startYOffset != 0.0f) {
					this.startLocation.setY(this.startLocation.getY() + EffectProjectile.this.startYOffset);
				}
				if (EffectProjectile.this.startForwardOffset != 0.0f) {
					Vector v=Utils.getFrontBackOffsetVector(BukkitAdapter.adapt(this.startLocation).getDirection(),EffectProjectile.this.startForwardOffset);
					AbstractVector av=new AbstractVector(v.getX(),v.getY(),v.getZ());
					this.startLocation.add(av);
				}
				if (EffectProjectile.this.startSideOffset != 0.0f) {
					Vector v=Utils.getSideOffsetVector(this.startLocation.getYaw(), EffectProjectile.this.startSideOffset,false);
					AbstractVector av=new AbstractVector(v.getX(),v.getY(),v.getZ());
					this.startLocation.add(av);
				}
			}
			this.startLocation.clone();
			this.currentLocation = this.startLocation.clone();
			if (this.currentLocation == null) {
				return;
			}
			if (!this.eyedir) {
				this.currentVelocity = target.toVector().subtract(this.currentLocation.toVector()).normalize();
			} else {
				LivingEntity bukkitEntity = (LivingEntity) this.am.getEntity().getBukkitEntity();
				AbstractLocation al = BukkitAdapter.adapt(bukkitEntity.getEyeLocation());
				this.currentVelocity = al.getDirection().normalize();
			}
			if (EffectProjectile.this.projectileVelocityHorizOffset != 0.0f
					|| EffectProjectile.this.projectileVelocityHorizNoise > 0.0f) {
				noise = 0.0f;
				if (EffectProjectile.this.projectileVelocityHorizNoise > 0.0f) {
					noise = EffectProjectile.this.projectileVelocityHorizNoiseBase
							+ MythicMobs.r.nextFloat() * EffectProjectile.this.projectileVelocityHorizNoise;
				}
				this.currentVelocity.rotate(EffectProjectile.this.projectileVelocityHorizOffset + noise);
			}
			if (EffectProjectile.this.startSideOffset != 0.0f) {
				// empty if block
			}
			if (EffectProjectile.this.projectileVelocityVertOffset != 0.0f
					|| EffectProjectile.this.projectileVelocityVertNoise > 0.0f) {
				noise = 0.0f;
				if (EffectProjectile.this.projectileVelocityVertNoise > 0.0f) {
					noise = EffectProjectile.this.projectileVelocityVertNoiseBase
							+ MythicMobs.r.nextFloat() * EffectProjectile.this.projectileVelocityVertNoise;
				}
				this.currentVelocity
						.add(new AbstractVector(0.0f, EffectProjectile.this.projectileVelocityVertOffset + noise, 0.0f))
						.normalize();
			}
			if (EffectProjectile.this.hugSurface) {
				this.currentLocation
						.setY(((int) this.currentLocation.getY()) + EffectProjectile.this.heightFromSurface);
				this.currentVelocity.setY(0).normalize();
			}
			if (EffectProjectile.this.powerAffectsVelocity) {
				this.currentVelocity.multiply(this.power);
			}
			this.currentVelocity.multiply(velocity);
			if (EffectProjectile.this.projectileGravity > 0.0f) {
				this.currentVelocity.setY(this.currentVelocity.getY() - this.gravity);
			}

			this.taskId = TaskManager.get().scheduleTask(this, 0, EffectProjectile.this.tickInterval);
			if (EffectProjectile.this.hitPlayers || EffectProjectile.this.hitNonPlayers) {
				this.inRange
						.addAll(EffectProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						ActiveMob eam = null;
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())
								|| e.getBukkitEntity().hasMetadata(Utils.noTargetVar)) {
							return true;
						}
						if (!EffectProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!EffectProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
						if (EffectProjectile.this.mobmanager.isActiveMob(e)) {
							eam = EffectProjectile.this.mobmanager.getMythicMobInstance(e);
							if (eam.getOwner().isPresent()
									&& eam.getOwner().get().equals(this.am.getEntity().getUniqueId())) {
								return true;
							}
						}
					} else {
						return true;
					}
					return false;
				});
			}
			if (EffectProjectile.this.onStartSkill.isPresent()
					&& EffectProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = data.deepClone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(this.startLocation);
				sData.setLocationTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				EffectProjectile.this.onStartSkill.get().execute(sData);
			}
		}

		public void modifyVelocity(double v) {
			this.currentVelocity = this.currentVelocity.multiply(v);
		}

		public void modifyPower(float p) {
			this.power *= p;
		}

		public void modifyGravity(float p) {
			this.gravity *= p;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public void run() {
			if (this.cancelled) {
				return;
			}
			if (this.am != null && this.am.getEntity().isDead()) {
				this.stop();
				return;
			}
			if (this.startTime + EffectProjectile.this.duration < System.currentTimeMillis()) {
				this.stop();
				return;
			}
			this.currentLocation.clone();
			this.currentLocation.add(this.currentVelocity);
			if (EffectProjectile.this.hugSurface) {
				if (this.currentLocation.getBlockX() != this.currentX
						|| this.currentLocation.getBlockZ() != this.currentZ) {
					boolean ok;
					int attempts;
					Block b = BukkitAdapter
							.adapt(this.currentLocation.subtract(0.0, EffectProjectile.this.heightFromSurface, 0.0))
							.getBlock();
					if (BlockUtil.isPathable(b)) {
						attempts = 0;
						ok = false;
						while (attempts++ < 10) {
							if (BlockUtil.isPathable(b = b.getRelative(BlockFace.DOWN))) {
								this.currentLocation.add(0.0, -1.0, 0.0);
								continue;
							}
							ok = true;
							break;
						}
						if (!ok) {
							this.stop();
							return;
						}
					} else {
						attempts = 0;
						ok = false;
						while (attempts++ < 10) {
							b = b.getRelative(BlockFace.UP);
							this.currentLocation.add(0.0, 1.0, 0.0);
							if (!BlockUtil.isPathable(b))
								continue;
							ok = true;
							break;
						}
						if (!ok) {
							this.stop();
							return;
						}
					}
					this.currentLocation
							.setY(((int) this.currentLocation.getY()) + EffectProjectile.this.heightFromSurface);
					this.currentX = this.currentLocation.getBlockX();
					this.currentZ = this.currentLocation.getBlockZ();
				}
			} else if (EffectProjectile.this.projectileGravity != 0.0f) {
				if (EffectProjectile.this.bounce
						&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
					if (this.currentBounce < 0.0F) {
						this.stop();
						return;
					}
					this.currentBounce -= this.bounceReduce;
					this.currentVelocity.setY(this.currentBounce / EffectProjectile.this.ticksPerSecond);
					if (EffectProjectile.this.onBounceSkill.isPresent()
							&& EffectProjectile.this.onBounceSkill.get().isUsable(this.data)) {
						SkillMetadata sData = this.data.deepClone();
						AbstractLocation location = this.currentLocation.clone();
						HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
						targets.add(location);
						sData.setLocationTargets(targets);
						sData.setOrigin(location);
						EffectProjectile.this.onBounceSkill.get().execute(sData);
					}
				}
				this.currentVelocity.setY(this.currentVelocity.getY()
						- EffectProjectile.this.projectileGravity / EffectProjectile.this.ticksPerSecond);
			}
			if (EffectProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			if (this.currentLocation.distanceSquared(this.startLocation) >= EffectProjectile.this.maxDistanceSquared) {
				this.stop();
				return;
			}
			Location cl=BukkitAdapter.adapt(this.currentLocation);
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(cl, EffectProjectile.this.hitRadius,
						EffectProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getBukkitEntity().getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (EffectProjectile.this.onTickSkill.isPresent()
					&& EffectProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				AbstractLocation location = this.currentLocation.clone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(location);
				sData.setLocationTargets(targets);
				sData.setOrigin(location);
				EffectProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (EffectProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
			this.targets.clear();
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (EffectProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (EffectProjectile.this.onHitSkill.get().isUsable(sData)) {
					EffectProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (EffectProjectile.this.onEndSkill.isPresent()
					&& EffectProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				EffectProjectile.this.onEndSkill.get()
						.execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
			}
			TaskManager.get().cancelTask(this.taskId);
			this.cancelled = true;
		}

		@Override
		public void setCancelled() {
			this.stop();
		}

		@Override
		public boolean getCancelled() {
			return this.cancelled;
		}
	}

}
