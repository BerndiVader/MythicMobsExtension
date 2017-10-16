package com.gmail.berndivader.mmcustomskills26.customprojectiles;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.EntityManager;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import java.util.Optional;

import com.gmail.berndivader.mmcustomskills26.Main;

public class CustomProjectile extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {
	protected MythicMobs mythicmobs;
	protected EntityManager entitymanager;
	protected SkillManager skillmanager;
	protected MobManager mobmanager;

	protected Optional<Skill> onTickSkill = Optional.empty();
	protected Optional<Skill> onHitSkill = Optional.empty();
	protected Optional<Skill> onEndSkill = Optional.empty();
	protected Optional<Skill> onStartSkill = Optional.empty();
	protected Optional<Skill> onBounceSkill = Optional.empty();

	protected String onTickSkillName;
	protected String onHitSkillName;
	protected String onEndSkillName;
	protected String onStartSkillName;
	protected String onBounceSkillName;
	protected ProjectileType type;
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

	protected boolean pFaceDirection;
	protected double pVOffset;
	protected float pFOffset;
	protected boolean targetable, eyedir, bounce;
	protected float bounceReduce;

	protected String pEntityName;
	protected float pEntitySpin;
	protected float pEntityPitchOffset;

	public CustomProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;

		this.mythicmobs = Main.getPlugin().getMythicMobs();
		this.entitymanager = this.mythicmobs.getEntityManager();
		this.skillmanager = this.mythicmobs.getSkillManager();
		this.mobmanager = this.mythicmobs.getMobManager();

		this.onTickSkillName = mlc.getString(new String[] { "ontickskill", "ontick", "ot", "skill", "s", "meta", "m" });
		this.onHitSkillName = mlc.getString(new String[] { "onhitskill", "onhit", "oh" });
		this.onEndSkillName = mlc.getString(new String[] { "onendskill", "onend", "oe" });
		this.onStartSkillName = mlc.getString(new String[] { "onstartskill", "onstart", "os" });
		String type = mlc.getString("type", "NORMAL");
		this.type = ProjectileType.valueOf(type.toUpperCase());
		this.tickInterval = mlc.getInteger(new String[] { "interval", "int", "i" }, 4);
		this.ticksPerSecond = 20.0f / this.tickInterval;
		this.range = mlc.getFloat("maxrange", 40.0f);
		this.range = mlc.getFloat("mr", this.range);
		this.maxDistanceSquared = this.range * this.range;
		this.duration = mlc.getInteger(new String[] { "maxduration", "md" }, 100);
		this.duration *= 500;
		this.hitRadius = mlc.getFloat("hr", 2.0f);
		this.verticalHitRadius = mlc.getFloat("vr", 2.0f);
		this.startYOffset = mlc.getFloat("startyoffset", 1.0f);
		this.startYOffset = mlc.getFloat("syo", this.startYOffset);
		this.startForwardOffset = mlc.getFloat(new String[] { "forwardoffset", "startfoffset", "sfo" }, 1.0f);
		this.startSideOffset = mlc.getFloat(new String[] { "sideoffset", "soffset", "sso" }, 0.0f);
		this.targetYOffset = mlc.getFloat(new String[] { "targetyoffset", "targety", "tyo" }, 0.0f);
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
		this.projectileVelocityAccuracy = mlc.getFloat(new String[] { "accuracy", "ac", "a" }, 1.0f);
		float defNoise = (1.0f - this.projectileVelocityAccuracy) * 45.0f;
		this.projectileVelocityVertNoise = mlc.getFloat(new String[] { "verticaloffset", "vn" }, defNoise) / 10.0f;
		this.projectileVelocityHorizNoise = mlc.getFloat(new String[] { "horizontaloffset", "hn" }, defNoise);
		this.projectileVelocityVertNoiseBase = 0.0f - this.projectileVelocityVertNoise / 2.0f;
		this.projectileVelocityHorizNoiseBase = 0.0f - this.projectileVelocityHorizNoise / 2.0f;

		this.pFaceDirection = mlc.getBoolean("pfacedir", false);
		this.pVOffset = mlc.getDouble("pvoff", 0.0D);
		this.pFOffset = mlc.getFloat("pfoff", 0.0F);
		this.targetable = mlc.getBoolean("targetable", false);
		this.eyedir = mlc.getBoolean("eyedir", false);
		this.bounce = mlc.getBoolean("bounce", false);
		this.bounceReduce = mlc.getFloat("bred", 0.2F);
		this.pEntityName = mlc.getString(new String[] { "pobject", "projectilemythic", "pmythic" }, "MINECART");
		this.pEntitySpin = mlc.getFloat("pspin", 0.0F);
		this.pEntityPitchOffset = mlc.getFloat("ppOff", 360.0f);
		this.onBounceSkillName = mlc.getString(new String[] { "onbounceskill", "onbounce", "ob" });

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
		if (this.onBounceSkillName != null) {
			this.onBounceSkill = skillmanager.getSkill(this.onBounceSkillName);
		}

	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return false;
	}

	protected static enum ProjectileType {
		NORMAL, METEOR;
		private ProjectileType() {
		}
	}

}
