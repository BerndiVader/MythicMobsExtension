package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class MStatueMechanic extends SkillMechanic
implements 
ITargetedEntitySkill,
ITargetedLocationSkill {
    Optional<Skill>onTickSkill=Optional.empty(),
    		onHitSkill=Optional.empty(),
    		onEndSkill=Optional.empty(),
    		onStartSkill=Optional.empty();
    MythicMob material;
    String onTickSkillName,
    		onHitSkillName,
    		onEndSkillName,
    		onStartSkillName;
    int tickInterval,duration;
    float ticksPerSecond,
    		hitRadius,
    		verticalHitRadius,
    		YOffset;
    double sOffset,
    		fOffset;
    boolean facedir,
    		hitTarget,
    		hitPlayers,
    		hitNonPlayers,
    		hitTargetOnly;

    public MStatueMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
		String i=mlc.getString(new String[] {"mob","m"},"SkeletonKing");
		this.material=Utils.mythicmobs.getMobManager().getMythicMob(i);
		if (this.material==null&&Utils.mythicmobs.getMobManager().getMobTypes().size()>0) {
			this.material=Utils.mythicmobs.getMobManager().getMobTypes().iterator().next();
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
        this.facedir=mlc.getBoolean("facedir",false);
		if (this.onTickSkillName != null) {
			this.onTickSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onTickSkillName);
		}
		if (this.onHitSkillName != null) {
			this.onHitSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onHitSkillName);
		}
		if (this.onEndSkillName != null) {
			this.onEndSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onEndSkillName);
		}
		if (this.onStartSkillName != null) {
			this.onStartSkill = Utils.mythicmobs.getSkillManager().getSkill(this.onStartSkillName);
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
    	private Handler vh;
        private boolean cancelled,useOffset,iYaw,islocationtarget;
        private SkillMetadata data;
        private ActiveMob am;
        private SkillCaster caster;
        private Entity owner,entity;
        private Location currentLocation,oldLocation;
        private int taskId;
        private HashSet<LivingEntity> targets;
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
        	this.vh=Volatile.handler;
        	this.islocationtarget=target==null&&location!=null;
            this.cancelled = false;
            this.data = data;
            this.data.setCallingEvent(this);
            this.caster = data.getCaster();
            this.owner=this.caster.getEntity().getBukkitEntity();
            this.currentLocation=this.caster.getEntity().getBukkitEntity().getLocation();
            this.yOffset=MStatueMechanic.this.YOffset;
            this.sOffset=MStatueMechanic.this.sOffset;
            this.fOffset=MStatueMechanic.this.fOffset;
            this.count=0;
            if (MStatueMechanic.this.YOffset != 0.0f) {
                this.currentLocation.setY(this.currentLocation.getY()+this.yOffset);
            }
    		this.useOffset=MStatueMechanic.this.fOffset!=0d||MStatueMechanic.this.sOffset!=0d;
    		if (this.useOffset) {
    			Vector soV=Utils.getSideOffsetVector(this.currentLocation.getYaw(), this.sOffset, this.iYaw);
    			Vector foV=Utils.getFrontBackOffsetVector(this.currentLocation.getDirection(),this.fOffset);
    			this.currentLocation.add(soV);
    			this.currentLocation.add(foV);
    		}
            this.targets=new HashSet<LivingEntity>();
            this.immune=new HashMap<LivingEntity,Long>();
            this.am=MStatueMechanic.this.material.spawn(BukkitAdapter.adapt(this.currentLocation),0);
            this.entity=this.am.getEntity().getBukkitEntity();
            Main.entityCache.add(this.entity);
			this.entity.setMetadata(Utils.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.entity.setMetadata(Utils.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.entity.setInvulnerable(true);
			this.entity.setGravity(false);
			this.entity.setTicksLived(Integer.MAX_VALUE);
			vh.teleportEntityPacket(this.entity);
			vh.changeHitBox((Entity)this.entity,0,0,0);
            if (MStatueMechanic.this.onStartSkill.isPresent()
            		&&MStatueMechanic.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                MStatueMechanic.this.onStartSkill.get().execute(sData);
            }
            this.taskId=TaskManager.get().scheduleTask(this, 0, 1);
        }

        @Override
        public void run() {
            if (this.cancelled) {
                return;
            }
            if(this.count>MStatueMechanic.this.duration) {
                this.stop();
                return;
            }
            if (this.entity==null||this.owner==null||this.owner.isDead()) {
            	this.stop();
            	return;
            }
            if (oldLocation.getX()!=currentLocation.getX()
            		||oldLocation.getY()!=currentLocation.getY()
            		||oldLocation.getZ()!=currentLocation.getZ()) {
                this.oldLocation=this.currentLocation.clone();
            }
            if (!this.islocationtarget) {
            	this.currentLocation=this.owner.getLocation().add(0d,this.yOffset,0d);
            } else {
            	this.currentLocation=this.entity.getLocation();
            }
            if (this.dur>MStatueMechanic.this.tickInterval) {
                HitBox hitBox = new HitBox(this.currentLocation,MStatueMechanic.this.hitRadius,MStatueMechanic.this.verticalHitRadius);
                ListIterator<Entity>lit1=entity.getNearbyEntities(1,1,1).listIterator();
                while(lit1.hasNext()) {
                	Entity ee=lit1.next();
                	if (ee==this.owner||ee==entity||ee.isDead()
                			||!(ee instanceof LivingEntity)
                			||!hitBox.contains(ee.getLocation().add(0,0.6,0))
                			||this.immune.containsKey((LivingEntity)ee)
                			||ee.hasMetadata(Utils.noTargetVar)
                			||(ee instanceof Player)&&ee!=entity&&!MStatueMechanic.this.hitPlayers
                			||!MStatueMechanic.this.hitNonPlayers&&ee!=entity) continue;
                   	LivingEntity t1=(LivingEntity)ee;
                   	this.targets.add(t1);
   					this.immune.put(t1,System.currentTimeMillis());
   					break;
                }
                Iterator<Map.Entry<LivingEntity,Long>>iter=this.immune.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<LivingEntity,Long>entry=iter.next();
                    if (entry.getValue()>=System.currentTimeMillis()-2000) continue;
                    iter.remove();
                }
                if (this.targets.size() > 0) {
                    this.doHit((HashSet)this.targets.clone());
                }
                this.targets.clear();
            	if (MStatueMechanic.this.onTickSkill.isPresent()
            			&&MStatueMechanic.this.onTickSkill.get().isUsable(this.data)) {
            		SkillMetadata sData = this.data.deepClone();
            		AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
            		HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
            		targets.add(location);
            		sData.setLocationTargets(targets);
            		sData.setOrigin(location);
            		MStatueMechanic.this.onTickSkill.get().execute(sData);
            	}
            	this.dur=0;
            }
            if (!this.islocationtarget) {
            	double x = this.currentLocation.getX();
            	double z = this.currentLocation.getZ();
            	Vector soV=Utils.getSideOffsetVector(this.owner.getLocation().getYaw(), this.sOffset, this.iYaw);
            	Vector foV=Utils.getFrontBackOffsetVector(this.owner.getLocation().getDirection(),this.fOffset);
            	x+=soV.getX()+foV.getX();
            	z+=soV.getZ()+foV.getZ();
            	this.currentLocation.setX(x);
            	this.currentLocation.setZ(z);
            	NMSUtils.setLocation(this.entity,
            			x,
            			this.currentLocation.getY(),
            			z,
            			facedir?Utils.lookAtYaw(oldLocation,currentLocation):currentLocation.getYaw(),
            			this.currentLocation.getPitch());
            } else {
            	NMSUtils.setLocation(this.entity,
            			this.currentLocation.getX(),
            			this.currentLocation.getY(),
            			this.currentLocation.getZ(),
            			currentLocation.getYaw(),
            			this.currentLocation.getPitch());
            }
			this.count++;
			this.dur++;
        }

        public void doHit(HashSet<AbstractEntity> targets) {
            if (MStatueMechanic.this.onHitSkill.isPresent() && MStatueMechanic.this.onHitSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                sData.setEntityTargets(targets);
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                MStatueMechanic.this.onHitSkill.get().execute(sData);
            }
        }

        public void stop() {
            if (MStatueMechanic.this.onEndSkill.isPresent() && MStatueMechanic.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                MStatueMechanic.this.onEndSkill.get().execute(sData.setOrigin(BukkitAdapter.adapt(this.currentLocation))
                		.setLocationTarget(BukkitAdapter.adapt(this.currentLocation)));
            }
            TaskManager.get().cancelTask(this.taskId);
            this.entity.remove();
            this.cancelled = true;
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
