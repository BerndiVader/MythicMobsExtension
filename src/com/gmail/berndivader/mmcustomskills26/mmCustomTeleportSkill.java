package com.gmail.berndivader.mmcustomskills26;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitPlayer;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.targeters.ConsoleTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import io.lumine.xikage.mythicmobs.skills.targeters.MTOrigin;
import io.lumine.xikage.mythicmobs.skills.targeters.MTTriggerLocation;

public class mmCustomTeleportSkill extends SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill
{
	protected String stargeter, FinalSignal, inBetweenSignal;
	protected boolean inFrontOf, isLocations, returnToStart, sortTargets;
	protected long delay, noise, maxTargets, maxSources;
	protected AbstractEntity entityTarget;
	protected AbstractLocation startLocation;

    public mmCustomTeleportSkill(String line, MythicLineConfig mlc) {
        super(line, mlc);
        this.ASYNC_SAFE = false;
        this.noise = mlc.getLong(new String[]{"noise","n"},0L);
        this.delay = mlc.getLong(new String[]{"delay"},0L);
        if ((this.maxTargets = mlc.getLong(new String[]{"maxtargets","mt"},0L))<0) this.maxTargets=0L;
        if ((this.maxSources = mlc.getLong(new String[]{"maxsources","ms"},0L))<0) this.maxSources=0L;
        this.delay = mlc.getLong(new String[]{"delay"},0L);
        this.inFrontOf = mlc.getBoolean(new String[]{"infront","front","f"},false);
        this.returnToStart = mlc.getBoolean(new String[]{"returntostart","return","r"},false);
        this.inBetweenSignal = mlc.getString(new String[]{"betweensignal","bs"},null);
        this.FinalSignal = mlc.getString(new String[]{"finalsignal","fs"},null);
        String s = mlc.getString(new String[]{"destination","dest","d"},"@self").toLowerCase();
        s=s.replaceAll("<&lc>", "{");
        s=s.replaceAll("<&rc>", "}");
        s=s.replaceAll("<&eq>", "=");
        s=s.replaceAll("<&sc>", ";");
        s=s.replaceAll("<&cm>", ",");
        s=s.replaceAll("<&lb>", "[");
        s=s.replaceAll("<&rb>", "]");
        this.stargeter = s = s.substring(1, s.length() - 1);
        if (!this.stargeter.startsWith("@")) this.stargeter = "@"+this.stargeter;
    }
    
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return this.doMechanic(data, target);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.doMechanic(data, target);
	}
	
    private boolean doMechanic(SkillMetadata data, Object target) {
    	HashSet<?>osources=getDestination(this.stargeter, data);
    	
    	if (!osources.iterator().hasNext()) return false;
    	this.isLocations=osources.iterator().next() instanceof AbstractLocation?true:false;
    	if (target.getClass().equals(BukkitEntity.class) || target.getClass().equals(BukkitPlayer.class)) {
    		this.entityTarget = (AbstractEntity)target;
    		this.startLocation = ((AbstractEntity)target).getLocation();
    	} else {
    		Bukkit.getLogger().warning("A location is not a valid source for advanced teleport mechanic!");
    	}
		new BukkitRunnable() {
			AbstractEntity sourceEntity = entityTarget;
			boolean isLoc = isLocations;
			Iterator<?>it = osources.iterator();
			double n = noise;
			boolean ifo = inFrontOf;
			String bs = inBetweenSignal;
			String fs = FinalSignal;
			AbstractLocation start = startLocation;
			
			@Override
			public void run() {
				if (!this.it.hasNext()) {
					if (this.isLoc) {
						if (this.fs!=null) ((ActiveMob)data.getCaster()).signalMob(null, this.fs);
					} else {
						if (this.fs!=null) ((ActiveMob)data.getCaster()).signalMob((AbstractEntity)target, this.fs);
					}
					if (returnToStart) this.sourceEntity.teleport(this.start);
					this.cancel();
					return;
				}
				Object target = this.it.next();
				if (this.isLoc) {
					if (this.n>0) target = MobManager.findSafeSpawnLocation((AbstractLocation)target, (int)this.n, 0, ((ActiveMob)data.getCaster()).getType().getMythicEntity().getHeight(), false);
					if (this.bs!=null) ((ActiveMob)data.getCaster()).signalMob(null, this.bs);
				} else {
					AbstractEntity t = (AbstractEntity)target;
					target = ((AbstractEntity)target).getLocation();
					if (this.ifo) target = getTargetBlock((AbstractLocation)target, 1);
					if (this.n>0) target = MobManager.findSafeSpawnLocation(((AbstractLocation)target), (int)this.n, 0, ((ActiveMob)data.getCaster()).getType().getMythicEntity().getHeight(), false);
					if (this.bs!=null) ((ActiveMob)data.getCaster()).signalMob(t, this.bs);
				}
				this.sourceEntity.teleport((AbstractLocation)target);
			}
		}.runTaskTimer(Main.getPlugin(), 0L, this.delay);
    	return true;
    }
    
	private static HashSet<?> getDestination(String target, SkillMetadata skilldata) {
        SkillMetadata data = new SkillMetadata(SkillTrigger.API, skilldata.getCaster(), skilldata.getTrigger(), skilldata.getOrigin(), null, null, 1.0f);
    	Optional<SkillTargeter>maybeTargeter;
    	maybeTargeter = Optional.of(AbstractSkill.parseSkillTargeter(target));
	    if (maybeTargeter.isPresent()) {
            SkillTargeter targeter = maybeTargeter.get();
            if (targeter instanceof IEntitySelector) {
                data.setEntityTargets(((IEntitySelector)targeter).getEntities(data));
                ((IEntitySelector)targeter).filter(data, false);
                return data.getEntityTargets();
            }
            if (targeter instanceof ILocationSelector) {
                data.setLocationTargets(((ILocationSelector)targeter).getLocations(data));
                ((ILocationSelector)targeter).filter(data);
            } else if (targeter instanceof MTOrigin) {
                data.setLocationTargets(((MTOrigin)targeter).getLocation(data.getOrigin()));
            } else if (targeter instanceof MTTriggerLocation) {
                HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
                lTargets.add(data.getTrigger().getLocation());
                data.setLocationTargets(lTargets);
            }
            if (targeter instanceof ConsoleTargeter) {
                data.setEntityTargets(null);
                data.setLocationTargets(null);
            }
            return data.getLocationTargets();
        }
	    return null;
    }
	
	private static AbstractLocation getTargetBlock(AbstractLocation l, int range) {
		BlockIterator bit = new BlockIterator(BukkitAdapter.adapt(l), range);
		while(bit.hasNext()) {
			Block next = bit.next();
			if(next != null && next.getType() != Material.AIR) {
				return BukkitAdapter.adapt(next.getLocation().clone());
			}
		}
		return null;
	}
	
}
