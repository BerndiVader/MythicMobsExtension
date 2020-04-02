package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Vec2D;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "entitygoggle,entitygoggleat,entitylookin", author = "BerndiVader")
public class EntityGoogleMechanic extends SkillMechanic implements ITargetedEntitySkill {
	public static String str = "mmGoggle";
	private float rotate;
	private long dur;
	private boolean b;
	private Handler vh = Volatile.handler;
	final Optional<Location> location;

	public EntityGoogleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		b(skill.toLowerCase().startsWith("entitylookin"));
		this.dur = (long) mlc.getInteger(new String[] { "duration", "dur" }, 120);
		this.rotate = mlc.getFloat("rotate", 0f);
		String s1 = mlc.getString("location", null);
		Location l = null;
		if (s1 != null) {
			String[] arr1 = s1.toUpperCase().split(",");
			if (arr1.length < 4) {
				MythicMobs.error("There was an error while parsing location string for skill " + skill);
			} else {
				try {
					l = new Location(Bukkit.getWorld(arr1[3]), Double.parseDouble(arr1[0]), Double.parseDouble(arr1[1]),
							Double.parseDouble(arr1[2]));
				} catch (Exception ex) {
					MythicMobs.error("There was an error while creating the location for the skill " + skill + ": "
							+ ex.getMessage());
				}
			}
		}
		location = Optional.ofNullable(l);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (data.getCaster().getEntity().isPlayer())
			return false;
		if (data.getCaster().getEntity().getBukkitEntity().hasMetadata(str))
			data.getCaster().getEntity().getBukkitEntity().removeMetadata(str, Main.getPlugin());
		final AbstractEntity caster = data.getCaster().getEntity();
		final AbstractEntity target = this.location.isPresent() ? caster : t;
		caster.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final Long d = this.dur;
		final Boolean b1 = this.b;
		new BukkitRunnable() {
			long l = 0;
			Vec2D v2;

			@Override
			public void run() {
				if (caster == null || l > d || target == null || caster.isDead() || target.isDead()
						|| !caster.getBukkitEntity().hasMetadata(str)) {
					caster.getBukkitEntity().removeMetadata(str, Main.getPlugin());
					this.cancel();
				} else {
					if (!b1) {
						v2 = MathUtils.lookAtVec(BukkitAdapter.adapt(caster.getEyeLocation()),
								EntityGoogleMechanic.this.location.isPresent()
										? EntityGoogleMechanic.this.location.get()
										: target.getBukkitEntity().getLocation().add(0, target.getEyeHeight(), 0));
					} else {
						v2 = new Vec2D(target.getEyeLocation().getYaw(), target.getEyeLocation().getPitch());
					}
					EntityGoogleMechanic.this.vh.rotateEntityPacket(caster.getBukkitEntity(),
							(float) v2.getX() + EntityGoogleMechanic.this.rotate, (float) v2.getY());
				}
				l++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L, 1L);
		return true;
	}

	private void b(Boolean b) {
		this.b = b;
	}

}
