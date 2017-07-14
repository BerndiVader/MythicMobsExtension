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
import io.lumine.xikage.mythicmobs.util.MythicUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mmcustomskills26.Main;

public class ItemProjectile
extends CustomProjectile
implements ITargetedEntitySkill,
ITargetedLocationSkill {
    
    protected String pEntityName;

    public ItemProjectile(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
        this.pEntityName = mlc.getString(new String[]{"pobject","projectileitem","pitem"},"DIRT").toUpperCase();
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
        private Item pItem;
		private Location pLocation;
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
            double velocity = 0.0;
            
            if (ItemProjectile.this.type == ProjectileType.METEOR) {
                this.startLocation = target.clone();
                this.startLocation.add(0.0, ItemProjectile.this.heightFromSurface, 0.0);
                if (ItemProjectile.this.projectileGravity <= 0.0f) {
                    this.gravity = ItemProjectile.this.projectileVelocity;
                    this.gravity = this.gravity > 0.0f ? this.gravity / ItemProjectile.this.ticksPerSecond : 0.0f;
                } else {
                    this.gravity = ItemProjectile.this.projectileGravity > 0.0f ? ItemProjectile.this.projectileGravity / ItemProjectile.this.ticksPerSecond : 0.0f;
                }
                velocity = 0.0;
            } else {
                this.startLocation = ItemProjectile.this.sourceIsOrigin ? data.getOrigin().clone() : data.getCaster().getEntity().getLocation().clone();
                velocity = ItemProjectile.this.projectileVelocity / ItemProjectile.this.ticksPerSecond;
                if (ItemProjectile.this.startYOffset != 0.0f) {
                    this.startLocation.setY(this.startLocation.getY() + (double)ItemProjectile.this.startYOffset);
                }
                if (ItemProjectile.this.startForwardOffset != 0.0f) {
                    this.startLocation = this.startLocation.add(this.startLocation.getDirection().clone().multiply(ItemProjectile.this.startForwardOffset));
                }
                if (ItemProjectile.this.startSideOffset != 0.0f) {
                    this.startLocation.setPitch(0.0f);
                    this.startLocation = MythicUtil.move(this.startLocation, 0.0, 0.0, ItemProjectile.this.startSideOffset);
                }
            }
            this.startLocation.clone();
            this.currentLocation = this.startLocation.clone();
            if (this.currentLocation == null) {
                return;
            }
            this.currentVelocity = target.toVector().subtract(this.currentLocation.toVector()).normalize();
            if (ItemProjectile.this.projectileVelocityHorizOffset != 0.0f || ItemProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                noise = 0.0f;
                if (ItemProjectile.this.projectileVelocityHorizNoise > 0.0f) {
                    noise = ItemProjectile.this.projectileVelocityHorizNoiseBase + MythicMobs.r.nextFloat() * ItemProjectile.this.projectileVelocityHorizNoise;
                }
                this.currentVelocity.rotate(ItemProjectile.this.projectileVelocityHorizOffset + noise);
            }
            if (ItemProjectile.this.startSideOffset != 0.0f) {
                // empty if block
            }
            if (ItemProjectile.this.projectileVelocityVertOffset != 0.0f || ItemProjectile.this.projectileVelocityVertNoise > 0.0f) {
                noise = 0.0f;
                if (ItemProjectile.this.projectileVelocityVertNoise > 0.0f) {
                    noise = ItemProjectile.this.projectileVelocityVertNoiseBase + MythicMobs.r.nextFloat() * ItemProjectile.this.projectileVelocityVertNoise;
                }
                this.currentVelocity.add(new AbstractVector(0.0f, ItemProjectile.this.projectileVelocityVertOffset + noise, 0.0f)).normalize();
            }
            if (ItemProjectile.this.hugSurface) {
                this.currentLocation.setY((float)((int)this.currentLocation.getY()) + ItemProjectile.this.heightFromSurface);
                this.currentVelocity.setY(0).normalize();
            }
            if (ItemProjectile.this.powerAffectsVelocity) {
                this.currentVelocity.multiply(this.power);
            }
            this.currentVelocity.multiply(velocity);
            if (ItemProjectile.this.projectileGravity > 0.0f) {
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)this.gravity);
            }
            
            this.pLocation = BukkitAdapter.adapt(currentLocation);
            
            Vector v = new Vector();
            ItemStack i = new ItemStack(Material.valueOf(customItemName));
            this.pItem = this.pLocation.getWorld().dropItem(BukkitAdapter.adapt(currentLocation), i);
            this.pItem.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
            this.pItem.setPickupDelay(Integer.MAX_VALUE);
            this.pItem.setInvulnerable(true);
            this.pItem.setGravity(false);
            this.pItem.setVelocity(v);
            
            this.taskId = TaskManager.get().scheduleTask(this, 0, ItemProjectile.this.tickInterval);
            if (ItemProjectile.this.hitPlayers || ItemProjectile.this.hitNonPlayers) {
                this.inRange.addAll(MythicMobs.inst().getEntityManager().getLivingEntities(this.currentLocation.getWorld()));
                this.inRange.removeIf(e -> {
                    if (e != null) {
                        if (e.getUniqueId().equals(this.am.getEntity().getUniqueId())) {
                            return true;
                        }
                        if (!ItemProjectile.this.hitPlayers && e.isPlayer()) {
                            return true;
                        }
                        if (!ItemProjectile.this.hitNonPlayers && !e.isPlayer()) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                    return false;
                }
                );
            }
            if (ItemProjectile.this.onStartSkill.isPresent() && ItemProjectile.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
                targets.add(this.startLocation);
                sData.setLocationTargets(targets);
                sData.setOrigin(this.currentLocation.clone());
                ItemProjectile.this.onStartSkill.get().execute(sData);
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
            if (this.cancelled) {
                return;
            }
            if (this.am != null && this.am.getEntity().isDead()) {
                this.stop();
                return;
            }
            if (this.startTime + ItemProjectile.this.duration < System.currentTimeMillis()) {
                this.stop();
                return;
            }
            this.currentLocation.clone();
            this.currentLocation.add(this.currentVelocity);
            if (ItemProjectile.this.hugSurface) {
                if (this.currentLocation.getBlockX() != this.currentX || this.currentLocation.getBlockZ() != this.currentZ) {
                    boolean ok;
                    int attempts;
                    Block b = BukkitAdapter.adapt(this.currentLocation.subtract(0.0, ItemProjectile.this.heightFromSurface, 0.0)).getBlock();
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
                    this.currentLocation.setY((float)((int)this.currentLocation.getY()) + ItemProjectile.this.heightFromSurface);
                    this.currentX = this.currentLocation.getBlockX();
                    this.currentZ = this.currentLocation.getBlockZ();
                }
            } else if (ItemProjectile.this.projectileGravity != 0.0f) {
                this.currentVelocity.setY(this.currentVelocity.getY() - (double)(ItemProjectile.this.projectileGravity / ItemProjectile.this.ticksPerSecond));
            }
            if (ItemProjectile.this.stopOnHitGround && !BlockUtil.isPathable(BukkitAdapter.adapt(this.currentLocation).getBlock())) {
                this.stop();
                return;
            }
            if (this.currentLocation.distanceSquared(this.startLocation) >= (double)ItemProjectile.this.maxDistanceSquared) {
                this.stop();
                return;
            }
            if (this.inRange != null) {
                HitBox hitBox = new HitBox(this.currentLocation, ItemProjectile.this.hitRadius, ItemProjectile.this.verticalHitRadius);
                for (AbstractEntity e : this.inRange) {
                    if (e.isDead() || !hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0))) continue;
                    this.targets.add(e);
                    this.immune.put(e, System.currentTimeMillis());
                    break;
                }
                this.immune.entrySet().removeIf(entry -> (Long)entry.getValue() < System.currentTimeMillis() - 2000);
            }
            if (ItemProjectile.this.onTickSkill.isPresent() && ItemProjectile.this.onTickSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                AbstractLocation location = this.currentLocation.clone();
                HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
                targets.add(location);
                sData.setLocationTargets(targets);
                sData.setOrigin(location);
                ItemProjectile.this.onTickSkill.get().execute(sData);
            }
            if (this.targets.size() > 0) {
                this.doHit((HashSet)this.targets.clone());
                if (ItemProjectile.this.stopOnHitEntity) {
                    this.stop();
                }
            }
            Location loc = BukkitAdapter.adapt(currentLocation);
            Location eloc = this.pItem.getLocation();
            this.pItem.setVelocity(loc.toVector().subtract(eloc.toVector()).multiply(0.5));            
            this.targets.clear();
        }

        private void doHit(HashSet<AbstractEntity> targets) {
            if (ItemProjectile.this.onHitSkill.isPresent()) {
                SkillMetadata sData = this.data.deepClone();
                sData.setEntityTargets(targets);
                sData.setOrigin(this.currentLocation.clone());
                if (ItemProjectile.this.onHitSkill.get().isUsable(sData)) {
                	ItemProjectile.this.onHitSkill.get().execute(sData);
                }
            }
        }

        private void stop() {
            if (ItemProjectile.this.onEndSkill.isPresent() && ItemProjectile.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                ItemProjectile.this.onEndSkill.get().execute(sData.setOrigin(this.currentLocation).setLocationTarget(this.currentLocation));
            }
            this.pItem.remove();
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

