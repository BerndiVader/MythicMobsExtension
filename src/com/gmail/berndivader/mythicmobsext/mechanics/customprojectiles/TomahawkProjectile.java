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
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.NMS.NMSUtils;

public class TomahawkProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {

	public TomahawkProjectile(String skill, MythicLineConfig mlc) {
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
			this.startTime = System.currentTimeMillis();
			this.pSpin = TomahawkProjectile.this.pEntitySpin;
			this.pFaceDir = TomahawkProjectile.this.pFaceDirection;
			this.pVOff = TomahawkProjectile.this.pVOffset;
			this.pFOff = TomahawkProjectile.this.pFOffset;
			this.targetable = TomahawkProjectile.this.targetable;
			this.eyedir = TomahawkProjectile.this.eyedir;
			double velocity = 0.0;

			this.startLocation = TomahawkProjectile.this.sourceIsOrigin ? data.getOrigin().clone()
					: data.getCaster().getEntity().getLocation().clone();
			velocity = TomahawkProjectile.this.projectileVelocity / TomahawkProjectile.this.ticksPerSecond;
			if (TomahawkProjectile.this.startYOffset != 0.0f) {
				this.startLocation.setY(this.startLocation.getY() + TomahawkProjectile.this.startYOffset);
			}
			if (TomahawkProjectile.this.startForwardOffset != 0.0f) {
				this.startLocation = this.startLocation.add(this.startLocation.getDirection().clone()
						.multiply(TomahawkProjectile.this.startForwardOffset));
			}
			if (TomahawkProjectile.this.startSideOffset != 0.0f) {
				this.startLocation.setPitch(0.0f);
				this.startLocation = MythicUtil.move(this.startLocation, 0.0, 0.0,
						TomahawkProjectile.this.startSideOffset);
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
			if (TomahawkProjectile.this.projectileVelocityHorizOffset != 0.0f
					|| TomahawkProjectile.this.projectileVelocityHorizNoise > 0.0f) {
				noise = 0.0f;
				if (TomahawkProjectile.this.projectileVelocityHorizNoise > 0.0f) {
					noise = TomahawkProjectile.this.projectileVelocityHorizNoiseBase
							+ MythicMobs.r.nextFloat() * TomahawkProjectile.this.projectileVelocityHorizNoise;
				}
				this.currentVelocity.rotate(TomahawkProjectile.this.projectileVelocityHorizOffset + noise);
			}
			if (TomahawkProjectile.this.projectileVelocityVertOffset != 0.0f
					|| TomahawkProjectile.this.projectileVelocityVertNoise > 0.0f) {
				noise = 0.0f;
				if (TomahawkProjectile.this.projectileVelocityVertNoise > 0.0f) {
					noise = TomahawkProjectile.this.projectileVelocityVertNoiseBase
							+ MythicMobs.r.nextFloat() * TomahawkProjectile.this.projectileVelocityVertNoise;
				}
				this.currentVelocity
						.add(new AbstractVector(0.0f, TomahawkProjectile.this.projectileVelocityVertOffset + noise, 0.0f))
						.normalize();
			}
			if (TomahawkProjectile.this.projectileGravity > 0.0f) {
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
				this.pEntity = TomahawkProjectile.this.mythicmobs.getAPIHelper().spawnMythicMob(customItemName,
						this.pLocation.add(0.0D, this.pVOff, 0.0D));
				this.pEntity.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
				if (!this.targetable)
					this.pEntity.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = TomahawkProjectile.this.mobmanager.getMythicMobInstance(this.pEntity);
			this.taskId = TaskManager.get().scheduleTask(this, 0, TomahawkProjectile.this.tickInterval);
			if (TomahawkProjectile.this.hitPlayers || TomahawkProjectile.this.hitNonPlayers) {
				this.inRange
						.addAll(TomahawkProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						ActiveMob eam = null;
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())
								|| e.getBukkitEntity().hasMetadata(Utils.noTargetVar)) {
							return true;
						}
						if (!TomahawkProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!TomahawkProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
						if (TomahawkProjectile.this.mobmanager.isActiveMob(e)) {
							eam = TomahawkProjectile.this.mobmanager.getMythicMobInstance(e);
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
			if (TomahawkProjectile.this.onStartSkill.isPresent()
					&& TomahawkProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				TomahawkProjectile.this.onStartSkill.get().execute(sData);
			}
		}

		public void modifyVelocity(double v) {
			this.currentVelocity = this.currentVelocity.multiply(v);
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
			if (this.startTime + TomahawkProjectile.this.duration < System.currentTimeMillis()) {
				this.stop();
				return;
			}
			this.currentLocation.clone();
			this.currentLocation.add(this.currentVelocity);
			if (TomahawkProjectile.this.hugSurface) {
				if (this.currentLocation.getBlockX() != this.currentX
						|| this.currentLocation.getBlockZ() != this.currentZ) {
					boolean ok;
					int attempts;
					Block b = BukkitAdapter
							.adapt(this.currentLocation.subtract(0.0, TomahawkProjectile.this.heightFromSurface, 0.0))
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
							.setY(((int) this.currentLocation.getY()) + TomahawkProjectile.this.heightFromSurface);
					this.currentX = this.currentLocation.getBlockX();
					this.currentZ = this.currentLocation.getBlockZ();
				}
			} else if (TomahawkProjectile.this.projectileGravity != 0.0f) {
				this.currentVelocity.setY(this.currentVelocity.getY()
						- TomahawkProjectile.this.projectileGravity / TomahawkProjectile.this.ticksPerSecond);
			}
			if (TomahawkProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			if (this.currentLocation.distanceSquared(this.startLocation) >= TomahawkProjectile.this.maxDistanceSquared) {
				this.stop();
				return;
			}
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(this.pam.getEntity().getBukkitEntity().getLocation(), TomahawkProjectile.this.hitRadius,
						TomahawkProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getBukkitEntity().getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (TomahawkProjectile.this.onTickSkill.isPresent()
					&& TomahawkProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				TomahawkProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (TomahawkProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
			Location eloc = this.pEntity.getLocation();
			float yaw = eloc.getYaw();
			if (this.pSpin != 0.0) {
				yaw = ((yaw + this.pSpin) % 360.0F);
			}
			NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY() + this.pVOff,
					this.currentLocation.getZ(), yaw, eloc.getPitch());
			this.targets.clear();
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (TomahawkProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (TomahawkProjectile.this.onHitSkill.get().isUsable(sData)) {
					TomahawkProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (TomahawkProjectile.this.onEndSkill.isPresent()
					&& TomahawkProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				TomahawkProjectile.this.onEndSkill.get()
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
