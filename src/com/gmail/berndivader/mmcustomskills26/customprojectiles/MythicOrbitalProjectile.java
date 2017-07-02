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
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import io.lumine.xikage.mythicmobs.util.HitBox;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.NMS.NMSUtils;

public class MythicOrbitalProjectile
extends SkillMechanic
implements ITargetedEntitySkill,
ITargetedLocationSkill {
    protected Optional<Skill> onTickSkill = Optional.empty();
    protected Optional<Skill> onHitSkill = Optional.empty();
    protected Optional<Skill> onEndSkill = Optional.empty();
    protected Optional<Skill> onStartSkill = Optional.empty();
    protected String onTickSkillName;
    protected String onHitSkillName;
    protected String onEndSkillName;
    protected String onStartSkillName;
    protected ProjectileType type;
    protected int tickInterval;
    protected float ticksPerSecond;
    protected float hitRadius;
    protected float verticalHitRadius;
    protected float range;
    protected float maxDistanceSquared;
    protected long duration;
    protected float startYOffset;
    protected float startForwardOffset;
    protected float startSideOffset;
    protected float targetYOffset;
    protected float projectileVelocity;
    protected float projectileVelocityVertOffset;
    protected float projectileVelocityHorizOffset;
    protected float projectileGravity;
    protected float projectileVelocityAccuracy;
    protected float projectileVelocityVertNoise;
    protected float projectileVelocityHorizNoise;
    protected float projectileVelocityVertNoiseBase;
    protected float projectileVelocityHorizNoiseBase;
    protected boolean stopOnHitEntity;
    protected boolean stopOnHitGround;
    protected boolean powerAffectsVelocity = true;
    protected boolean powerAffectsRange = true;
    protected boolean hugSurface = false;
    protected boolean hitPlayers = true;
    protected boolean hitNonPlayers = false;
    protected float heightFromSurface;
    
    protected String pEntityName;
    protected float pEntitySpin;
    protected float pEntityPitchOffset;
    protected boolean pFaceDirection;
    protected double pVOffset;
    protected float pFOffset;
	protected float oRadius;
	protected float oRadiusPerSec;
	protected double oYOffset;

    public MythicOrbitalProjectile(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
        this.onTickSkillName = mlc.getString(new String[]{"ontickskill", "ontick", "ot", "skill", "s", "meta", "m"});
        this.onHitSkillName = mlc.getString(new String[]{"onhitskill", "onhit", "oh"});
        this.onEndSkillName = mlc.getString(new String[]{"onendskill", "onend", "oe"});
        this.onStartSkillName = mlc.getString(new String[]{"onstartskill", "onstart", "os"});
        String type = mlc.getString("type", "NORMAL");
        this.type = ProjectileType.valueOf(type.toUpperCase());
        this.tickInterval = mlc.getInteger(new String[]{"interval", "int", "i"}, 4);
        this.ticksPerSecond = 20.0f / (float)this.tickInterval;
        this.hitRadius = mlc.getFloat("horizontalradius", 1.25f);
        this.hitRadius = mlc.getFloat("hradius", this.hitRadius);
        this.hitRadius = mlc.getFloat("hr", this.hitRadius);
        this.hitRadius = mlc.getFloat("r", this.hitRadius);
        this.range = mlc.getFloat("maxrange", 40.0f);
        this.range = mlc.getFloat("mr", this.range);
        this.maxDistanceSquared = this.range * this.range;
        this.duration = (long)mlc.getInteger(new String[]{"maxduration","md"}, 100);
        this.duration *= 1000;
        this.verticalHitRadius = mlc.getFloat("verticalradius", this.hitRadius);
        this.verticalHitRadius = mlc.getFloat("vradius", this.verticalHitRadius);
        this.verticalHitRadius = mlc.getFloat("vr", this.verticalHitRadius);
        this.startYOffset = mlc.getFloat("startyoffset", 1.0f);
        this.startYOffset = mlc.getFloat("syo", this.startYOffset);
        this.startForwardOffset = mlc.getFloat(new String[]{"forwardoffset", "startfoffset", "sfo"}, 1.0f);
        this.startSideOffset = mlc.getFloat(new String[]{"sideoffset", "soffset", "sso"}, 0.0f);
        this.targetYOffset = mlc.getFloat(new String[]{"targetyoffset", "targety", "tyo"}, 0.0f);
        this.projectileVelocity = mlc.getFloat("velocity", 5.0f);
        this.projectileVelocity = mlc.getFloat("v", this.projectileVelocity);
        this.projectileVelocityVertOffset = mlc.getFloat("verticaloffset", 0.0f);
        this.projectileVelocityVertOffset = mlc.getFloat("vo", this.projectileVelocityVertOffset);
        this.projectileVelocityHorizOffset = mlc.getFloat("horizontaloffset", 0.0f);
        this.projectileVelocityHorizOffset = mlc.getFloat("ho", this.projectileVelocityHorizOffset);
        this.projectileGravity = mlc.getFloat("gravity", 0.0f);
        this.projectileGravity = mlc.getFloat("g", this.projectileGravity);
        this.stopOnHitEntity = mlc.getBoolean("stopatentity", true);
        this.stopOnHitEntity = mlc.getBoolean("se", this.stopOnHitEntity);
        this.stopOnHitGround = mlc.getBoolean("stopatblock", true);
        this.stopOnHitGround = mlc.getBoolean("sb", this.stopOnHitGround);
        this.powerAffectsVelocity = mlc.getBoolean("poweraffectsvelocity", true);
        this.powerAffectsVelocity = mlc.getBoolean("pav", this.powerAffectsVelocity);
        this.powerAffectsRange = mlc.getBoolean("poweraffectsrange", true);
        this.powerAffectsRange = mlc.getBoolean("par", this.powerAffectsRange);
        this.hugSurface = mlc.getBoolean("hugsurface", false);
        this.hugSurface = mlc.getBoolean("hs", this.hugSurface);
        this.heightFromSurface = mlc.getFloat("heightfromsurface", 0.5f);
        this.heightFromSurface = mlc.getFloat("hfs", this.heightFromSurface);
        this.hitPlayers = mlc.getBoolean("hitplayers", true);
        this.hitPlayers = mlc.getBoolean("hp", this.hitPlayers);
        this.hitNonPlayers = mlc.getBoolean("hitnonplayers", false);
        this.hitNonPlayers = mlc.getBoolean("hnp", this.hitNonPlayers);
        this.projectileVelocityAccuracy = mlc.getFloat(new String[]{"accuracy", "ac", "a"}, 1.0f);
        float defNoise = (1.0f - this.projectileVelocityAccuracy) * 45.0f;
        this.projectileVelocityVertNoise = mlc.getFloat(new String[]{"verticaloffset", "vn"}, defNoise) / 10.0f;
        this.projectileVelocityHorizNoise = mlc.getFloat(new String[]{"horizontaloffset", "hn"}, defNoise);
        this.projectileVelocityVertNoiseBase = 0.0f - this.projectileVelocityVertNoise / 2.0f;
        this.projectileVelocityHorizNoiseBase = 0.0f - this.projectileVelocityHorizNoise / 2.0f;
        
        this.pEntityName = mlc.getString(new String[]{"pobject","projectilemythic","pmythic"},"MINECART");
        this.pEntitySpin = mlc.getFloat("pspin",0.0F);
        this.pEntityPitchOffset = mlc.getFloat("ppOff",360.0f);
        this.pFaceDirection = mlc.getBoolean("pfacedir",false);
        this.pVOffset = mlc.getDouble("pvoff",0.0D);
        this.pFOffset = mlc.getFloat("pfoff",0.0F);
        this.oRadius = mlc.getFloat("orad",1.5F);
        this.oRadiusPerSec = mlc.getFloat("oradsec",1.5F);
        this.oYOffset = mlc.getDouble("oyoff",1D);
        
        if (this.onTickSkillName != null) {
            this.onTickSkill = MythicMobs.inst().getSkillManager().getSkill(this.onTickSkillName);
        }
        if (this.onHitSkillName != null) {
            this.onHitSkill = MythicMobs.inst().getSkillManager().getSkill(this.onHitSkillName);
        }
        if (this.onEndSkillName != null) {
            this.onEndSkill = MythicMobs.inst().getSkillManager().getSkill(this.onEndSkillName);
        }
        if (this.onStartSkillName != null) {
            this.onStartSkill = MythicMobs.inst().getSkillManager().getSkill(this.onStartSkillName);
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
		private Location pLocation, centerLocation;
		private float radius, radPerSec, radPerTick;
		private float pSpin;
		private float ppOff;
		private float pFOff;
		private double pVOff, oYOff;
		private boolean pFaceDir;
		private int tick;
		
        @SuppressWarnings({ "unchecked", "rawtypes"})
		public ProjectileTracker(SkillMetadata data, String customItemName, AbstractLocation target) {

            float noise;
            this.cancelled = false;
            this.gravity = 0.0f;
            this.inRange = ConcurrentHashMap.newKeySet();
            this.targets = new HashSet();
            this.immune = new HashMap<AbstractEntity, Long>();
            this.cancelled = false;
            this.data = data;
            this.data.setCallingEvent(this);
            this.am = data.getCaster();
            this.power = data.getPower();
            this.startTime = System.currentTimeMillis();
            this.ppOff = MythicOrbitalProjectile.this.pEntityPitchOffset;
            this.pSpin = MythicOrbitalProjectile.this.pEntitySpin;
            this.pFaceDir = MythicOrbitalProjectile.this.pFaceDirection;
            this.pVOff = MythicOrbitalProjectile.this.pVOffset;
            this.pFOff = MythicOrbitalProjectile.this.pFOffset;
            this.radius = MythicOrbitalProjectile.this.oRadius;
            this.radPerSec = MythicOrbitalProjectile.this.oRadiusPerSec;
            this.radPerTick = this.radPerSec / 20f;
            this.oYOff = MythicOrbitalProjectile.this.oYOffset;
            this.tick = 0;
            this.centerLocation = BukkitAdapter.adapt(data.getCaster().getLocation());
            double velocity = 0.0;
            
            if (MythicOrbitalProjectile.this.type == ProjectileType.METEOR) {
                this.startLocation = target.clone();
                this.startLocation.add(0.0, MythicOrbitalProjectile.this.heightFromSurface, 0.0);
                if (MythicOrbitalProjectile.this.projectileGravity <= 0.0f) {
                    this.gravity = MythicOrbitalProjectile.this.projectileVelocity;
                    this.gravity = this.gravity > 0.0f ? this.gravity / MythicOrbitalProjectile.this.ticksPerSecond : 0.0f;
                } else {
                    this.gravity = MythicOrbitalProjectile.this.projectileGravity > 0.0f ? MythicOrbitalProjectile.this.projectileGravity / MythicOrbitalProjectile.this.ticksPerSecond : 0.0f;
                }
                velocity = 0.0;
            } else {
                this.startLocation = MythicOrbitalProjectile.this.sourceIsOrigin ? data.getOrigin().clone() : data.getCaster().getEntity().getLocation().clone();
                velocity = MythicOrbitalProjectile.this.projectileVelocity / MythicOrbitalProjectile.this.ticksPerSecond;
                if (MythicOrbitalProjectile.this.startYOffset != 0.0f) {
                    this.startLocation.setY(this.startLocation.getY() + (double)MythicOrbitalProjectile.this.startYOffset);
                }
                if (MythicOrbitalProjectile.this.startForwardOffset != 0.0f) {
                    this.startLocation = this.startLocation.add(this.startLocation.getDirection().clone().multiply(MythicOrbitalProjectile.this.startForwardOffset));
                }
                if (MythicOrbitalProjectile.this.startSideOffset != 0.0f) {
                    this.startLocation.setPitch(0.0f);
                    this.startLocation = MythicUtil.move(this.startLocation, 0.0, 0.0, MythicOrbitalProjectile.this.startSideOffset);
                }
            }
            this.startLocation.clone();
            this.currentLocation = this.startLocation.clone();
            if (this.currentLocation == null) {
                return;
            }
            this.currentVelocity = target.toVector().subtract(this.currentLocation.toVector()).normalize();
            if (MythicOrbitalProjectile.this.projectileVelocityHorizOffset != 0.0f || MythicOrbitalProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                noise = 0.0f;
                if (MythicOrbitalProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                    noise = MythicOrbitalProjectile.this.projectileVelocityHorizNoiseBase + MythicMobs.r.nextFloat() * MythicOrbitalProjectile.this.projectileVelocityHorizNoise;
                }
                this.currentVelocity.rotate(MythicOrbitalProjectile.this.projectileVelocityHorizOffset + noise);
            }
            if (MythicOrbitalProjectile.this.startSideOffset != 0.0f) {
                // empty if block
            }
            if (MythicOrbitalProjectile.this.projectileVelocityVertOffset != 0.0f || MythicOrbitalProjectile.this.projectileVelocityVertNoise > 0.0f) {
                noise = 0.0f;
                if (MythicOrbitalProjectile.this.projectileVelocityVertNoise > 0.0f) {
                    noise = MythicOrbitalProjectile.this.projectileVelocityVertNoiseBase + MythicMobs.r.nextFloat() * MythicOrbitalProjectile.this.projectileVelocityVertNoise;
                }
                this.currentVelocity.add(new AbstractVector(0.0f, MythicOrbitalProjectile.this.projectileVelocityVertOffset + noise, 0.0f)).normalize();
            }
            if (MythicOrbitalProjectile.this.hugSurface) {
                this.currentLocation.setY((float)((int)this.currentLocation.getY()) + MythicOrbitalProjectile.this.heightFromSurface);
                this.currentVelocity.setY(0).normalize();
            }
            if (MythicOrbitalProjectile.this.powerAffectsVelocity) {
                this.currentVelocity.multiply(this.power);
            }
            this.currentVelocity.multiply(velocity);
            if (MythicOrbitalProjectile.this.projectileGravity > 0.0f) {
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)this.gravity);
            }
            
            this.pLocation = BukkitAdapter.adapt(currentLocation);
            Vector v = new Vector();
			try {
				this.pEntity = MythicMobs.inst().getAPIHelper().spawnMythicMob(customItemName, this.pLocation.add(0,this.pVOff,0));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = MythicMobs.inst().getMobManager().getMythicMobInstance(this.pEntity);
            new BukkitRunnable() {
				@Override
				public void run() {
		            NMSUtils.setInvulnerable(pEntity, true);
		            NMSUtils.setSilent(pEntity,true);
		            pEntity.setGravity(false);
		            pEntity.setVelocity(v);
				}
			}.runTaskLater(Main.getPlugin(), 5L);
            
            MythicMobs.debug(3, "------ Initializing projectile skill");
            this.taskId = TaskManager.get().scheduleTask(this, 0, MythicOrbitalProjectile.this.tickInterval);
            if (MythicOrbitalProjectile.this.hitPlayers || MythicOrbitalProjectile.this.hitNonPlayers) {
                this.inRange.addAll(MythicMobs.inst().getEntityManager().getLivingEntities(this.currentLocation.getWorld()));
                this.inRange.removeIf(e -> {
                    if (e != null) {
                        MythicMobs.debug(4, "-------- Added entity " + e.getName());
                        if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())) {
                            MythicMobs.debug(4, "-------- Removed entity " + e.getName() + ": is self");
                            return true;
                        }
                        if (!MythicOrbitalProjectile.this.hitPlayers && e.isPlayer()) {
                            MythicMobs.debug(4, "-------- Removed entity " + e.getName() + ": is player");
                            return true;
                        }
                        if (!MythicOrbitalProjectile.this.hitNonPlayers && !e.isPlayer()) {
                            MythicMobs.debug(4, "-------- Removed entity " + e.getName() + ": is non-player");
                            return true;
                        }
                    } else {
                        return true;
                    }
                    return false;
                }
                );
            }
            if (MythicOrbitalProjectile.this.onStartSkill.isPresent() && MythicOrbitalProjectile.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
                targets.add(this.startLocation);
                sData.setLocationTargets(targets);
                sData.setOrigin(this.currentLocation.clone());
                MythicOrbitalProjectile.this.onStartSkill.get().execute(sData);
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

        @SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
        public void run() {
        	this.tick++;
            if (this.cancelled) {
                return;
            }
            if (this.am != null && this.am.getEntity().isDead()) {
                this.stop();
                return;
            }
            if (this.startTime + MythicOrbitalProjectile.this.duration < System.currentTimeMillis()) {
                this.stop();
                return;
            }
            this.centerLocation = this.data.getCaster().getEntity().getBukkitEntity().getLocation();
            this.centerLocation.setY(this.centerLocation.getY()+this.oYOff);
            this.currentLocation = MythicOrbitalProjectile.getCircleLoc(this.centerLocation, this.radius, this.radPerTick * tick);
            if (MythicOrbitalProjectile.this.hugSurface) {
                if (this.currentLocation.getBlockX() != this.currentX || this.currentLocation.getBlockZ() != this.currentZ) {
                    boolean ok;
                    int attempts;
                    Block b = BukkitAdapter.adapt(this.currentLocation.subtract(0.0, MythicOrbitalProjectile.this.heightFromSurface, 0.0)).getBlock();
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
                    this.currentLocation.setY((float)((int)this.currentLocation.getY()) + MythicOrbitalProjectile.this.heightFromSurface);
                    this.currentX = this.currentLocation.getBlockX();
                    this.currentZ = this.currentLocation.getBlockZ();
                }
            } else if (MythicOrbitalProjectile.this.projectileGravity != 0.0f) {
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)(MythicOrbitalProjectile.this.projectileGravity / MythicOrbitalProjectile.this.ticksPerSecond));
            }
            if (MythicOrbitalProjectile.this.stopOnHitGround && !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
                this.stop();
                return;
            }
            if (this.currentLocation.distanceSquared(this.startLocation) >= (double)MythicOrbitalProjectile.this.maxDistanceSquared) {
                this.stop();
                return;
            }
            if (this.inRange != null) {
                MythicMobs.debug(4, "-------- Checking if entities in HitBox");
                HitBox hitBox = new HitBox(this.currentLocation, MythicOrbitalProjectile.this.hitRadius, MythicOrbitalProjectile.this.verticalHitRadius);
                for (AbstractEntity e : this.inRange) {
                    if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0))) continue;
                    MythicMobs.debug(4, "---------- Target " + e.getName() + " is in HitBox!");
                    this.targets.add(e);
                    this.immune.put(e, System.currentTimeMillis());
                    break;
                }
                this.immune.entrySet().removeIf(entry -> (Long)entry.getValue() < System.currentTimeMillis() - 2000);
            }
            if (MythicOrbitalProjectile.this.onTickSkill.isPresent() && MythicOrbitalProjectile.this.onTickSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                AbstractEntity entity = BukkitAdapter.adapt(this.pEntity);
                HashSet<AbstractEntity> targets = new HashSet<AbstractEntity>();
                targets.add(entity);
                sData.setEntityTargets(targets);
                sData.setOrigin(entity.getLocation());
                
                MythicOrbitalProjectile.this.onTickSkill.get().execute(sData);
            }
            if (this.targets.size() > 0) {
                this.doHit((HashSet)this.targets.clone());
                if (MythicOrbitalProjectile.this.stopOnHitEntity) {
                    this.stop();
                }
            }
            
            Location loc = BukkitAdapter.adapt(currentLocation);
        	Location eloc = this.pEntity.getLocation();
            if (this.pFaceDir) {
                eloc = lookAt(eloc,loc);
            }
            
            NMSUtils.setLocation(this.pEntity, loc.getX(), loc.getY()+this.pVOff, loc.getZ(), eloc.getYaw(), eloc.getPitch());
            if (this.pSpin!=0.0) {
                float yaw = eloc.getYaw();
                yaw = ((yaw + this.pSpin) % 360.0F);
                NMSUtils.setYawPitch(this.pEntity, yaw, eloc.getPitch());
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
            if (MythicOrbitalProjectile.this.onEndSkill.isPresent() && MythicOrbitalProjectile.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                MythicOrbitalProjectile.this.onEndSkill.get().execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
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
    
	private static Location lookAt(Location loc, Location lookat) {
        loc = loc.clone();
        lookat = lookat.clone();
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();
 
        if (dx != 0) {
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
        loc.setPitch((float) -Math.atan(dy / dxz));
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
        return loc;
    }	
    
	private static AbstractLocation getCircleLoc(Location c, double r, double air) {
        double x = c.getX() + r * Math.cos(air);
        double z = c.getZ() + r * Math.sin(air);
        double y = c.getY();
        Location loc = new Location(c.getWorld(), x, y, z);
        Vector difference = c.toVector().clone().subtract(loc.toVector()); 
        loc.setDirection(difference);
        return BukkitAdapter.adapt(loc);
    }	

    protected static enum ProjectileType {
        NORMAL,
        METEOR;
        private ProjectileType() {
        }
    }

}

