package com.gmail.berndivader.mmcustomskills26.customprojectiles;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import io.lumine.xikage.mythicmobs.util.HitBox;
import io.lumine.xikage.mythicmobs.util.MythicUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;

public class EntityProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {

	protected String pEntityName;
	protected float pEntitySpin;
	protected float pEntityPitchOffset;

	public EntityProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		this.pEntityName = mlc.getString(new String[] { "pobject", "projectileentity", "pentity" }, "MINECART")
				.toUpperCase();
		this.pEntitySpin = mlc.getFloat("pspin", 0.0F);
		this.pEntityPitchOffset = mlc.getFloat("ppOff", 360.0f);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		try {
			new ProjectileTracker(data, this.pEntityName, target.clone().add(0.0, this.targetYOffset, 0.0));
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
		private Entity pEntity;
		private Location pLocation;
		private float pSpin;
		private double pVOff;
		private double pFOff;
		private boolean pFaceDir, targetable, eyedir;
		private float currentBounce, bounceReduce;

		public ProjectileTracker(SkillMetadata data, String customItemName, AbstractLocation target) {

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
			this.pSpin = EntityProjectile.this.pEntitySpin;
			this.pFaceDir = EntityProjectile.this.pFaceDirection;
			this.pVOff = EntityProjectile.this.pVOffset;
			this.pFOff = EntityProjectile.this.pFOffset;
			this.targetable = EntityProjectile.this.targetable;
			this.eyedir = EntityProjectile.this.eyedir;
			this.bounceReduce = EntityProjectile.this.bounceReduce;
			this.currentBounce = EntityProjectile.this.projectileVelocity;
			double velocity = 0.0;

			if (EntityProjectile.this.type == ProjectileType.METEOR) {
				this.startLocation = target.clone();
				this.startLocation.add(0.0, EntityProjectile.this.heightFromSurface, 0.0);
				if (EntityProjectile.this.projectileGravity <= 0.0f) {
					this.gravity = EntityProjectile.this.projectileVelocity;
					this.gravity = this.gravity > 0.0f ? this.gravity / EntityProjectile.this.ticksPerSecond : 0.0f;
				} else {
					this.gravity = EntityProjectile.this.projectileGravity > 0.0f
							? EntityProjectile.this.projectileGravity / EntityProjectile.this.ticksPerSecond : 0.0f;
				}
				velocity = 0.0;
			} else {
				this.startLocation = EntityProjectile.this.sourceIsOrigin ? data.getOrigin().clone()
						: data.getCaster().getEntity().getLocation().clone();
				velocity = EntityProjectile.this.projectileVelocity / EntityProjectile.this.ticksPerSecond;
				if (EntityProjectile.this.startYOffset != 0.0f) {
					this.startLocation.setY(this.startLocation.getY() + EntityProjectile.this.startYOffset);
				}
				if (EntityProjectile.this.startForwardOffset != 0.0f) {
					this.startLocation = this.startLocation.add(this.startLocation.getDirection().clone()
							.multiply(EntityProjectile.this.startForwardOffset));
				}
				if (EntityProjectile.this.startSideOffset != 0.0f) {
					this.startLocation.setPitch(0.0f);
					this.startLocation = MythicUtil.move(this.startLocation, 0.0, 0.0,
							EntityProjectile.this.startSideOffset);
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
			if (EntityProjectile.this.projectileVelocityHorizOffset != 0.0f
					|| EntityProjectile.this.projectileVelocityHorizNoise > 0.0f) {
				noise = 0.0f;
				if (EntityProjectile.this.projectileVelocityHorizNoise > 0.0f) {
					noise = EntityProjectile.this.projectileVelocityHorizNoiseBase
							+ MythicMobs.r.nextFloat() * EntityProjectile.this.projectileVelocityHorizNoise;
				}
				this.currentVelocity.rotate(EntityProjectile.this.projectileVelocityHorizOffset + noise);
			}
			if (EntityProjectile.this.startSideOffset != 0.0f) {
				// empty if block
			}
			if (EntityProjectile.this.projectileVelocityVertOffset != 0.0f
					|| EntityProjectile.this.projectileVelocityVertNoise > 0.0f) {
				noise = 0.0f;
				if (EntityProjectile.this.projectileVelocityVertNoise > 0.0f) {
					noise = EntityProjectile.this.projectileVelocityVertNoiseBase
							+ MythicMobs.r.nextFloat() * EntityProjectile.this.projectileVelocityVertNoise;
				}
				this.currentVelocity
						.add(new AbstractVector(0.0f, EntityProjectile.this.projectileVelocityVertOffset + noise, 0.0f))
						.normalize();
			}
			if (EntityProjectile.this.hugSurface) {
				this.currentLocation
						.setY(((int) this.currentLocation.getY()) + EntityProjectile.this.heightFromSurface);
				this.currentVelocity.setY(0).normalize();
			}
			if (EntityProjectile.this.powerAffectsVelocity) {
				this.currentVelocity.multiply(this.power);
			}
			this.currentVelocity.multiply(velocity);
			if (EntityProjectile.this.projectileGravity > 0.0f) {
				this.currentVelocity.setY(this.currentVelocity.getY() - this.gravity);
			}
			this.pLocation = BukkitAdapter.adapt(this.startLocation.clone());
			float yaw = this.pLocation.getYaw();
			if (this.pFaceDir && !this.eyedir) {
				yaw = CustomSkillStuff.lookAtYaw(this.pLocation, BukkitAdapter.adapt(target));
				this.pLocation.setYaw(yaw);
			}
			this.pLocation.add(this.pLocation.getDirection().clone().multiply(this.pFOff));
			this.pEntity = this.pLocation.getWorld().spawnEntity(this.pLocation.add(0.0d, this.pVOff, 0.0d),
					EntityType.valueOf(customItemName));
			this.pEntity.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			if (!this.targetable)
				this.pEntity.setMetadata(Main.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.pEntity.setGravity(false);
			Main.getPlugin().getVolatileHandler().changeHitBox(this.pEntity,0,0,0);
			this.taskId = TaskManager.get().scheduleTask(this, 0, EntityProjectile.this.tickInterval);
			if (EntityProjectile.this.hitPlayers || EntityProjectile.this.hitNonPlayers) {
				this.inRange
						.addAll(EntityProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())) {
							return true;
						}
						if (!EntityProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!EntityProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
					} else {
						return true;
					}
					return false;
				});
			}
			if (EntityProjectile.this.onStartSkill.isPresent()
					&& EntityProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = data.deepClone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(this.startLocation);
				sData.setLocationTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				EntityProjectile.this.onStartSkill.get().execute(sData);
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
			if (this.startTime + EntityProjectile.this.duration < System.currentTimeMillis()) {
				this.stop();
				return;
			}
			this.currentLocation.clone();
			this.currentLocation.add(this.currentVelocity);
			if (EntityProjectile.this.hugSurface) {
				if (this.currentLocation.getBlockX() != this.currentX
						|| this.currentLocation.getBlockZ() != this.currentZ) {
					boolean ok;
					int attempts;
					Block b = BukkitAdapter
							.adapt(this.currentLocation.subtract(0.0, EntityProjectile.this.heightFromSurface, 0.0))
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
							.setY(((int) this.currentLocation.getY()) + EntityProjectile.this.heightFromSurface);
					this.currentX = this.currentLocation.getBlockX();
					this.currentZ = this.currentLocation.getBlockZ();
				}
			} else if (EntityProjectile.this.projectileGravity != 0.0f) {
				if (EntityProjectile.this.bounce
						&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
					if (this.currentBounce < 0.0F) {
						this.stop();
						return;
					}
					this.currentBounce -= this.bounceReduce;
					this.currentVelocity.setY(this.currentBounce / EntityProjectile.this.ticksPerSecond);
				}
				this.currentVelocity.setY(this.currentVelocity.getY()
						- EntityProjectile.this.projectileGravity / EntityProjectile.this.ticksPerSecond);
			}
			if (EntityProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			if (this.currentLocation.distanceSquared(this.startLocation) >= EntityProjectile.this.maxDistanceSquared) {
				this.stop();
				return;
			}
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(this.currentLocation, EntityProjectile.this.hitRadius,
						EntityProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (EntityProjectile.this.onTickSkill.isPresent()
					&& EntityProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				AbstractLocation location = this.currentLocation.clone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(location);
				sData.setLocationTargets(targets);
				sData.setOrigin(location);
				EntityProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (EntityProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
			Location eloc = this.pEntity.getLocation();
			float yaw = eloc.getYaw();
			if (this.pSpin != 0.0) {
				yaw = ((yaw + this.pSpin) % 360.0F);
			}
			NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY(),
					this.currentLocation.getZ(), yaw, eloc.getPitch());
			this.targets.clear();
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (EntityProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (EntityProjectile.this.onHitSkill.get().isUsable(sData)) {
					EntityProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (EntityProjectile.this.onEndSkill.isPresent()
					&& EntityProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				EntityProjectile.this.onEndSkill.get()
						.execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
			}
			this.pEntity.remove();
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
