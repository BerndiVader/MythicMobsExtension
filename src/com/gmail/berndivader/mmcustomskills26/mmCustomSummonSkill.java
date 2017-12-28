package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Creature;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntity;
import io.lumine.xikage.mythicmobs.skills.*;

public class mmCustomSummonSkill extends SkillMechanic
		implements
		ITargetedLocationSkill,
		ITargetedEntitySkill {
	protected MythicMobs mythicmobs = Main.getPlugin().getMythicMobs();
	protected MobManager mobmanager = this.mythicmobs.getMobManager();
	private MythicMob mm;
	private MythicEntity me;
	private String tag,amount;
	private Integer noise,yNoise;
	private Boolean yUpOnly,onSurface,inheritThreatTable,copyThreatTable,useEyeDirection,setowner,invisible,leashtocaster;
	private Double addx,addy,addz,inFrontBlocks;

	public mmCustomSummonSkill(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.amount = mlc.getString(new String[] { "amount", "a" }, "1");
		if (this.amount.startsWith("-")) this.amount = "1";
		String strType = mlc.getString(new String[] { "mobtype", "type", "t", "mob", "m" }, null);
		this.invisible=mlc.getBoolean(new String[] {"invisible","inv"},false);
		this.tag = mlc.getString(new String[] { "addtag", "tag", "at" } );
		this.noise = mlc.getInteger(new String[] { "noise", "n", "radius", "r" }, 0);
		this.yNoise = mlc.getInteger(new String[] { "ynoise", "yn", "yradius", "yr" }, this.noise);
		this.yUpOnly = mlc.getBoolean(new String[] { "yradiusuponly", "ynoiseuponly", "yruo", "ynuo", "yu" }, false);
		this.onSurface = mlc.getBoolean(new String[] { "onsurface", "os", "s" }, true);
		this.copyThreatTable = mlc.getBoolean(new String[] { "copythreattable", "ctt" }, false);
		this.inheritThreatTable = mlc.getBoolean(new String[] { "inheritthreattable", "itt" }, false);
		this.addx = mlc.getDouble(new String[] { "addx", "ax", "relx", "rx" }, 0);
		this.addy = mlc.getDouble(new String[] { "addy", "ay", "rely", "ry" }, 0);
		this.addz = mlc.getDouble(new String[] { "addz", "az", "relz", "rz" }, 0);
		this.useEyeDirection = mlc.getBoolean(new String[] { "useeyedirection", "eyedirection", "ued" }, false);
		this.inFrontBlocks = mlc.getDouble(new String[] { "infrontblocks", "infront", "ifb" }, 0D);
		this.setowner = mlc.getBoolean(new String[] { "setowner", "so" }, false);
		this.leashtocaster=mlc.getBoolean(new String[] {"leashtocaster","leash","lc"},false);
		this.mm = this.mobmanager.getMythicMob(strType);
		if (this.mm == null) this.me = MythicEntity.getMythicEntity(strType);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation t) {
		return cast(data,t,null);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return cast(data,target.getLocation(),target);
	}
	
	private boolean cast(SkillMetadata data, AbstractLocation tl, AbstractEntity te) {
		AbstractLocation target = tl.clone();
		if (!data.getCaster().getEntity().getWorld().equals(tl.getWorld())) return false;
		if (this.useEyeDirection) {
			target = BukkitAdapter
					.adapt(CustomSkillStuff.getLocationInFront(BukkitAdapter.adapt(target), this.inFrontBlocks));
		}
		target.add(this.addx, this.addy, this.addz);
		int amount=CustomSkillStuff.randomRangeInt(this.amount);
		if (this.mm != null) {
			if (this.noise > 0) {
				for (int i=1;i<=amount;i++) {
					this.mythicmobs.getMobManager();
					AbstractLocation l = MobManager.findSafeSpawnLocation(target, (int) this.noise, (int) this.yNoise,
							this.mm.getMythicEntity().getHeight(), this.yUpOnly);
					ActiveMob ams = this.mm.spawn(l, data.getCaster().getLevel());
					if (ams == null
							||ams.getEntity()==null
							||ams.getEntity().isDead())
						continue;
					if (this.leashtocaster&&ams.getEntity().getBukkitEntity() instanceof Creature) {
						Creature c=(Creature)ams.getEntity().getBukkitEntity();
						c.setLeashHolder(data.getCaster().getEntity().getBukkitEntity());
					}
					if (this.invisible) CustomSkillStuff.applyInvisible(ams.getLivingEntity(),0);
					this.mythicmobs.getEntityManager().registerMob(ams.getEntity().getWorld(), ams.getEntity());
					if (this.tag!=null) {
						String tt = SkillString.unparseMessageSpecialChars(this.tag);
						tt = SkillString.parseMobVariables(tt, data.getCaster(), te, data.getTrigger());
						ams.getEntity().addScoreboardTag(tt);
					}
					if (this.setowner) {
						ams.setOwner(data.getCaster().getEntity().getUniqueId());
					}
					if (data.getCaster() instanceof ActiveMob) {
						ActiveMob am = (ActiveMob) data.getCaster();
 						ams.setParent(am);
						ams.setFaction(am.getFaction());
						if (this.copyThreatTable) {
							try {
								ams.importThreatTable(am.getThreatTable().clone());
								ams.getThreatTable().targetHighestThreat();
							} catch (CloneNotSupportedException e1) {
								e1.printStackTrace();
							}
							continue;
						}
						if (!this.inheritThreatTable)
							continue;
						ams.importThreatTable(am.getThreatTable());
						ams.getThreatTable().targetHighestThreat();
					}
				}
			} else {
				for (int i = 1; i <= amount; ++i) {
					ActiveMob ams = this.mm.spawn(target, data.getCaster().getLevel());
					if (ams == null
							||ams.getEntity()==null
							||!ams.getEntity().getWorld().equals(data.getCaster().getEntity().getWorld())
							||ams.getEntity().isDead())
						continue;
					if (this.invisible) CustomSkillStuff.applyInvisible(ams.getLivingEntity(),0);
					this.mythicmobs.getEntityManager().registerMob(ams.getEntity().getWorld(), ams.getEntity());
					if (this.setowner) {
						ams.setOwner(data.getCaster().getEntity().getUniqueId());
					}
					if (data.getCaster() instanceof ActiveMob) {
						ActiveMob am = (ActiveMob) data.getCaster();
 						ams.setParent(am);
						ams.setFaction(am.getFaction());
						if (this.copyThreatTable) {
							try {
								ams.importThreatTable(am.getThreatTable().clone());
								ams.getThreatTable().targetHighestThreat();
							} catch (CloneNotSupportedException e1) {
								e1.printStackTrace();
							}
							continue;
						}
						if (!this.inheritThreatTable)
							continue;
						ams.importThreatTable(am.getThreatTable());
						ams.getThreatTable().targetHighestThreat();
					}
				}
			}
			return true;
		}
		if (this.me != null) {
			if (this.noise > 0) {
				for (int i = 1; i <= amount; ++i) {
					AbstractLocation l = MobManager.findSafeSpawnLocation(target, (int) this.noise, (int) this.yNoise,
							this.me.getHeight(), this.yUpOnly);
					this.me.spawn(BukkitAdapter.adapt(l));
				}
			} else {
				for (int i = 1; i <= amount; ++i) {
					this.me.spawn(BukkitAdapter.adapt(target));
				}
			}
			return true;
		}
		return true;
	}
	
}
