package main.java.com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import main.java.com.gmail.berndivader.mythicmobsext.Main;
import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;
import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

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

public class BlockProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {

	protected String pEntityName;
	protected float pEntitySpin;
	protected float pEntityPitchOffset;

	public BlockProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		this.pEntityName = mlc.getString(new String[] { "pobject", "projectileblock", "pblock" }, "DIRT").toUpperCase();
		this.pEntitySpin = mlc.getFloat("pspin", 0.0F);
		this.pEntityPitchOffset = mlc.getFloat("ppOff", 360.0f);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		try {
			new ProjectileTracker(data, this.pEntityName, target.clone().add(0.0, this.targetYOffset, 0.0));
			return true;
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
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
		private FallingBlock pBlock;
		private Location pLocation;
		private float pSpin;
		private double pVOff;
		private double pFOff;
		private boolean pFaceDir, targetable, eyedir;
		private float currentBounce, bounceReduce;

		@SuppressWarnings({ "deprecation" })
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
			this.pSpin = BlockProjectile.this.pEntitySpin;
			this.pFaceDir = BlockProjectile.this.pFaceDirection;
			this.pVOff = BlockProjectile.this.pVOffset;
			this.pFOff = BlockProjectile.this.pFOffset;
			this.targetable = BlockProjectile.this.targetable;
			this.eyedir = BlockProjectile.this.eyedir;
			this.bounceReduce = BlockProjectile.this.bounceReduce;
			this.currentBounce = BlockProjectile.this.projectileVelocity;
			double velocity = 0.0;

			if (BlockProjectile.this.type == ProjectileType.METEOR) {
				this.startLocation = target.clone();
				this.startLocation.add(0.0, BlockProjectile.this.heightFromSurface, 0.0);
				if (BlockProjectile.this.projectileGravity <= 0.0f) {
					this.gravity = BlockProjectile.this.projectileVelocity;
					this.gravity = this.gravity > 0.0f ? this.gravity / BlockProjectile.this.ticksPerSecond : 0.0f;
				} else {
					this.gravity = BlockProjectile.this.projectileGravity > 0.0f
							? BlockProjectile.this.projectileGravity / BlockProjectile.this.ticksPerSecond : 0.0f;
				}
				velocity = 0.0;
			} else {
				this.startLocation = BlockProjectile.this.sourceIsOrigin ? data.getOrigin().clone()
						: data.getCaster().getEntity().getLocation().clone();
				velocity = BlockProjectile.this.projectileVelocity / BlockProjectile.this.ticksPerSecond;
				if (BlockProjectile.this.startYOffset != 0.0f) {
					this.startLocation.setY(this.startLocation.getY() + BlockProjectile.this.startYOffset);
				}
				if (BlockProjectile.this.startForwardOffset != 0.0f) {
					Vector v=Utils.getFrontBackOffsetVector(BukkitAdapter.adapt(this.startLocation).getDirection(),BlockProjectile.this.startForwardOffset);
					AbstractVector av=new AbstractVector(v.getX(),v.getY(),v.getZ());
					this.startLocation.add(av);
				}
				if (BlockProjectile.this.startSideOffset != 0.0f) {
					Vector v=Utils.getSideOffsetVector(this.startLocation.getYaw(), BlockProjectile.this.startSideOffset,false);
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
			if (BlockProjectile.this.projectileVelocityHorizOffset != 0.0f
					|| BlockProjectile.this.projectileVelocityHorizNoise > 0.0f) {
				noise = 0.0f;
				if (BlockProjectile.this.projectileVelocityHorizNoise > 0.0f) {
					noise = BlockProjectile.this.projectileVelocityHorizNoiseBase
							+ MythicMobs.r.nextFloat() * BlockProjectile.this.projectileVelocityHorizNoise;
				}
				this.currentVelocity.rotate(BlockProjectile.this.projectileVelocityHorizOffset + noise);
			}
			if (BlockProjectile.this.startSideOffset != 0.0f) {
				// empty if block
			}
			if (BlockProjectile.this.projectileVelocityVertOffset != 0.0f
					|| BlockProjectile.this.projectileVelocityVertNoise > 0.0f) {
				noise = 0.0f;
				if (BlockProjectile.this.projectileVelocityVertNoise > 0.0f) {
					noise = BlockProjectile.this.projectileVelocityVertNoiseBase
							+ MythicMobs.r.nextFloat() * BlockProjectile.this.projectileVelocityVertNoise;
				}
				this.currentVelocity
						.add(new AbstractVector(0.0f, BlockProjectile.this.projectileVelocityVertOffset + noise, 0.0f))
						.normalize();
			}
			if (BlockProjectile.this.hugSurface) {
				this.currentLocation.setY(((int) this.currentLocation.getY()) + BlockProjectile.this.heightFromSurface);
				this.currentVelocity.setY(0).normalize();
			}
			if (BlockProjectile.this.powerAffectsVelocity) {
				this.currentVelocity.multiply(this.power);
			}
			this.currentVelocity.multiply(velocity);
			if (BlockProjectile.this.projectileGravity > 0.0f) {
				this.currentVelocity.setY(this.currentVelocity.getY() - this.gravity);
			}
			this.pLocation = BukkitAdapter.adapt(this.startLocation.clone());
			float yaw = this.pLocation.getYaw();
			if (this.pFaceDir && !this.eyedir) {
				yaw = Utils.lookAtYaw(this.pLocation, BukkitAdapter.adapt(target));
				this.pLocation.setYaw(yaw);
			}
			this.pLocation.add(this.pLocation.getDirection().clone().multiply(this.pFOff));
			this.pBlock = this.pLocation.getWorld().spawnFallingBlock(this.pLocation.add(0.0d, this.pVOff, 0.0d),
					Material.valueOf(customItemName), (byte) 0);
			Main.entityCache.add(this.pBlock);
			this.pBlock.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			if (!this.targetable)
				this.pBlock.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.pBlock.setHurtEntities(false);
			this.pBlock.setDropItem(false);
			this.pBlock.setTicksLived(Integer.MAX_VALUE);
			this.pBlock.setInvulnerable(true);
			this.pBlock.setGravity(false);
			Volatile.handler.changeHitBox((Entity)this.pBlock,0,0,0);

			this.taskId = TaskManager.get().scheduleTask(this, 0, BlockProjectile.this.tickInterval);
			if (BlockProjectile.this.hitPlayers || BlockProjectile.this.hitNonPlayers) {
				this.inRange
						.addAll(BlockProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())) {
							return true;
						}
						if (e.getBukkitEntity().hasMetadata(Utils.noTargetVar)) return true;
						if (!BlockProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!BlockProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
					} else {
						return true;
					}
					return false;
				});
			}
			if (BlockProjectile.this.onStartSkill.isPresent()
					&& BlockProjectile.this.onStartSkill.get().isUsable(data)) {
				SkillMetadata sData = data.deepClone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(this.startLocation);
				sData.setLocationTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				BlockProjectile.this.onStartSkill.get().execute(sData);
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
			if (this.startTime + BlockProjectile.this.duration < System.currentTimeMillis()) {
				this.stop();
				return;
			}
			this.currentLocation.clone();
			this.currentLocation.add(this.currentVelocity);
			if (BlockProjectile.this.hugSurface) {
				if (this.currentLocation.getBlockX() != this.currentX
						|| this.currentLocation.getBlockZ() != this.currentZ) {
					boolean ok;
					int attempts;
					Block b = BukkitAdapter
							.adapt(this.currentLocation.subtract(0.0, BlockProjectile.this.heightFromSurface, 0.0))
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
							.setY(((int) this.currentLocation.getY()) + BlockProjectile.this.heightFromSurface);
					this.currentX = this.currentLocation.getBlockX();
					this.currentZ = this.currentLocation.getBlockZ();
				}
			} else if (BlockProjectile.this.projectileGravity != 0.0f) {
				if (BlockProjectile.this.bounce
						&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
					if (this.currentBounce < 0.0F) {
						this.stop();
						return;
					}
					this.currentBounce -= this.bounceReduce;
					this.currentVelocity.setY(this.currentBounce / BlockProjectile.this.ticksPerSecond);
				}
				this.currentVelocity.setY(this.currentVelocity.getY()
						- BlockProjectile.this.projectileGravity / BlockProjectile.this.ticksPerSecond);
			}
			if (BlockProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			if (this.currentLocation.distanceSquared(this.startLocation) >= BlockProjectile.this.maxDistanceSquared) {
				this.stop();
				return;
			}
			Location loc = BukkitAdapter.adapt(this.currentLocation).clone();
			Location eloc = this.pBlock.getLocation().clone();
			this.pBlock.setVelocity(loc.toVector().subtract(eloc.toVector()).multiply(0.5));
			this.targets.clear();
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(this.currentLocation, BlockProjectile.this.hitRadius,
						BlockProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (BlockProjectile.this.onTickSkill.isPresent()
					&& BlockProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				AbstractLocation location = this.currentLocation.clone();
				HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
				targets.add(location);
				sData.setLocationTargets(targets);
				sData.setOrigin(location);
				BlockProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (BlockProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (BlockProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (BlockProjectile.this.onHitSkill.get().isUsable(sData)) {
					BlockProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (BlockProjectile.this.onEndSkill.isPresent()
					&& BlockProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				BlockProjectile.this.onEndSkill.get()
						.execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
			}
			TaskManager.get().cancelTask(this.taskId);
			this.pBlock.remove();
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
