package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3.pathfindergoals;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;

import java.util.Optional;

import org.bukkit.entity.Monster;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class PathfinderGoalAttack extends PathfinderGoalMeleeAttack {
	protected float r;
	boolean is_monster;
	Optional<ActiveMob> am;

	public PathfinderGoalAttack(EntityCreature e, double d, boolean b, float r) {
		super(e, d, b);
		is_monster = e.getBukkitEntity() instanceof Monster;
		this.r = r;
		am = Utils.mobmanager.getActiveMob(e.getUniqueID());
	}

	@Override
	protected void a(EntityLiving entityLiving, double d2) {

		if (am.isPresent()) {
			ActiveMob active_mob = am.get();
			if (active_mob.getOwner().isPresent()) {
				if (active_mob.getOwner().get() == entityLiving.getUniqueID())
					return;
			}
		}

		double d3 = this.a(entityLiving);
		if (d2 <= d3 && h()) {
			g();
			if (is_monster) {
				this.a.swingHand(EnumHand.MAIN_HAND);
				this.a.attackEntity(entityLiving);
			}
			ActiveMob am = Utils.mobmanager.getMythicMobInstance(this.a.getBukkitEntity());
			if (am != null)
				am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()), Utils.signal_AIHIT);
		}
	}

	@Override
	protected double a(EntityLiving e) {
		return (double) (this.a.getWidth() * this.r * this.a.getWidth() * this.r + e.getWidth());
	}
}
