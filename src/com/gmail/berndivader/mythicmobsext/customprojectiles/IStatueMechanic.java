package com.gmail.berndivader.mythicmobsext.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.volatilecode.Handler;

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
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class IStatueMechanic extends SkillMechanic
implements 
ITargetedEntitySkill,
ITargetedLocationSkill {
    Optional<Skill>onTickSkill=Optional.empty(),
    		onHitSkill=Optional.empty(),
    		onEndSkill=Optional.empty(),
    		onStartSkill=Optional.empty();
    Material material;
    String onTickSkillName,
    		onHitSkillName,
    		onEndSkillName,
    		onStartSkillName;
    int tickInterval,duration;
    float ticksPerSecond,
    		hitRadius,
    		verticalHitRadius,
    		YOffset;
    double sOffset,fOffset;
    boolean hitTarget=true,
    		hitPlayers=false,
    		hitNonPlayers=false,
    		hitTargetOnly=false;

    public IStatueMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
		String i=mlc.getString(new String[] {"item","i"},"DIRT").toUpperCase();
		try {
			this.material=Material.valueOf(i);
		} catch (Exception e){
			this.material=Material.DIRT;
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
		if (this.onTickSkillName != null) {
			this.onTickSkill = Main.mythicmobs.getSkillManager().getSkill(this.onTickSkillName);
		}
		if (this.onHitSkillName != null) {
			this.onHitSkill = Main.mythicmobs.getSkillManager().getSkill(this.onHitSkillName);
		}
		if (this.onEndSkillName != null) {
			this.onEndSkill = Main.mythicmobs.getSkillManager().getSkill(this.onEndSkillName);
		}
		if (this.onStartSkillName != null) {
			this.onStartSkill = Main.mythicmobs.getSkillManager().getSkill(this.onStartSkillName);
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
        private Item item;
        private SkillCaster caster;
        private Entity owner;
        private Location currentLocation,oldLocation;
        private int taskId;
        private HashSet<LivingEntity> targets;
        private Map<LivingEntity,Long> immune;
        private double sOffset,fOffset,yOffset;
        private int count,dur;
        private Vector vEmpty=new Vector(0d,0d,0d);
        
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
            this.yOffset=IStatueMechanic.this.YOffset;
            this.sOffset=IStatueMechanic.this.sOffset;
            this.fOffset=IStatueMechanic.this.fOffset;
            this.count=0;
            if (IStatueMechanic.this.YOffset != 0.0f) {
                this.currentLocation.setY(this.currentLocation.getY()+this.yOffset);
            }
    		this.useOffset=IStatueMechanic.this.fOffset!=0d||IStatueMechanic.this.sOffset!=0d;
    		if (this.useOffset) {
    			Vector soV=Utils.getSideOffsetVector(this.currentLocation.getYaw(), this.sOffset, this.iYaw);
    			Vector foV=Utils.getFrontBackOffsetVector(this.currentLocation.getDirection(),this.fOffset);
    			this.currentLocation.add(soV);
    			this.currentLocation.add(foV);
    		}
            this.targets=new HashSet<LivingEntity>();
            this.immune=new HashMap<LivingEntity,Long>();
            this.item=this.currentLocation.getWorld().dropItem(this.currentLocation, new ItemStack(IStatueMechanic.this.material));
            Main.entityCache.add(this.item);
			this.item.setMetadata(Main.mpNameVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setMetadata(Main.noTargetVar, new FixedMetadataValue(Main.getPlugin(), null));
			this.item.setInvulnerable(true);
			this.item.setGravity(false);
			this.item.setTicksLived(Integer.MAX_VALUE);
			this.item.setPickupDelay(Integer.MAX_VALUE);
			vh.teleportEntityPacket(this.item);
			vh.changeHitBox((Entity)this.item,0,0,0);
            if (IStatueMechanic.this.onStartSkill.isPresent()
            		&&IStatueMechanic.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                IStatueMechanic.this.onStartSkill.get().execute(sData);
            }
            this.taskId=TaskManager.get().scheduleTask(this, 0, 1);
        }

        @Override
        public void run() {
            if (this.cancelled) {
                return;
            }
            if(this.count>IStatueMechanic.this.duration) {
                this.stop();
                return;
            }
            if (this.item==null) {
            	this.stop();
            	return;
            }
            this.oldLocation=this.currentLocation.clone();
            if (!this.islocationtarget) this.currentLocation=this.owner.getLocation().add(0d,this.yOffset,0d);
            if (this.dur>IStatueMechanic.this.tickInterval) {
                HitBox hitBox = new HitBox(this.currentLocation,IStatueMechanic.this.hitRadius,IStatueMechanic.this.verticalHitRadius);
                ListIterator<Entity>lit1=item.getNearbyEntities(1,1,1).listIterator();
                while(lit1.hasNext()) {
                	Entity ee=lit1.next();
                	if (ee==this.owner||ee==item||ee.isDead()
                			||!(ee instanceof LivingEntity)
                			||!hitBox.contains(ee.getLocation().add(0,0.6,0))
                			||this.immune.containsKey((LivingEntity)ee)
                			||ee.hasMetadata(Main.noTargetVar)
                			||(ee instanceof Player)&&ee!=item&&!IStatueMechanic.this.hitPlayers
                			||!IStatueMechanic.this.hitNonPlayers&&ee!=item) continue;
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
            	if (IStatueMechanic.this.onTickSkill.isPresent()
            			&&IStatueMechanic.this.onTickSkill.get().isUsable(this.data)) {
            		SkillMetadata sData = this.data.deepClone();
            		AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
            		HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
            		targets.add(location);
            		sData.setLocationTargets(targets);
            		sData.setOrigin(location);
            		IStatueMechanic.this.onTickSkill.get().execute(sData);
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
    			this.item.setVelocity(this.currentLocation.toVector().subtract(this.oldLocation.toVector()));
    			NMSUtils.setLocation(this.item,this.oldLocation.getX(),this.oldLocation.getY(),this.oldLocation.getZ(),this.oldLocation.getYaw(), this.oldLocation.getPitch());
            } else {
    			this.item.setVelocity(this.vEmpty);
    			NMSUtils.setLocation(this.item,this.currentLocation.getX(),this.currentLocation.getY(),this.currentLocation.getZ(),this.currentLocation.getYaw(), this.currentLocation.getPitch());
            }
			this.count++;
			this.dur++;
        }

        public void doHit(HashSet<AbstractEntity> targets) {
            if (IStatueMechanic.this.onHitSkill.isPresent() && IStatueMechanic.this.onHitSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                sData.setEntityTargets(targets);
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                IStatueMechanic.this.onHitSkill.get().execute(sData);
            }
        }

        public void stop() {
            if (IStatueMechanic.this.onEndSkill.isPresent() && IStatueMechanic.this.onEndSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                IStatueMechanic.this.onEndSkill.get().execute(sData.setOrigin(BukkitAdapter.adapt(this.currentLocation))
                		.setLocationTarget(BukkitAdapter.adapt(this.currentLocation)));
            }
            TaskManager.get().cancelTask(this.taskId);
            this.item.remove();
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
