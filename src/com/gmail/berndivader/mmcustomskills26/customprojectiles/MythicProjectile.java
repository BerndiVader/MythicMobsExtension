package com.gmail.berndivader.mmcustomskills26.customprojectiles;

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
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import io.lumine.xikage.mythicmobs.util.MythicUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.NMS.NMSUtils;

public class MythicProjectile
extends CustomProjectile
implements ITargetedEntitySkill,
ITargetedLocationSkill {
    protected Optional<Skill> onBounceSkill = Optional.empty();
    protected String onBounceSkillName;
    protected String pEntityName;
    protected float pEntitySpin;
    protected float pEntityPitchOffset;

    public MythicProjectile(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.pEntityName = mlc.getString(new String[]{"pobject","projectilemythic","pmythic"},"MINECART");
        this.pEntitySpin = mlc.getFloat("pspin",0.0F);
        this.pEntityPitchOffset = mlc.getFloat("ppOff",360.0f);
        this.onBounceSkillName = mlc.getString(new String[]{"onbounceskill", "onbounce", "ob"});
        if (this.onBounceSkillName != null) {
            this.onBounceSkill = MythicMobs.inst().getSkillManager().getSkill(this.onBounceSkillName);
        }
        
    }

    @Override
    public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
        try {
            new ProjectileTracker(data, this.pEntityName, target.clone().add(0.0, this.targetYOffset, 0.0));
            return true;
        }
        catch (Exception ex) {
            MythicMobs.inst().handleException(ex);
            return false;
        }
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        return this.castAtLocation(data, target.getLocation().add(0.0, target.getEyeHeight() / 2.0, 0.0));
    }

    public class ProjectileTracker
    implements IParentSkill,
    Runnable {
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
		private boolean pFaceDir,targetable,eyedir;
		private float currentBounce,bounceReduce;
		
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
                    this.gravity = MythicProjectile.this.projectileGravity > 0.0f ? MythicProjectile.this.projectileGravity / MythicProjectile.this.ticksPerSecond : 0.0f;
                }
                velocity = 0.0;
            } else {
                this.startLocation = MythicProjectile.this.sourceIsOrigin ? data.getOrigin().clone() : data.getCaster().getEntity().getLocation().clone();
                velocity = MythicProjectile.this.projectileVelocity / MythicProjectile.this.ticksPerSecond;
                if (MythicProjectile.this.startYOffset != 0.0f) {
                    this.startLocation.setY(this.startLocation.getY() + (double)MythicProjectile.this.startYOffset);
                }
                if (MythicProjectile.this.startForwardOffset != 0.0f) {
                    this.startLocation = this.startLocation.add(this.startLocation.getDirection().clone().multiply(MythicProjectile.this.startForwardOffset));
                }
                if (MythicProjectile.this.startSideOffset != 0.0f) {
                    this.startLocation.setPitch(0.0f);
                    this.startLocation = MythicUtil.move(this.startLocation, 0.0, 0.0, MythicProjectile.this.startSideOffset);
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
            	AbstractLocation al = BukkitAdapter.adapt(this.am.getEntity().getEyeLocation());
            	this.currentVelocity = al.getDirection().normalize();
            }
            if (MythicProjectile.this.projectileVelocityHorizOffset != 0.0f || MythicProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                noise = 0.0f;
                if (MythicProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                    noise = MythicProjectile.this.projectileVelocityHorizNoiseBase + MythicMobs.r.nextFloat() * MythicProjectile.this.projectileVelocityHorizNoise;
                }
                this.currentVelocity.rotate(MythicProjectile.this.projectileVelocityHorizOffset + noise);
            }
            if (MythicProjectile.this.startSideOffset != 0.0f) {
                // empty if block
            }
            if (MythicProjectile.this.projectileVelocityVertOffset != 0.0f || MythicProjectile.this.projectileVelocityVertNoise > 0.0f) {
                noise = 0.0f;
                if (MythicProjectile.this.projectileVelocityVertNoise > 0.0f) {
                    noise = MythicProjectile.this.projectileVelocityVertNoiseBase + MythicMobs.r.nextFloat() * MythicProjectile.this.projectileVelocityVertNoise;
                }
                this.currentVelocity.add(new AbstractVector(0.0f, MythicProjectile.this.projectileVelocityVertOffset + noise, 0.0f)).normalize();
            }
            if (MythicProjectile.this.hugSurface) {
                this.currentLocation.setY((float)((int)this.currentLocation.getY()) + MythicProjectile.this.heightFromSurface);
                this.currentVelocity.setY(0).normalize();
            }
            if (MythicProjectile.this.powerAffectsVelocity) {
                this.currentVelocity.multiply(this.power);
            }
            this.currentVelocity.multiply(velocity);
            if (MythicProjectile.this.projectileGravity > 0.0f) {
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)this.gravity);
            }
            
            this.pLocation = BukkitAdapter.adapt(this.startLocation.clone());
            float yaw = this.pLocation.getYaw();
            if (this.pFaceDir && !this.eyedir) {
            	yaw = CustomSkillStuff.lookAtYaw(this.pLocation, BukkitAdapter.adapt(target));
            	this.pLocation.setYaw(yaw);
            }
            this.pLocation.add(this.pLocation.getDirection().clone().multiply(this.pFOff));
			try {
				this.pEntity = MythicMobs.inst().getAPIHelper().spawnMythicMob(customItemName, this.pLocation.add(0.0D, this.pVOff, 0.0D));
	            this.pEntity.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
	            if (!this.targetable) this.pEntity.setMetadata(Main.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = MythicMobs.inst().getMobManager().getMythicMobInstance(this.pEntity);
            this.taskId = TaskManager.get().scheduleTask(this, 0, MythicProjectile.this.tickInterval);
            if (MythicProjectile.this.hitPlayers || MythicProjectile.this.hitNonPlayers) {
                this.inRange.addAll(MythicMobs.inst().getEntityManager().getLivingEntities(this.currentLocation.getWorld()));
                this.inRange.removeIf(e -> {
                    if (e != null) {
                    	ActiveMob eam = null;
                        if (e.getUniqueId().equals(this.am.getEntity().getUniqueId()) || e.getBukkitEntity().hasMetadata(Main.noTargetVar)) {
                            return true;
                        }
                        if (!MythicProjectile.this.hitPlayers && e.isPlayer()) {
                            return true;
                        }
                        if (!MythicProjectile.this.hitNonPlayers && !e.isPlayer()) {
                            return true;
                        }
                    	if (MythicMobs.inst().getMobManager().isActiveMob(e)) {
                        	eam = MythicMobs.inst().getMobManager().getMythicMobInstance(e);
                        	if (eam.getOwner().isPresent() && eam.getOwner().get().equals(this.am.getEntity().getUniqueId())) {
                        		return true;
                        	}
                    	}
                    } else {
                        return true;
                    } 
                    return false;
                }
                );
            }
            if (MythicProjectile.this.onStartSkill.isPresent() && MythicProjectile.this.onStartSkill.get().isUsable(data)) {
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
                if (this.currentLocation.getBlockX() != this.currentX || this.currentLocation.getBlockZ() != this.currentZ) {
                    boolean ok;
                    int attempts;
                    Block b = BukkitAdapter.adapt(this.currentLocation.subtract(0.0, MythicProjectile.this.heightFromSurface, 0.0)).getBlock();
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
                            if (!BlockUtil.isPathable(b)) continue;
                            ok = true;
                            break;
                        }
                        if (!ok) {
                            this.stop();
                            return;
                        }
                    }
                    this.currentLocation.setY((float)((int)this.currentLocation.getY()) + MythicProjectile.this.heightFromSurface);
                    this.currentX = this.currentLocation.getBlockX();
                    this.currentZ = this.currentLocation.getBlockZ();
                }
            } else if (MythicProjectile.this.projectileGravity != 0.0f) {
           		if (MythicProjectile.this.bounce 
           				&& !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
           			if (this.currentBounce<0.0F) {
                        this.stop();
                        return;
           			}
           			this.currentBounce-=this.bounceReduce;
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
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)(MythicProjectile.this.projectileGravity / MythicProjectile.this.ticksPerSecond));
            }
            if (MythicProjectile.this.stopOnHitGround && !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
                this.stop();
                return;
            }
            if (this.currentLocation.distanceSquared(this.startLocation) >= (double)MythicProjectile.this.maxDistanceSquared) {
                this.stop();
                return;
            }
            if (this.inRange != null) {
                HitBox hitBox = new HitBox(this.pam.getLocation(), MythicProjectile.this.hitRadius, MythicProjectile.this.verticalHitRadius);
                for (AbstractEntity e : this.inRange) {
                    if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0))) continue;
                    this.targets.add(e);
                    this.immune.put(e, System.currentTimeMillis());
                    break;
                }
                this.immune.entrySet().removeIf(entry -> (Long)entry.getValue() < System.currentTimeMillis() - 2000);
            }
            if (MythicProjectile.this.onTickSkill.isPresent() && MythicProjectile.this.onTickSkill.get().isUsable(this.data)) {
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
                this.doHit((HashSet<AbstractEntity>)this.targets.clone());
                if (MythicProjectile.this.stopOnHitEntity) {
                    this.stop();
                }
            }
        	Location eloc = this.pEntity.getLocation();
            float yaw = eloc.getYaw();
            if (this.pSpin!=0.0) {
                yaw = ((yaw + this.pSpin) % 360.0F);
            }
            NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY()+this.pVOff, this.currentLocation.getZ(), yaw, eloc.getPitch());
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
            if (MythicProjectile.this.onEndSkill.isPresent() && MythicProjectile.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                MythicProjectile.this.onEndSkill.get().execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
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

