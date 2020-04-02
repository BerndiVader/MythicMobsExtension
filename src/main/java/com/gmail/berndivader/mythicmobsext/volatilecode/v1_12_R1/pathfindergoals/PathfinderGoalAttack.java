package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;

import java.util.Optional;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public class PathfinderGoalAttack extends PathfinderGoalMeleeAttack {
	protected float r;
	Optional<ActiveMob> am;

	public PathfinderGoalAttack(EntityCreature e, double d, boolean b, float r) {
		super(e, d, b);
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
		if (d2 <= d3 && this.c <= 0) {
			this.c = 20;
			this.b.a(EnumHand.MAIN_HAND);
			this.b.B(entityLiving);
			ActiveMob am = Utils.mobmanager.getMythicMobInstance(this.b.getBukkitEntity());
			if (am != null)
				am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()), Utils.signal_AIHIT);
		}
	}

	@Override
	protected double a(EntityLiving e) {
		return (double) (this.b.width * this.r * this.b.width * this.r + e.width);
	}
}
