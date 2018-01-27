package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.NMS.NMSUtil;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;

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
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.targeters.ConsoleTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import io.lumine.xikage.mythicmobs.skills.targeters.MTOrigin;
import io.lumine.xikage.mythicmobs.skills.targeters.MTTriggerLocation;

public class CustomTeleportMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill, 
ITargetedLocationSkill {
	String stargeter, FinalSignal, inBetweenLastSignal, inBetweenNextSignal;
	boolean inFrontOf, isLocations, returnToStart, sortTargets, targetInsight, ignoreOwner;
	double delay, noise, maxTargets, frontOffset;
	AbstractEntity entityTarget;
	AbstractLocation startLocation;

	public CustomTeleportMechanic(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.ASYNC_SAFE=false;
		this.noise = mlc.getDouble(new String[] { "noise", "n", "radius", "r" }, 0D);
		this.delay = mlc.getDouble(new String[] { "teleportdelay","tdelay", "td" }, 0D);
		this.frontOffset=mlc.getDouble(new String[] {"frontoffest","fo"},0.0D);
		if ((this.maxTargets=mlc.getDouble(new String[] { "maxtargets", "mt" },0D))<0) this.maxTargets=0D;
		this.inFrontOf = mlc.getBoolean(new String[] { "infrontof", "infront", "front", "f" }, false);
		this.returnToStart = mlc.getBoolean(new String[] { "returntostart", "return", "rs" }, false);
		this.targetInsight = mlc.getBoolean(new String[] { "targetinsight", "insight", "is" }, false);
		this.ignoreOwner = mlc.getBoolean(new String[] { "ignoreowner", "io" }, false);
		this.inBetweenLastSignal = mlc.getString(new String[] { "betweenlastentitysignal", "bls" }, null);
		this.inBetweenNextSignal = mlc.getString(new String[] { "betweennextentitysignal", "bns" }, null);
		this.FinalSignal = mlc.getString(new String[] { "finalsignal", "final", "fs" }, null);

		String s = mlc.getString(new String[] { "destination", "dest", "d", "location", "loc", "l" }, "@self").toLowerCase();
		s = s.replaceAll("<&lc>", "{");
		s = s.replaceAll("<&rc>", "}");
		s = s.replaceAll("<&eq>", "=");
		s = s.replaceAll("<&sc>", ";");
		s = s.replaceAll("<&cm>", ",");
		s = s.replaceAll("<&lb>", "[");
		s = s.replaceAll("<&rb>", "]");
		this.stargeter=s=s.substring(1,s.length()-1);
		if (!this.stargeter.startsWith("@")) this.stargeter="@"+this.stargeter;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return this.doMechanic(data, target);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return this.doMechanic(data, target);
	}

	@SuppressWarnings("unchecked")
	private boolean doMechanic(SkillMetadata data, Object target) {
		String targeter = this.stargeter;
		if (target.getClass().equals(BukkitEntity.class)||target.getClass().equals(BukkitPlayer.class)) {
			targeter = SkillString.parseMobVariables(this.stargeter, data.getCaster(), (AbstractEntity) target,
					data.getTrigger());
			this.entityTarget = (AbstractEntity) target;
			this.startLocation = ((AbstractEntity) target).getLocation();
		} else {
			Bukkit.getLogger().warning("A location is not a valid source for advanced teleport mechanic!");
			return false;
		}
		HashSet<Object>osources=(HashSet<Object>)getDestination(targeter,data);
		Map<Double,Object>sortedsources=new TreeMap<>();
		if (osources==null||!osources.iterator().hasNext()) return false;
		this.isLocations = osources.iterator().next() instanceof AbstractLocation;
		if (this.maxTargets > 0 && osources.size() > this.maxTargets) {
			HashSet<Object> lsrc = new HashSet<>();
			Iterator<?> it = osources.iterator();
			int c = 1;
			while (it.hasNext()) {
				if (c > this.maxTargets) {
					osources.clear();
					osources.addAll(lsrc);
					break;
				}
				lsrc.add(it.next());
				c++;
			}
		}
		if (!this.isLocations && this.ignoreOwner && data.getCaster() instanceof ActiveMob
				&& ((ActiveMob) data.getCaster()).getOwner().isPresent()) {
			World w = data.getCaster().getEntity().getBukkitEntity().getWorld();
			osources.remove(BukkitAdapter.adapt(NMSUtil.getEntity(w,((ActiveMob) data.getCaster()).getOwner().get())));
		}
		if (this.sortTargets) {
			for(Object o:osources) {
				AbstractLocation l=this.isLocations?((AbstractLocation)o):((AbstractEntity)o).getLocation();
				double distance=data.getCaster().getLocation().distanceSquared(l);
				sortedsources.put(distance,o);
			}
		}

		new BukkitRunnable() {
			AbstractEntity sourceEntity = entityTarget;
			AbstractEntity lastEntity;
			boolean isLoc = isLocations;
			boolean ifo = inFrontOf;
			boolean is = targetInsight;
			boolean sorted = sortTargets;
			Iterator<?> it = osources.iterator();
			Map<Double,Object>sosources=sortedsources;
			double n = noise;
			double fo=CustomTeleportMechanic.this.frontOffset;
			String bls = inBetweenLastSignal;
			String bns = inBetweenNextSignal;
			String fs = FinalSignal;
			AbstractLocation start = startLocation;

			@Override
			public void run() {
				block: {
					if (!this.it.hasNext()) {
						if (returnToStart)
							this.sourceEntity.teleport(this.start);
						if (this.fs != null) {
							if (this.isLoc) {
								((ActiveMob) data.getCaster()).signalMob(null, this.fs);
							} else {
								((ActiveMob) data.getCaster()).signalMob((AbstractEntity) target, this.fs);
							}
						}
						this.cancel();
						return;
					}
					Object target = this.it.next();
					if (this.isLoc) {
						if (this.n > 0)
							target = MobManager.findSafeSpawnLocation((AbstractLocation) target, (int) this.n, 0,
									((ActiveMob) data.getCaster()).getType().getMythicEntity().getHeight(), false);
						if (this.fo!=0.0D) {
							Location ll=BukkitAdapter.adapt((AbstractLocation)target);
			    			Vector foV=Utils.getFrontBackOffsetVector(ll.getDirection(),this.fo);
			    			ll.add(foV);
			    			target=BukkitAdapter.adapt(ll);
						}
						this.sourceEntity.teleport((AbstractLocation) target);
						if (this.bls != null)
							((ActiveMob) data.getCaster()).signalMob(null, this.bls);
					} else {
						if (lastEntity == null)
							lastEntity = this.sourceEntity;
						AbstractEntity t = (AbstractEntity) target;
						target = ((AbstractEntity) target).getLocation();
						if (this.is && !this.sourceEntity.hasLineOfSight(t))
							break block;
						if (this.ifo)
							target = t.getLocation()
									.add(t.getLocation().getDirection().setY(0).normalize().multiply(2));
						if (this.n > 0)
							target = MobManager.findSafeSpawnLocation(((AbstractLocation) target), (int) this.n, 0,
									((ActiveMob) data.getCaster()).getType().getMythicEntity().getHeight(), false);
						if (this.bns != null)
							((ActiveMob) data.getCaster()).signalMob(t, this.bns);
						if (this.fo!=0.0D) {
							Location ll=BukkitAdapter.adapt((AbstractLocation)target);
			    			Vector foV=Utils.getFrontBackOffsetVector(ll.getDirection(),this.fo);
			    			ll.add(foV);
			    			target=BukkitAdapter.adapt(ll);
						}
						this.sourceEntity.teleport((AbstractLocation) target);
						if (this.bls != null)
							((ActiveMob) data.getCaster()).signalMob(this.lastEntity, this.bls);
						this.lastEntity = t;
					}
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0L, (long) this.delay);
		return true;
	}

	protected static HashSet<?>getDestination(String target, SkillMetadata skilldata) {
		SkillMetadata data=new SkillMetadata(SkillTrigger.API, skilldata.getCaster(), skilldata.getTrigger(),skilldata.getOrigin(), null, null, 1.0f);
		Optional<SkillTargeter>maybeTargeter;
		maybeTargeter=Optional.of(AbstractSkill.parseSkillTargeter(target));
		if (maybeTargeter.isPresent()) {
			SkillTargeter targeter = maybeTargeter.get();
			if (targeter instanceof IEntitySelector) {
				data.setEntityTargets(((IEntitySelector) targeter).getEntities(data));
				((IEntitySelector) targeter).filter(data, false);
				return data.getEntityTargets();
			}
			if (targeter instanceof ILocationSelector) {
				data.setLocationTargets(((ILocationSelector) targeter).getLocations(data));
				((ILocationSelector) targeter).filter(data);
			} else if (targeter instanceof MTOrigin) {
				data.setLocationTargets(((MTOrigin) targeter).getLocation(data.getOrigin()));
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

}
