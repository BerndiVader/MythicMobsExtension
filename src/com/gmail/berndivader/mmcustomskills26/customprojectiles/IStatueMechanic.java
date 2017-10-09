package com.gmail.berndivader.mmcustomskills26.customprojectiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.EntityManager;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class IStatueMechanic extends SkillMechanic
implements 
ITargetedEntitySkill,
ITargetedLocationSkill {
	protected MythicMobs mythicmobs;
	protected EntityManager entitymanager;
	protected SkillManager skillmanager;
	protected MobManager mobmanager;
	
    protected Optional<Skill>onTickSkill=Optional.empty(),
    		onHitSkill=Optional.empty(),
    		onEndSkill=Optional.empty(),
    		onStartSkill=Optional.empty();
    protected Material material;
    protected String onTickSkillName,
    		onHitSkillName,
    		onEndSkillName,
    		onStartSkillName;
    protected int tickInterval;
    protected float ticksPerSecond,
    		hitRadius,
    		verticalHitRadius,
    		duration,
    		YOffset;
    protected double sOffset,
    		fOffset;
    protected boolean 
    		hitTarget=true,
    		hitPlayers=false,
    		hitNonPlayers=false,
    		hitTargetOnly=false;

    public IStatueMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE=false;
		this.mythicmobs=Main.getPlugin().getMythicMobs();
		this.entitymanager=this.mythicmobs.getEntityManager();
		this.skillmanager=this.mythicmobs.getSkillManager();
		this.mobmanager=this.mythicmobs.getMobManager();
		String i=mlc.getString(new String[] {"item","i"},"DIRT");
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
        this.duration=mlc.getFloat(new String[] {"maxduration","md"},10.0f);
        this.duration*= 1000.0f;
        this.verticalHitRadius = mlc.getFloat(new String[] {"verticalradius","vradius","vr"}, this.hitRadius);
        this.YOffset = mlc.getFloat(new String[] {"yoffset","yo"}, 1.0f);
        this.sOffset=mlc.getDouble(new String[] {"soffset","so"},0d);
        this.fOffset=mlc.getDouble(new String[] {"foffset","fo"},0d);
        this.hitPlayers = mlc.getBoolean(new String[] {"hitplayers","hp"}, false);
        this.hitNonPlayers = mlc.getBoolean(new String[] {"hitnonplayers","hnp"}, false);
        this.hitTarget = mlc.getBoolean(new String[] {"hittarget","ht"}, true);
        this.hitTargetOnly = mlc.getBoolean("hittargetonly", false);
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

    private class StatueTracker
    implements IParentSkill,
    Runnable {
        private boolean cancelled;
        private SkillMetadata data;
        private Item item;
        private SkillCaster caster;
        private Location currentLocation;
        private long startTime;
        private int taskId;
        private HashSet<LivingEntity> targets;
        private List<LivingEntity> inRange;
        private Map<LivingEntity,Long> immune;

        public StatueTracker(SkillMetadata data, AbstractEntity target) {
            this.cancelled = false;
            this.data = data;
            this.data.setCallingEvent(this);
            this.caster = data.getCaster();
            this.startTime = System.currentTimeMillis();
            this.currentLocation=this.caster.getEntity().getBukkitEntity().getLocation();
            if (IStatueMechanic.this.YOffset != 0.0f) {
                this.currentLocation.setY(this.currentLocation.getY()+(double)IStatueMechanic.this.YOffset);
            }
            this.taskId=TaskManager.get().scheduleTask(this, 0, IStatueMechanic.this.tickInterval);
            if (IStatueMechanic.this.hitPlayers || IStatueMechanic.this.hitNonPlayers || IStatueMechanic.this.hitTarget) {
                this.inRange = this.currentLocation.getWorld().getLivingEntities();
                Iterator<LivingEntity> iter = this.inRange.iterator();
                while (iter.hasNext()) {
                    LivingEntity e = iter.next();
                    if (e.getUniqueId().equals(this.caster.getEntity().getUniqueId())) {
                        iter.remove();
                        continue;
                    }
                    if (!IStatueMechanic.this.hitPlayers && (e instanceof Player) && !e.equals(target)) {
                        iter.remove();
                        continue;
                    }
                    if (IStatueMechanic.this.hitNonPlayers || (e instanceof Player) && !e.equals(target)) continue;
                    iter.remove();
                }
                if (IStatueMechanic.this.hitTarget) {
                    this.inRange.add((LivingEntity)target.getBukkitEntity());
                }
            }
            this.targets = new HashSet<LivingEntity>();
            this.immune = new HashMap<LivingEntity, Long>();
            if (IStatueMechanic.this.onStartSkill.isPresent() && IStatueMechanic.this.onStartSkill.get().isUsable(data)) {
                SkillMetadata sData = data.deepClone();
                sData.setLocationTarget(BukkitAdapter.adapt(this.currentLocation));
                sData.setOrigin(BukkitAdapter.adapt(this.currentLocation.clone()));
                IStatueMechanic.this.onStartSkill.get().execute(sData);
            }
        }

        @Override
        public void run() {
            if (this.cancelled) {
                return;
            }
            if (IStatueMechanic.this.duration > 0.0f && (float)this.startTime + IStatueMechanic.this.duration < (float)System.currentTimeMillis()) {
                this.stop();
                return;
            }
            if (this.inRange != null) {
                HitBox hitBox = new HitBox(this.currentLocation,IStatueMechanic.this.hitRadius,IStatueMechanic.this.verticalHitRadius);
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
            }
            if (IStatueMechanic.this.onTickSkill.isPresent() && IStatueMechanic.this.onTickSkill.get().isUsable(this.data)) {
                SkillMetadata sData = this.data.deepClone();
                AbstractLocation location = BukkitAdapter.adapt(this.currentLocation.clone());
                HashSet<AbstractLocation> targets = new HashSet<AbstractLocation>();
                targets.add(location);
                sData.setLocationTargets(targets);
                sData.setOrigin(location);
                IStatueMechanic.this.onTickSkill.get().execute(sData);
            }
            if (this.targets.size() > 0) {
                this.doHit((HashSet)this.targets.clone());
            }
            this.targets.clear();
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

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return false;
	}

}
