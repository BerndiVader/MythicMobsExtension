package com.gmail.berndivader.mythicmobsext.mechanics;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.ParticleMaker;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="customparticles",author="BerndiVader")
public class CustomParticleEffect 
extends
SkillMechanic
implements 
ITargetedEntitySkill,
ITargetedLocationSkill {
	String strParticle;
	float hSpread;
	float vSpread;
	float pSpeed;
	float yOffset;
	float fOffset;
	float sOffset;
	boolean useEyeLocation;
	int amount;
	int viewDistance;
	boolean directional;
	boolean directionReversed;
	Color color = null;
	boolean fromOrigin;

	public CustomParticleEffect(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.strParticle = mlc.getString("particle", "reddust");
		this.strParticle = mlc.getString("p", this.strParticle);
		this.amount = mlc.getInteger("amount", 10);
		this.amount = mlc.getInteger("a", this.amount);
		this.hSpread = mlc.getFloat("hspread", 0.0f);
		this.hSpread = mlc.getFloat("hs", this.hSpread);
		this.vSpread = mlc.getFloat("vspread", 0.0f);
		this.vSpread = mlc.getFloat("vs", this.vSpread);
		this.pSpeed = mlc.getFloat("speed", 0.0f);
		this.pSpeed = mlc.getFloat("s", this.pSpeed);
		this.yOffset = mlc.getFloat("yoffset", 0.0f);
		this.yOffset = mlc.getFloat("y", this.yOffset);
		this.fOffset = mlc.getFloat("forwardoffset", 0.0f);
		this.fOffset = mlc.getFloat("foffset", this.fOffset);
		this.fOffset = mlc.getFloat("fo", this.fOffset);
		this.sOffset = mlc.getFloat(new String[] { "sideoffset", "soffset", "so" }, 0.0f);
		this.useEyeLocation = mlc.getBoolean("useeyelocation", false);
		this.useEyeLocation = mlc.getBoolean("uel", this.useEyeLocation);
		this.viewDistance = mlc.getInteger(new String[] { "viewdistance", "vd" }, 128);
		this.viewDistance *= this.viewDistance;
		this.fromOrigin = mlc.getBoolean(new String[] { "fromorigin", "fo" }, false);
		this.directional = mlc.getBoolean(new String[] { "directional", "d" }, false);
		this.directionReversed = mlc.getBoolean(new String[] { "directionreversed", "dr" }, false);
		String color = mlc.getString(new String[] { "color", "c" }, null, new String[0]);
		if (color != null) {
			this.color = Color.decode(color);
		}
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		Location l = BukkitAdapter.adapt(target);
		if (this.fOffset > 0.0f || this.sOffset != 0.0f) {
			l.setPitch(0.0f);
			l = CustomParticleEffect.move(l, this.fOffset, 0.0, this.sOffset);
		}
		if (this.directional) {
			this.playDirectionalParticleEffect(BukkitAdapter.adapt(data.getOrigin()), BukkitAdapter.adapt(target));
		} else {
			this.playParticleEffect(BukkitAdapter.adapt(data.getOrigin()), l);
		}
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		LivingEntity bukkitEntity = (LivingEntity)target.getBukkitEntity();
		Location l = this.useEyeLocation ? bukkitEntity.getEyeLocation() : BukkitAdapter.adapt(target.getLocation());
		if (this.fOffset > 0.0f || this.sOffset != 0.0f) {
			l.setPitch(0.0f);
			l = CustomParticleEffect.move(l, this.fOffset, 0.0, this.sOffset);
		}
		if (this.directional) {
			this.playDirectionalParticleEffect(BukkitAdapter.adapt(data.getOrigin()),
					BukkitAdapter.adapt(target.getLocation()));
		} else {
			this.playParticleEffect(BukkitAdapter.adapt(data.getOrigin()), l);
		}
		return true;
	}

	protected void playParticleEffect(Location origin, Location target) {
		if (Utils.serverV<7) {
			Utils.mythicmobs.getVolatileCodeHandler().doParticleEffect(target, this.strParticle, this.hSpread,
					this.vSpread, this.amount, this.pSpeed, this.yOffset, 256);
			return;
		}
		if (this.color != null) {
			this.playColoredParticleEffect(target);
			return;
		}
		Location ln = target.clone().add(0.0, this.yOffset, 0.0);
		new ParticleMaker.ParticlePacket(this.strParticle, this.hSpread, this.vSpread, this.hSpread, this.pSpeed,
				this.amount, true).send(ln, this.viewDistance);
	}

	protected void playColoredParticleEffect(Location target) {
		for (int i = 0; i < this.amount; ++i) {
			Location ln = target.clone().add(0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0,
					this.yOffset - this.vSpread + MythicMobs.r.nextDouble() * this.vSpread * 2.0,
					0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0);
			new ParticleMaker.ParticlePacket(this.strParticle, this.color, this.pSpeed, this.amount, true).send(ln,
					this.viewDistance);
		}
	}

	protected void playDirectionalParticleEffect(Location origin, Location target) {
		Vector direction = this.directionReversed ? origin.toVector().subtract(target.clone().toVector()).normalize()
				: target.toVector().subtract(origin.clone().toVector()).normalize();
		for (int i = 0; i < this.amount; ++i) {
			Location ln = target.clone().add(0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0,
					this.yOffset - this.vSpread + MythicMobs.r.nextDouble() * this.vSpread * 2.0,
					0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0);
			new ParticleMaker.ParticlePacket(this.strParticle, direction, this.pSpeed, this.amount, true).send(ln,
					this.viewDistance);
		}
	}

	protected Location getOffsetLocation(Location l, double vo, double ho, boolean iy) {
		double yaw = 0.0D;
		if (!iy)
			yaw = Math.toRadians(l.getYaw());
		double xo = Math.cos(yaw) * ho;
		double zo = Math.sin(yaw) * ho;
		Location offset = l.clone().add(xo, vo, zo);
		return offset;
	}

	public static Location move(Location loc, double dx, double dy, double dz) {
		AbstractVector off = CustomParticleEffect.rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
		double x = loc.getX() + off.getX();
		double y = loc.getY() + off.getY();
		double z = loc.getZ() + off.getZ();
		return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}

	public static AbstractVector rotate(float yaw, float pitch, double x, double y, double z) {
		float angle = yaw * 0.017453292F;
		double sinyaw = Math.sin(angle);
		double cosyaw = Math.cos(angle);

		angle = pitch * 0.017453292F;
		double sinpitch = Math.sin(angle);
		double cospitch = Math.cos(angle);

		double newx = 0.0D;
		double newy = 0.0D;
		double newz = 0.0D;
		newz -= x * cosyaw;
		newz -= y * sinyaw * sinpitch;
		newz -= z * sinyaw * cospitch;
		newx += x * sinyaw;
		newx -= y * cosyaw * sinpitch;
		newx -= z * cosyaw * cospitch;
		newy += y * cospitch;
		newy -= z * sinpitch;

		return new AbstractVector(newx, newy, newz);
	}

}
