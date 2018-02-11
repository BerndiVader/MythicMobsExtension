package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.SkillAnnotation;
import com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles.CustomProjectile;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
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
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import io.lumine.xikage.mythicmobs.util.HitBox;

@SkillAnnotation(name="mythicorbitalprojectile",author="BerndiVader")
public class MythicOrbitalProjectile extends CustomProjectile implements ITargetedEntitySkill, ITargetedLocationSkill {

	protected String pEntityName;
	protected float pEntitySpin;
	protected int pEntityPitchOffset;
	protected float oRadiusX;
	protected float oRadiusZ;
	protected float oRadiusY;
	protected float oRadiusPerSec;
	protected boolean invisible, ct, tc, lifetime;
	protected String tag;

	public MythicOrbitalProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.pEntityName = mlc.getString(new String[] { "pobject", "projectilemythic", "pmythic" }, "MINECART");
		this.pEntitySpin = mlc.getFloat("pspin", 0.0F);
		this.pEntityPitchOffset = mlc.getInteger("ppOff", 0);
		this.pFaceDirection = mlc.getBoolean("pfacedir", false);
		this.pVOffset = mlc.getDouble("pvoff", 0.0D);
		this.pFOffset = mlc.getFloat("pfoff", 0.0F);
		this.oRadiusX = mlc.getFloat("oradx", 0.0F);
		this.oRadiusZ = mlc.getFloat("oradz", 0.0F);
		this.oRadiusY = mlc.getFloat("orady", 0.0F);
		this.oRadiusPerSec = mlc.getFloat("oradsec", 0.0F);
		this.targetable = mlc.getBoolean("targetable", false);
		this.tag = mlc.getString("tag");
		this.invisible = mlc.getBoolean("invis", false);
		this.ct = mlc.getBoolean("ct", false);
		this.tc = mlc.getBoolean("tc", false);
		this.lifetime = mlc.getBoolean("lt", false);

	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			new ProjectileTracker(data, this.pEntityName, target);
			return true;
		} catch (Exception ex) {
			this.mythicmobs.handleException(ex);
			return false;
		}
	}

	public class ProjectileTracker implements IParentSkill, Runnable {
		private SkillMetadata data;
		private boolean cancelled;
		private SkillCaster am;
		private ActiveMob cam;
		private long startTime;
		private AbstractLocation currentLocation;
		private int taskId;
		private Set<AbstractEntity> inRange;
		private HashSet<AbstractEntity> targets;
		private Map<AbstractEntity, Long> immune;
		private ActiveMob pam;
		private Entity pEntity;
		private Location centerLocation;
		private float radiusX, radiusZ, radiusY, radPerSec, radPerTick;
		private float pSpin;
		private int ppOff;
		private float pFOff;
		private double pVOff;
		private boolean pFaceDir;
		private int tick;
		private AbstractEntity target;
		private boolean targetable, ct, tc, lt;
		private String tag;

		public ProjectileTracker(SkillMetadata data, String customItemName, AbstractEntity t) {

			this.cancelled = false;
			this.inRange = ConcurrentHashMap.newKeySet();
			this.targets = new HashSet<AbstractEntity>();
			this.immune = new HashMap<AbstractEntity, Long>();
			this.cancelled = false;
			this.data = data;
			this.data.setCallingEvent(this);
			this.am = data.getCaster();
			this.startTime = System.currentTimeMillis();
			this.ppOff = MythicOrbitalProjectile.this.pEntityPitchOffset;
			this.pSpin = MythicOrbitalProjectile.this.pEntitySpin;
			this.pFaceDir = MythicOrbitalProjectile.this.pFaceDirection;
			this.pVOff = MythicOrbitalProjectile.this.pVOffset;
			this.pFOff = MythicOrbitalProjectile.this.pFOffset;
			this.radiusX = MythicOrbitalProjectile.this.oRadiusX;
			this.radiusZ = MythicOrbitalProjectile.this.oRadiusZ;
			this.radiusY = MythicOrbitalProjectile.this.oRadiusY;
			this.radPerSec = MythicOrbitalProjectile.this.oRadiusPerSec;
			this.radPerTick = this.radPerSec / 20f;
			this.tick = this.ppOff;
			this.target = t;
			this.centerLocation = BukkitAdapter.adapt(this.target.getLocation().add(0.0D, this.pVOff, 0.0D).clone());
			this.targetable = MythicOrbitalProjectile.this.targetable;
			this.tag = MythicOrbitalProjectile.this.tag;
			this.ct = MythicOrbitalProjectile.this.ct;
			this.tc = MythicOrbitalProjectile.this.tc;
			this.lt = MythicOrbitalProjectile.this.lifetime;
			if (this.am instanceof ActiveMob)
				this.cam = (ActiveMob) this.am;
			if (this.centerLocation == null)
				return;

			try {
				this.currentLocation = Utils.getCircleLoc(this.centerLocation, this.radiusX, this.radiusZ,
						this.radiusY, this.radPerTick * tick);
				this.pEntity = MythicOrbitalProjectile.this.mythicmobs.getAPIHelper().spawnMythicMob(customItemName,
						BukkitAdapter.adapt(this.currentLocation));
				this.pEntity.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
				if (!this.targetable)
					Volatile.handler.changeHitBox(this.pEntity,0,0,0);
					this.pEntity.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = MythicOrbitalProjectile.this.mobmanager.getMythicMobInstance(this.pEntity);
			this.pam.setOwner(this.am.getEntity().getUniqueId());
			if (this.tc) {
				this.pam.setTarget(data.getCaster().getEntity());
			} else if (this.ct) {
				if (this.cam != null && this.cam.hasThreatTable() && this.cam.getThreatTable().size() > 0) {
					this.pam.setTarget(this.cam.getThreatTable().getTopThreatHolder());
				} else if (this.cam.getEntity().getTarget() != null) {
					this.pam.setTarget(this.cam.getEntity().getTarget());
				}
			}
			if (this.tag != null) {
				this.tag = SkillString.parseMobVariables(this.tag, this.pam, this.am.getEntity().getTarget(),
						this.am.getEntity());
				this.pEntity.setMetadata(this.tag, new FixedMetadataValue(Main.getPlugin(), null));
			}
			this.taskId = TaskManager.get().scheduleTask(this, 0, 1);
			if (MythicOrbitalProjectile.this.hitPlayers || MythicOrbitalProjectile.this.hitNonPlayers) {
				this.inRange.addAll(
						MythicOrbitalProjectile.this.entitymanager.getLivingEntities(this.currentLocation.getWorld()));
				this.inRange.removeIf(e -> {
					if (e != null) {
						if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())
								||e.getBukkitEntity().hasMetadata(Utils.noTargetVar)) {
							return true;
						}
						if (!MythicOrbitalProjectile.this.hitPlayers && e.isPlayer()) {
							return true;
						}
						if (!MythicOrbitalProjectile.this.hitNonPlayers && !e.isPlayer()) {
							return true;
						}
					} else {
						return true;
					}
					return false;
				});
			}
			if (MythicOrbitalProjectile.this.onStartSkill.isPresent()
					&& MythicOrbitalProjectile.this.onStartSkill.get().isUsable(data)) {

				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				MythicOrbitalProjectile.this.onStartSkill.get().execute(sData);
			}
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public void run() {
			this.tick++;
			if ((this.radPerTick*this.tick)>360) {
				this.tick = 0;
			}
			if (this.cancelled) {
				return;
			}
			if (this.am != null && this.am.getEntity().isDead()) {
				this.stop();
				return;
			}
			if (this.lt && (this.startTime + MythicOrbitalProjectile.this.duration < System.currentTimeMillis())) {
				this.stop();
				return;
			}
			if (this.pam == null || this.pam.getEntity().isDead() || this.target == null || this.target.isDead()) {
				this.stop();
				return;
			}

			this.centerLocation = this.target.getBukkitEntity().getLocation().clone().add(0.0D, this.pVOff, 0.0D);
			this.currentLocation = Utils.getCircleLoc(this.centerLocation, this.radiusX, this.radiusZ,
					this.radiusY, this.radPerTick * tick);
			if (MythicOrbitalProjectile.this.stopOnHitGround
					&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
				this.stop();
				return;
			}
			Location eloc = this.pEntity.getLocation().clone();
			float yaw = eloc.getYaw();
			if (this.pFaceDir) {
				yaw = Utils.lookAtYaw(eloc, BukkitAdapter.adapt(this.currentLocation));
			} else if (this.pSpin != 0.0) {
				yaw = ((yaw + this.pSpin) % 360.0F);
			}
			NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY(),
					this.currentLocation.getZ(), yaw, eloc.getPitch());
			this.targets.clear();
			if (this.ct) {
				if (this.cam != null && this.cam.hasThreatTable() && this.cam.getThreatTable().size() > 0) {
					this.pam.setTarget(this.cam.getThreatTable().getTopThreatHolder());
				} else if (this.cam.getEntity().getTarget() != null) {
					this.pam.setTarget(this.cam.getEntity().getTarget());
				}
			}
			if (this.inRange != null) {
				HitBox hitBox = new HitBox(this.currentLocation, MythicOrbitalProjectile.this.hitRadius,
						MythicOrbitalProjectile.this.verticalHitRadius);
				for (AbstractEntity e : this.inRange) {
					if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0)))
						continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis() - 2000);
			}
			if (MythicOrbitalProjectile.this.onTickSkill.isPresent()
					&& MythicOrbitalProjectile.this.onTickSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				sData.setCaster(this.pam);
				sData.setTrigger(sData.getCaster().getEntity());
				AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
				HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
				targets.add(entity);
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation);
				MythicOrbitalProjectile.this.onTickSkill.get().execute(sData);
			}
			if (this.targets.size() > 0) {
				this.doHit((HashSet<AbstractEntity>) this.targets.clone());
				if (MythicOrbitalProjectile.this.stopOnHitEntity) {
					this.stop();
				}
			}
			this.targets.clear();
		}

		private void doHit(HashSet<AbstractEntity> targets) {
			if (MythicOrbitalProjectile.this.onHitSkill.isPresent()) {
				SkillMetadata sData = this.data.deepClone();
				sData.setEntityTargets(targets);
				sData.setOrigin(this.currentLocation.clone());
				if (MythicOrbitalProjectile.this.onHitSkill.get().isUsable(sData)) {
					MythicOrbitalProjectile.this.onHitSkill.get().execute(sData);
				}
			}
		}

		private void stop() {
			if (MythicOrbitalProjectile.this.onEndSkill.isPresent()
					&& MythicOrbitalProjectile.this.onEndSkill.get().isUsable(this.data)) {
				SkillMetadata sData = this.data.deepClone();
				MythicOrbitalProjectile.this.onEndSkill.get()
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
