package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "push,pushto", author = "BerndiVader")
public class PushCasterMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

	float s;
	boolean debug, exact, set, magneto;
	double reducemagnetobydistance;
	Optional<RangedDouble> clamp = Optional.empty();

	public PushCasterMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;

		this.s = mlc.getFloat("speed", 1.0f);
		this.exact = mlc.getBoolean("exact", false);
		this.debug = mlc.getBoolean("debug", false);
		this.set = mlc.getBoolean("set", false);
		this.magneto = mlc.getBoolean("magneto", false);
		this.reducemagnetobydistance = mlc.getDouble(new String[] { "reducemagnetobydistance", "rmbd" }, 0d);
		String s1 = mlc.getString("clamp", "");
		if (s1.contains("to"))
			clamp = Optional.ofNullable(new RangedDouble(s1));

		if (debug)
			System.err.println("Push mechanic loaded with skill line: " + skill);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		return cast(data, t.getLocation(), t);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation l) {
		return cast(data, l, null);
	}

	public boolean cast(SkillMetadata data, AbstractLocation l, AbstractEntity target) {
		final Location dest = this.magneto ? data.getCaster().getEntity().getBukkitEntity().getLocation()
				: BukkitAdapter.adapt(l);
		final Entity caster = this.magneto && target != null ? target.getBukkitEntity()
				: data.getCaster().getEntity().getBukkitEntity();
		final Vector final_distance_sq = dest.toVector().subtract(caster.getLocation().toVector()).normalize();
		double speed = this.s;
		final double final_length = dest.toVector().subtract(caster.getLocation().toVector()).length();
		final int time = (int) ((final_length) / speed);

		if (exact) {
			new BukkitRunnable() {
				int ticks = 0;
				final double speed = s;

				@Override
				public void run() {
					ticks++;
					Vector delta_distance = dest.toVector().subtract(caster.getLocation().toVector());
					double delta_length = delta_distance.length();
					double delta = speed * MathUtils.clamp(MathUtils.lerp(0d, final_length, delta_length), true);
					Vector mod_delta = final_distance_sq.clone().multiply(delta);
					caster.setVelocity(mod_delta);
					if (debug) {
						System.err.println("Push mechanic executed.\nWith mod vector " + delta_distance.toString());
					}
					if (ticks >= time) {
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(), 1l, 1l);
		} else {
			Vector distance = final_distance_sq.clone();
			if (magneto && reducemagnetobydistance != 0.0d) {
				int dd = (int) Math.sqrt(MathUtils.distance3D(caster.getLocation().toVector(), dest.toVector()));
				speed = MathUtils.clamp(this.s - (this.s * (dd * reducemagnetobydistance)), 0, this.s);
			}
			Vector mod_delta = set ? distance.multiply(speed) : distance.multiply(speed).add(caster.getVelocity());
			if (clamp.isPresent()) {
				if (!clamp.get().equals(mod_delta.length())) {
					double speed_delta = MathUtils.clamp(mod_delta.length(), clamp.get().getMin(),
							clamp.get().getMax());
					mod_delta = final_distance_sq.clone().multiply(speed_delta);
				}
			}
			caster.setVelocity(mod_delta);
			if (debug) {
				System.err.println("Push mechanic executed.\nWith mod vector " + mod_delta.toString());
			}
		}
		return true;
	}

}
