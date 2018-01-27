package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.NMS.NMSUtils;

public class MythicProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {

	public MythicProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.pEntityName = mlc.getString(new String[] { "pobject", "projectilemythic", "pmythic" }, "MINECART");
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
		private ActiveMob pam;
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
			this.pSpin = MythicProjectile.this.pEntitySpin;
			this.pFaceDir = MythicProjectile.this.pFaceDirection;
			this.pVOff = MythicProjectile.this.pVOffset;
			this.pFOff = MythicProjectile.this.pFOffset;
			this.targetable = MythicProjectile.this.targetable;
			this.eyedir = MythicProjectile.this.eyedir;
			this.bounceReduce = MythicProjectile.this.bounceReduce;
			this.currentBounce = MythicProjectile.this.projectileVelocity;
			double velocity = 0.0;

			if (MythicProjectile.this.type == ProjectileType.METEOR) {
				this.startLocation = target.clone();
				this.startLocation.add(0.0, MythicProjectile.this.heightFromSurface, 0.0);
				if (MythicProjectile.this.projectileGravity <= 0.0f) {
					this.gravity = MythicProjectile.this.projectileVelocity;
					this.gravity = this.gravity > 0.0f ? this.gravity / MythicProjectile.this.ticksPerSecond : 0.0f;
				} else {
					this.gravity = MythicProjectile.this.projectileGravity > 0.0f
							? MythicProjectile.this.projectileGravity / MythicProjectile.this.ticksPerSecond : 0.0f;
				}
				velocity = 0.0;
			} else {
				this.startLocation = MythicProjectile.this.sourceIsOrigin ? data.getOrigin().clone()
						: data.getCaster().getEntity().getLocation().clone();
				velocity = MythicProjectile.this.projectileVelocity / MythicProjectile.this.ticksPerSecond;
				if (MythicProjectile.this.startYOffset != 0.0f) {
					this.startLocation.setY(this.startLocation.getY() + MythicProjectile.this.startYOffset);
				}
				if (MythicProjectile.this.startForwardOffset != 0.0f) {
					Vector v=Utils.getFrontBackOffsetVector(BukkitAdapter.adapt(this.startLocation).getDirection(),MythicProjectile.this.startForwardOffset);
					AbstractVector av=new AbstractVector(v.getX(),v.getY(),v.getZ());
					this.startLocation.add(av);
				}
				if (MythicProjectile.this.startSideOffset != 0.0f) {
					Vector v=Utils.getSideOffsetVector(this.startLocation.getYaw(), MythicProjectile.this.startSideOffset,false);
					AbstractVector av=new AbstractVector(v.getX(),v.getY(),v.getZ());
					this.startLocation.add(av);
				}
			}
			this.currentLocation = this.startLocation.clone();
			if (this.currentLocation == null) return;
			if (!this.eyedir) {
				this.currentVelocity = target.toVector().subtract(this.currentLocation.toVector()).normalize();
			} else {
				LivingEntity bukkitEntity = (LivingEntity) this.am.getEntity().getBukkitEntity();
				AbstractLocation al = BukkitAdapter.adapt(bukkitEntity.getEyeLocation());
				this.currentVelocity = al.getDirection().normalize();
			}
			if (MythicProjectile.this.projectileVelocityHorizOffset != 0.0f
					|| MythicProjectile.this.projectileVelocityHorizNoise > 0.0f) {
				noise = 0.0f;
				if (MythicProjectile.this.projectileVelocityHorizNoise > 0.0f) {
					noise = MythicProjectile.this.projectileVelocityHorizNoiseBase
							+ MythicMobs.r.nextFloat() * MythicProjectile.this.projectileVelocityHorizNoise;
				}
				this.currentVelocity.rotate(MythicProjectile.this.projectileVelocityHorizOffset + noise);
			}
			if (MythicProjectile.this.projectileVelocityVertOffset != 0.0f
					|| MythicProjectile.this.projectileVelocityVertNoise > 0.0f) {
				noise = 0.0f;
				if (MythicProjectile.this.projectileVelocityVertNoise > 0.0f) {
					noise = MythicProjectile.this.projectileVelocityVertNoiseBase
							+ MythicMobs.r.nextFloat() * MythicProjectile.this.projectileVelocityVertNoise;
				}
				this.currentVelocity
						.add(new AbstractVector(0.0f, MythicProjectile.this.projectileVelocityVertOffset + noise, 0.0f))
						.normalize();
			}
			if (MythicProjectile.this.hugSurface) {
				this.currentLocation
						.setY(((int) this.currentLocation.getY()) + MythicProjectile.this.heightFromSurface);
				this.currentVelocity.setY(0).normalize();
			}
			if (MythicProjectile.this.powerAffectsVelocity) {
				this.currentVelocity.multiply(this.power);
			}
			this.currentVelocity.multiply(velocity);
			if (MythicProjectile.this.projectileGravity > 0.0f) {
				this.currentVelocity.setY(this.currentVelocity.getY() - this.gravity);
			}
			this.pLocation = BukkitAdapter.adapt(this.startLocation.clone());
			float yaw = this.pLocation.getYaw();
			if (this.pFaceDir && !this.eyedir) {
				yaw = Utils.lookAtYaw(this.pLocation, BukkitAdapter.adapt(target));
				this.pLocation.setYaw(yaw);
			}
			this.pLocation.add(this.pLocation.getDirection().clone().multiply(this.pFOff));
			try {
				this.pEntity = MythicProjectile.this.mythicmobs.getAPIHelper().spawnMythicMob(customItemName,
						this.pLocation.add(0.0D, this.pVOff, 0.0D));
				this.pEntity.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
				if (!this.targetable)
					Main.getPlugin().getVolatileHandler().changeHitBox(this.pEntity,0,0,0);
					this.pEntity.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = MythicProjectile.this.mobmanager.getMythicMobInstance(this.pEntity);
			this.taskId = TaskManager.get().scheduleTask(this, 0, MythicProjectile.this.tickInterval);
			if (MythicProjectile.this.hitPlayers || MythicProjectile.this.hitNonPlayers) {
				this.inRange
						.addAll(MythicProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						ActiveMob eam = null;
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())
								|| e.getBukkitEntity().hasMetadata(Utils.noTargetVar)) {
							return true;
						}
						if (!MythicProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!MythicProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
						if (MythicProjectile.this.mobmanager.isActiveMob(e)) {
							eam = MythicProjectile.this.mobmanager.getMythicMobInstance(e);
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
			if (MythicProjectile.this.onStartSkill.isPresent()
					&& MythicProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				MythicProjectile.this.onStartSkill.get().execute(sData);
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
			if (this.startTime + MythicProjectile.this.duration < System.currentTimeMillis()) {
				this.stop();
				return;
			}
			this.currentLocation.clone();
			this.currentLocation.add(this.currentVelocity);
			if (MythicProjectile.this.hugSurface) {
				if (this.currentLocation.getBlockX() != this.currentX
						|| this.currentLocation.getBlockZ() != this.currentZ) {
					boolean ok;
					int attempts;
					Block b = BukkitAdapter
							.adapt(this.currentLocation.subtract(0.0, MythicProjectile.this.heightFromSurface, 0.0))
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
							.setY(((int) this.currentLocation.getY()) + MythicProjectile.this.heightFromSurface);
					this.currentX = this.currentLocation.getBlockX();
					this.currentZ = this.currentLocation.getBlockZ();
				}
			} else if (MythicProjectile.this.projectileGravity != 0.0f) {
				if (MythicProjectile.this.bounce
						&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
					if (this.currentBounce < 0.0F) {
						this.stop();
						return;
					}
					this.currentBounce -= this.bounceReduce;
					this.currentVelocity.setY(this.currentBounce / MythicProjectile.this.ticksPerSecond);
					if (MythicProjectile.this.onBounceSkill.isPresent()
							&& MythicProjectile.this.onBounceSkill.get().isUsable(this.data)) {
						SkillMetadata sData = this.data.deepClone();
						sData.setCaster(this.pam);
						sData.setTrigger(sData.getCaster().getEntity());
						AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
						HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
						targets.add(entity);
						sData.setEntityTargets(targets);
						sData.setOrigin(this.currentLocation);
						MythicProjectile.this.onBounceSkill.get().execute(sData);
					}
				}
				this.currentVelocity.setY(this.currentVelocity.getY()
						- MythicProjectile.this.projectileGravity / MythicProjectile.this.ticksPerSecond);
			}
			if (MythicProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			if (this.currentLocation.distanceSquared(this.startLocation) >= MythicProjectile.this.maxDistanceSquared) {
				this.stop();
				return;
			}
			Location eloc = this.pEntity.getLocation();
			float yaw = eloc.getYaw();
			if (this.pSpin != 0.0) {
				yaw = ((yaw + this.pSpin) % 360.0F);
			}
			NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY() + this.pVOff,
					this.currentLocation.getZ(), yaw, eloc.getPitch());
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(this.pam.getEntity().getBukkitEntity().getLocation(), MythicProjectile.this.hitRadius,
						MythicProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getBukkitEntity().getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (MythicProjectile.this.onTickSkill.isPresent()
					&& MythicProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				MythicProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (MythicProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
			this.targets.clear();
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (MythicProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (MythicProjectile.this.onHitSkill.get().isUsable(sData)) {
					MythicProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (MythicProjectile.this.onEndSkill.isPresent()
					&& MythicProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				MythicProjectile.this.onEndSkill.get()
						.execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
			}
			TaskManager.get().cancelTask(this.taskId);
			this.pEntity.remove();
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
