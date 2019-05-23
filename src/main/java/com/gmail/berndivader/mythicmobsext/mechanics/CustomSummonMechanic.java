package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Creature;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntity;
import io.lumine.xikage.mythicmobs.skills.*;

@ExternalAnnotation(name="customsummon",author="BerndiVader")
public class CustomSummonMechanic extends SkillMechanic
		implements
		ITargetedLocationSkill,
		ITargetedEntitySkill {
	MythicMob mm;
	MythicEntity me;
	String tag,amount;
	int noise,yNoise;
	boolean yUpOnly,onSurface,inheritThreatTable,copyThreatTable,useEyeDirection,setowner,invisible,leashtocaster;
	double addx,addy,addz,inFrontBlocks;
	String reason;

	public CustomSummonMechanic(String skill, MythicLineConfig mlc) {
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
		this.mm = Utils.mobmanager.getMythicMob(strType);
		if (this.mm == null) this.me = MythicEntity.getMythicEntity(strType);
		this.reason=mlc.getString(new String[] {"customreason","custom","cr"},"CUSTOM").toUpperCase();
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
					.adapt(MathUtils.getLocationInFront(BukkitAdapter.adapt(target), this.inFrontBlocks));
		}
		target.add(this.addx, this.addy, this.addz);
		int amount=MathUtils.randomRangeInt(this.amount);
		if (this.mm != null) {
			for (int i=1;i<=amount;i++) {
				AbstractLocation l=noise>0?MobManager.findSafeSpawnLocation(target,(int)this.noise,(int)this.yNoise,this.mm.getMythicEntity().getHeight(), this.yUpOnly):target;
				ActiveMob ams = this.mm.spawn(l, data.getCaster().getLevel());
				if (ams==null||ams.getEntity()==null||ams.getEntity().isDead()) continue;
				ams.getEntity().getBukkitEntity().setMetadata(Utils.meta_CUSTOMSPAWNREASON,new FixedMetadataValue(Main.getPlugin(),this.reason));
				if (this.leashtocaster&&ams.getEntity().getBukkitEntity() instanceof Creature) {
					Creature c=(Creature)ams.getEntity().getBukkitEntity();
					c.setLeashHolder(data.getCaster().getEntity().getBukkitEntity());
				}
				if (this.invisible) Utils.applyInvisible(ams.getLivingEntity(),0);
				Utils.mythicmobs.getEntityManager().registerMob(ams.getEntity().getWorld(), ams.getEntity());
				if (this.tag!=null) {
					String tt = SkillString.unparseMessageSpecialChars(this.tag);
					tt=Utils.parseMobVariables(tt,data,data.getCaster().getEntity(),te,null);
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
			return true;
		}
		if (this.me!=null) {
			for (int i = 1; i <= amount; ++i) {
				AbstractLocation l=this.noise>0?MobManager.findSafeSpawnLocation(target,(int)this.noise,(int)this.yNoise,this.me.getHeight(),this.yUpOnly):target;
				this.me.spawn(BukkitAdapter.adapt(l));
			}
			return true;
		}
		return false;
	}
	
}
