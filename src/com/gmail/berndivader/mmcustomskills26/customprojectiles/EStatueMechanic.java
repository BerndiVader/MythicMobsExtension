package com.gmail.berndivader.mmcustomskills26.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EStatueMechanic extends SkillMechanic
implements 
ITargetedEntitySkill,
ITargetedLocationSkill {
	protected MythicMobs mythicmobs;
	protected SkillManager skillmanager;
	
    protected Optional<Skill>onTickSkill=Optional.empty(),
    		onHitSkill=Optional.empty(),
    		onEndSkill=Optional.empty(),
    		onStartSkill=Optional.empty();
    protected EntityType material;
    protected String onTickSkillName,
    		onHitSkillName,
    		onEndSkillName,
    		onStartSkillName;
    protected int tickInterval,duration;
    protected float ticksPerSecond,
    		hitRadius,
    		verticalHitRadius,
    		YOffset;
    protected double sOffset,
    		fOffset;
    protected boolean 
    		hitTarget=true,
    		hitPlayers=false,
    		hitNonPlayers=false,
    		hitTargetOnly=false,
    		noAI=true;

    public EStatueMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
		this.mythicmobs=Main.getPlugin().getMythicMobs();
		this.skillmanager=this.mythicmobs.getSkillManager();
		String i=mlc.getString(new String[] {"entity","e"},"pig").toUpperCase();
		try {
			this.material=EntityType.valueOf(i);
		} catch (Exception e){
			this.material=EntityType.PLAYER;
		}
        this.onTickSkillName=mlc.getString(new String[]{"ontickskill","ontick","ot","skill","s","meta","m"});
        this.onHitSkillName=mlc.getString(new String[]{"onhitskill","onhit","oh"});
        this.onEndSkillName=mlc.getString(new String[]{"onendskill","onend","oe"});
        this.onStartSkillName=mlc.getString(new String[]{"onstartskill","onstart","os"});
        this.tickInterval=mlc.getInteger(new String[]{"interval","int","i"},4);
        this.ticksPerSecond=20.0f/(float)this.tickInterval;
        this.hitRadius=mlc.getFloat(new String[] {"horizontalradius","hradius","hr","r"},1.25f);
        this.duration=mlc.getInteger(new String[] {"maxduration","md"},10);
        this.verticalHitRadius = mlc.getFloat(new String[] {"verticalradius","vradius","vr"}, this.hitRadius);
        this.YOffset = mlc.getFloat(new String[] {"yoffset","yo"}, 1.0f);
        this.sOffset=mlc.getDouble(new String[] {"soffset","so"},0d);
        this.fOffset=mlc.getDouble(new String[] {"foffset","fo"},0d);
        this.hitPlayers = mlc.getBoolean(new String[] {"hitplayers","hp"}, false);
        this.hitNonPlayers = mlc.getBoolean(new String[] {"hitnonplayers","hnp"}, false);
        this.hitTarget = mlc.getBoolean(new String[] {"hittarget","ht"}, true);
        this.hitTargetOnly = mlc.getBoolean("hittargetonly", false);
        this.noAI=mlc.getBoolean("noai",true);
		if (this.onTickSkillName != null) {
			this.onTickSkill = skillmanager.getSkill(this.onTickSkillName);
		}
		if (this.onHitSkillName != null) {
			this.onHitSkill = skillmanager.getSkill(this.onHitSkillName);
		}
		if (this.onEndSkillName != null) {
			this.onEndSkill = skillmanager.getSkill(this.onEndSkillName);
		}
		if (this.onStartSkillName != null) {
			this.onStartSkill = skillmanager.getSkill(this.onStartSkillName);
		}
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        try {
            new StatueTracker(data, target);
            return true;
        }
        catch (Exception ex) {
        	System.err.println(ex.getMessage());
            return false;
        }
    }
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
        try {
            new StatueTracker(data, target);
            return true;
        }
        catch (Exception ex) {
        	System.err.println(ex.getMessage());
            return false;
        }
	}

    private class StatueTracker
    implements IParentSkill,
    Runnable {
    	private VolatileHandler vh;
        private boolean cancelled,useOffset,iYaw,islocationtarget;
        private SkillMetadata data;
        private Entity entity;
        private SkillCaster caster;
        private Entity owner;
        private Location currentLocation,oldLocation;
        private int taskId;
        private HashSet<LivingEntity> targets;
        private List<LivingEntity> inRange;
        private Map<LivingEntity,Long> immune;
        private double sOffset,fOffset,yOffset;
        private int count,dur;
        
        public StatueTracker(SkillMetadata data, AbstractEntity target) {
        	this(data,target,null);
        }
        public StatueTracker(SkillMetadata data, AbstractLocation target) {
        	this(data,null,target);
        }

        private StatueTracker(SkillMetadata data, AbstractEntity target, AbstractLocation location) {
        	this.vh=Main.getPlugin().getVolatileHandler();
        	this.islocationtarget=target==null&&location!=null;
            this.cancelled = false;
            this.data = data;
            this.data.setCallingEvent(this);
            this.caster = data.getCaster();
            this.owner=this.caster.getEntity().getBukkitEntity();
            this.currentLocation=this.caster.getEntity().getBukkitEntity().getLocation();
            this.yOffset=EStatueMechanic.this.YOffset;
            this.sOffset=EStatueMechanic.this.sOffset;
            this.fOffset=EStatueMechanic.this.fOffset;
            this.count=0;
            if (EStatueMechanic.this.YOffset != 0.0f) {
                this.currentLocation.setY(this.currentLocation.getY()+this.yOffset);
            }
    		this.useOffset=EStatueMechanic.this.fOffset!=0d||EStatueMechanic.this.sOffset!=0d;
    		if (this.useOffset) {
    			Vector soV=CustomSkillStuff.getSideOffsetVector(this.currentLocation.getYaw(), this.sOffset, this.iYaw);
    			Vector foV=CustomSkillStuff.getFrontBackOffsetVector(this.currentLocation.getDirection(),this.fOffset);
    			this.currentLocation.add(soV);
    			this.currentLocation.add(foV);
    		}
            if (EStatueMechanic.this.hitPlayers
            		||EStatueMechanic.this.hitNonPlayers
            		||EStatueMechanic.this.hitTarget) {
                this.inRange = this.currentLocation.getWorld().getLivingEntities();
                Iterator<LivingEntity> iter = this.inRange.iterator();
                while (iter.hasNext()) {
                    LivingEntity e = iter.next();
                    if (e.getUniqueId().equals(this.caster.getEntity().getUniqueId())
                    		||e.hasMetadata(Main.noTargetVar)) {
                        iter.remove();
                        continue;
                    }
                    if (!EStatueMechanic.this.hitPlayers && (e instanceof Player) && !e.equals(target)) {
                        iter.remove();
                        continue;
                    }
                    if (EStatueMechanic.this.hitNonPlayers || (e instanceof Player) && !e.equals(target)) continue;
                    iter.remove();
                }
                if (EStatueMechanic.this.hitTarget&&target!=null) {
                    this.inRange.add((LivingEntity)target.getBukkitEntity());
                }
            }
            this.targets=new HashSet<LivingEntity>();
            this.immune=new HashMap<LivingEntity,Long>();
            this.entity=this.currentLocation.getWorld().spawnEntity(this.currentLocation,EStatueMechanic.this.material);
            Main.entityCache.add(this.entity);
			this.entity.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.entity.setMetadata(Main.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.entity.setInvulnerable(true);
			this.entity.setGravity(false);
			this.entity.setTicksLived(Integer.MAX_VALUE);
			if (this.entity instanceof LivingEntity) ((LivingEntity)this.entity).setAI(!EStatueMechanic.this.noAI);
			vh.teleportEntityPacket(this.entity);
			vh.changeHitBox((Entity)this.entity,0,0,0);
            if (EStatueMechanic.this.onStartSkill.isPresent()
            		&&EStatueMechanic.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                EStatueMechanic.this.onStartSkill.get().execute(sData);
            }
            this.taskId=TaskManager.get().scheduleTask(this, 0, 1);
        }

        @Override
        public void run() {
            if (this.cancelled) {
                return;
            }
            if(this.count>EStatueMechanic.this.duration) {
                this.stop();
                return;
            }
            if (this.entity==null) {
            	this.stop();
            	return;
            }
            this.oldLocation=this.currentLocation.clone();
            if (!this.islocationtarget) {
            	this.currentLocation=this.owner.getLocation().add(0d,this.yOffset,0d);
            } else {
            	this.currentLocation=this.entity.getLocation();
            }
            if (this.dur>EStatueMechanic.this.tickInterval
            		&&this.inRange != null) {
                HitBox hitBox = new HitBox(this.currentLocation,EStatueMechanic.this.hitRadius,EStatueMechanic.this.verticalHitRadius);
                for (int i=0;i<this.inRange.size();i++) {
                    LivingEntity e=this.inRange.get(i);
					if (e.isDead()||!hitBox.contains(e.getLocation().add(0.0, 0.6, 0.0))) continue;
					this.targets.add(e);
					this.immune.put(e, System.currentTimeMillis());
					break;
				}
				this.immune.entrySet().removeIf(entry->entry.getValue()<System.currentTimeMillis()-2000);
                Iterator<Map.Entry<LivingEntity,Long>> iter = this.immune.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<LivingEntity,Long> entry = iter.next();
                    if (entry.getValue()>=System.currentTimeMillis()-2000) continue;
                    iter.remove();
                    this.inRange.add(entry.getKey());
                }
                if (this.targets.size() > 0) {
                    this.doHit((HashSet)this.targets.clone());
                }
                this.targets.clear();
            	if (EStatueMechanic.this.onTickSkill.isPresent()
            			&&EStatueMechanic.this.onTickSkill.get().isUsable(this.data)) {
            		SkillMetadata sData = this.data.deepClone();
            		AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
            		HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
            		targets.add(location);
            		sData.setLocationTargets(targets);
            		sData.setOrigin(location);
            		EStatueMechanic.this.onTickSkill.get().execute(sData);
            	}
            	this.dur=0;
            }
            
            if (!this.islocationtarget) {
            	double x = this.currentLocation.getX();
            	double z = this.currentLocation.getZ();
            	Vector soV=CustomSkillStuff.getSideOffsetVector(this.owner.getLocation().getYaw(), this.sOffset, this.iYaw);
            	Vector foV=CustomSkillStuff.getFrontBackOffsetVector(this.owner.getLocation().getDirection(),this.fOffset);
            	x+=soV.getX()+foV.getX();
            	z+=soV.getZ()+foV.getZ();
            	this.currentLocation.setX(x);
            	this.currentLocation.setZ(z);
            	NMSUtils.setLocation(this.entity,x,this.currentLocation.getY(),z,this.currentLocation.getYaw(), this.currentLocation.getPitch());
            } else {
            	NMSUtils.setLocation(this.entity,this.currentLocation.getX(),this.currentLocation.getY(),this.currentLocation.getZ(),this.currentLocation.getYaw(), this.currentLocation.getPitch());
            }
			this.count++;
			this.dur++;
        }

        public void doHit(HashSet<AbstractEntity> targets) {
            if (EStatueMechanic.this.onHitSkill.isPresent() && EStatueMechanic.this.onHitSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                sData.setEntityTargets(targets);
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                EStatueMechanic.this.onHitSkill.get().execute(sData);
            }
        }

        public void stop() {
            if (EStatueMechanic.this.onEndSkill.isPresent() && EStatueMechanic.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                EStatueMechanic.this.onEndSkill.get().execute(sData.setOrigin(BukkitAdapter.adapt(this.currentLocation))
                		.setLocationTarget(BukkitAdapter.adapt(this.currentLocation)));
            }
            TaskManager.get().cancelTask(this.taskId);
            this.entity.remove();
            this.cancelled = true;
            if (this.inRange != null) {
                this.inRange.clear();
            }
        }

        @Override
        public void setCancelled() {
        }

        @Override
        public boolean getCancelled() {
            return false;
        }
    }
}
