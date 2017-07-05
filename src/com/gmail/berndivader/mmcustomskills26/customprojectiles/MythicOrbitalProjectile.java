package com.gmail.berndivader.mmcustomskills26.customprojectiles;

import io.lumine.xikage.mythicmobs.MythicMobs;
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
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.util.BlockUtil;
import io.lumine.xikage.mythicmobs.util.HitBox;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
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
	protected float oRadiusX;
	protected float oRadiusZ;
	protected float oRadiusY;
	protected float oRadiusPerSec;
	protected boolean targetable;

    public MythicOrbitalProjectile(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
        this.onTickSkillName = mlc.getString(new String[]{"ontickskill", "ontick", "ot", "skill", "s", "meta", "m"});
        this.onHitSkillName = mlc.getString(new String[]{"onhitskill", "onhit", "oh"});
        this.onEndSkillName = mlc.getString(new String[]{"onendskill", "onend", "oe"});
        this.onStartSkillName = mlc.getString(new String[]{"onstartskill", "onstart", "os"});
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
        this.oRadiusX = mlc.getFloat("oradx",0.0F);
        this.oRadiusZ = mlc.getFloat("oradz",0.0F);
        this.oRadiusY = mlc.getFloat("orady",0.0F);
        this.oRadiusPerSec = mlc.getFloat("oradsec",0.0F);
        this.targetable = mlc.getBoolean("targetable",false);
        
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
    	return false;
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        try {
            new ProjectileTracker(data, this.pEntityName, target);
            return true;
        }
        catch (Exception ex) {
            MythicMobs.inst().handleException(ex);
            return false;
        }
    }

    public class ProjectileTracker
    implements IParentSkill,
    Runnable {
        private SkillMetadata data;
        private boolean cancelled;
        private SkillCaster am;
        private long startTime;
        private AbstractLocation startLocation;
        private AbstractLocation currentLocation;
        private int taskId;
        private Set<AbstractEntity> inRange;
        private HashSet<AbstractEntity> targets;
        private Map<AbstractEntity, Long> immune;
        private ActiveMob pam;
        private Entity pEntity;
		private Location centerLocation;
		private float radiusX,radiusZ,radiusY,radPerSec,radPerTick;
		private float pSpin;
		private float ppOff;
		private float pFOff;
		private double pVOff;
		private boolean pFaceDir;
		private int tick;
		private AbstractEntity target;
		private UUID tuuid;
		private boolean targetable;
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ProjectileTracker(SkillMetadata data, String customItemName, AbstractEntity t) {

            this.cancelled = false;
            this.inRange = ConcurrentHashMap.newKeySet();
            this.targets = new HashSet();
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
            this.tick = 0;
            this.target = t;
            this.tuuid = t.getUniqueId();
            this.centerLocation = BukkitAdapter.adapt(this.target.getLocation());
            this.startLocation=this.target.getLocation();
            this.currentLocation = this.startLocation.clone();
            this.targetable = MythicOrbitalProjectile.this.targetable;
            if (this.currentLocation == null) {
                return;
            }
            
			try {
				this.pEntity = MythicMobs.inst().getAPIHelper().spawnMythicMob(customItemName, this.centerLocation);
	            this.pEntity.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
	            if (!this.targetable) this.pEntity.setMetadata(Main.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			} catch (InvalidMobTypeException e1) {
				e1.printStackTrace();
				return;
			}
			this.pam = MythicMobs.inst().getMobManager().getMythicMobInstance(this.pEntity);
			this.pam.setOwner(this.am.getEntity().getUniqueId());
            this.taskId = TaskManager.get().scheduleTask(this, 0, MythicOrbitalProjectile.this.tickInterval);
            if (MythicOrbitalProjectile.this.hitPlayers || MythicOrbitalProjectile.this.hitNonPlayers) {
                this.inRange.addAll(MythicMobs.inst().getEntityManager().getLivingEntities(this.currentLocation.getWorld()));
                this.inRange.removeIf(e -> {
                    if (e != null) {
                        if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())) {
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
            if (this.pam==null || this.pam.getEntity().isDead() || this.target==null || this.target.isDead()) {
            	this.stop();
            	return;
            }

            this.target = BukkitAdapter.adapt(NMSUtils.getEntity(this.target.getBukkitEntity().getWorld(), this.tuuid));
            this.centerLocation = this.target.getBukkitEntity().getLocation(); //this.data.getCaster().getEntity().getBukkitEntity().getLocation().clone();
            this.currentLocation = MythicOrbitalProjectile.getCircleLoc(this.centerLocation, this.radiusX, this.radiusZ, this.radiusY, this.radPerTick * tick);
            if (MythicOrbitalProjectile.this.stopOnHitGround && !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
                this.stop();
                return;
            }
            if (this.inRange != null) {
                HitBox hitBox = new HitBox(this.currentLocation, MythicOrbitalProjectile.this.hitRadius, MythicOrbitalProjectile.this.verticalHitRadius);
                for (AbstractEntity e : this.inRange) {
                    if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0))) continue;
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
            
            Location loc = BukkitAdapter.adapt(this.currentLocation);
        	Location eloc = this.pEntity.getLocation();
            if (this.pFaceDir) eloc = lookAt(eloc,loc);
            NMSUtils.setLocation(this.pEntity, this.currentLocation.getX(), this.currentLocation.getY()+this.pVOff, this.currentLocation.getZ(), eloc.getYaw(), eloc.getPitch());
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
    
	private static AbstractLocation getCircleLoc(Location c, double rX, double rZ, double rY, double air) {
        double x = c.getX() + rX * Math.cos(air);
        double z = c.getZ() + rZ * Math.sin(air);
        double y = c.getY() + rY * Math.cos(air);
        Location loc = new Location(c.getWorld(), x, y, z);
        Vector difference = c.toVector().clone().subtract(loc.toVector()); 
        loc.setDirection(difference);
        return BukkitAdapter.adapt(loc);
    }	

}

