package com.gmail.berndivader.mythicmobsext.volatilecode.v1_14_R1.pathfindergoals;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.google.common.base.Predicate;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalTarget;
import net.minecraft.server.v1_14_R1.ScoreboardTeam;
import org.bukkit.event.entity.EntityTargetEvent;

public class PathfinderGoalOtherTeams<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {
	ScoreboardTeam team1, team2;

	public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
		this(entitycreature, oclass, flag, false);
	}

	public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
		this(entitycreature, oclass, 10, flag, flag1, null);
	}

	public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, int i2, boolean flag, boolean flag1,
			Predicate<? super T> predicate) {
		super(entitycreature, oclass, flag);
	}

	public boolean a() {
		//
		return false;
	}

	@Override
	public boolean b() {
		EntityLiving entityliving = this.e.getGoalTarget();
		if (entityliving == null)
			entityliving = this.g;
		if (entityliving == null)
			return false;
		if (!entityliving.isAlive())
			return false;
		teams(entityliving);
		if (this.team1 != null && this.team1 == this.team2) {
			this.d();
			return false;
		}
		double d0 = this.k();
		if (this.e.h(entityliving) > d0 * d0) {
			return false;
		}
		if (this.f) {
			int d = (int) NMSUtils.getField("d", PathfinderGoalTarget.class, (Object) this);
			if (this.e.getEntitySenses().a(entityliving)) {
				NMSUtils.setField("d", PathfinderGoalTarget.class, (Object) this, 0);
			} else {
				NMSUtils.setField("d", PathfinderGoalTarget.class, (Object) this, d++);
				if (d++ > this.h)
					return false;
			}
		}
		if (entityliving instanceof EntityHuman && ((EntityHuman) entityliving).abilities.isInvulnerable) {
			return false;
		}
		this.e.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
		return true;
	}

	private void teams(EntityLiving entityliving) {
		this.team1 = this.e.getWorld().getScoreboard().getPlayerTeam(this.e.getUniqueID().toString());
		this.team2 = entityliving instanceof EntityPlayer
				? this.e.getWorld().getScoreboard().getPlayerTeam(entityliving.getName())
				: this.e.getWorld().getScoreboard().getPlayerTeam(entityliving.getUniqueID().toString());
	}

}