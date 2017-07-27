package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.ParticleMaker;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCustomParticleLineEffect extends CustomParticleEffect
		implements ITargetedEntitySkill, ITargetedLocationSkill {
	protected float distanceBetween, yStartOffset, vDestOffset, hDestOffset;
	protected boolean fromOrigin, ignoreYaw;

	public mmCustomParticleLineEffect(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.yStartOffset = mlc.getFloat(new String[] { "ystartoffset", "ys" }, 0.0f);
		this.vDestOffset = mlc.getFloat(new String[] { "vdestoffset", "vd" }, 0.0f);
		this.hDestOffset = mlc.getFloat(new String[] { "hdestoffset", "hd" }, 0.0f);
		this.distanceBetween = mlc.getFloat(new String[] { "distancebetween", "db" }, 0.25f);
		this.fromOrigin = mlc.getBoolean(new String[] { "fromorigin", "fo" }, false);
		this.ignoreYaw = mlc.getBoolean(new String[] { "ignoredestoffsetyaw", "idoy" }, false);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		this.playParticleLineEffect(data.getCaster(), data.getOrigin(), target);
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		this.playParticleLineEffect(data.getCaster(), data.getOrigin(), target.getLocation());
		return false;
	}

	protected void playParticleLineEffect(SkillCaster am, AbstractLocation origin, AbstractLocation target) {
		Location l = BukkitAdapter.adapt(target);
		if (this.hDestOffset != 0.0f) {
			l = getOffsetLocation(l, this.vDestOffset, this.hDestOffset, this.ignoreYaw);
		} else if (this.vDestOffset != 0.0f) {
			l = l.clone().add(0.0, this.vDestOffset, 0.0);
		}

		Location sl = this.fromOrigin ? BukkitAdapter.adapt(origin).add(0.0, this.yStartOffset, 0.0)
				: BukkitAdapter.adapt(am.getEntity()).getLocation().add(0.0, this.yStartOffset, 0.0);
		int c = (int) Math.ceil(sl.distance(l) / this.distanceBetween) - 1;
		if (c <= 0) {
			return;
		}
		Vector v = l.toVector().subtract(sl.toVector()).normalize().multiply(this.distanceBetween);
		Location l2 = sl.clone().add(0.0, this.yOffset, 0.0);
		for (int i = 0; i < c; ++i) {
			l2.add(v);
			if (this.directional) {
				this.playDirectionalParticleEffect(sl, l, l2);
				continue;
			}
			this.playParticleEffect(sl, l2);
		}
	}

	protected void playDirectionalParticleEffect(Location origin, Location target, Location spawn) {
		Vector direction = this.directionReversed ? origin.toVector().subtract(target.clone().toVector()).normalize()
				: target.toVector().subtract(origin.clone().toVector()).normalize();
		for (int i = 0; i < this.amount; ++i) {
			Location ln = spawn.clone().add(0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0,
					this.vSpread + MythicMobs.r.nextDouble() * this.vSpread * 2.0,
					0.0f - this.hSpread + MythicMobs.r.nextDouble() * this.hSpread * 2.0);
			new ParticleMaker.ParticlePacket(this.strParticle, direction, this.pSpeed, this.amount, true).send(ln,
					this.viewDistance);
		}
	}

}
