package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.PathfinderGoal;

public class PathfinderGoalAttackSelector extends PathfinderGoal {
	EntityInsentient e;
	int priority;
	byte state;
	PathFinderGoalShoot pathfinder_shoot;
	PathfinderGoalMeleeRangeAttack pathfinder_melee;

	public PathfinderGoalAttackSelector(EntityInsentient e2, int priority) {
		this.e = e2;
		this.priority = priority;
		this.state = -1;
		pathfinder_shoot = new PathFinderGoalShoot(this.e, 1.8, 20, 30, 3);
		pathfinder_melee = new PathfinderGoalMeleeRangeAttack((EntityCreature) this.e, 1, true, 1);
	}

	@Override
	public boolean a() {
		return this.e != null && this.e.isAlive();
	}

	@Override
	public boolean b() {
		ItemStack itemstack = this.e.getItemInMainHand();
		if (itemstack.getItem() == Items.BOW) {
			if (state != 1) {
				this.e.goalSelector.a(this.pathfinder_melee);
				this.e.goalSelector.a(this.priority, pathfinder_shoot);
				state = 1;
			}
		} else {
			if (state != 2) {
				this.e.goalSelector.a(this.pathfinder_shoot);
				this.e.goalSelector.a(this.priority, pathfinder_melee);
				state = 2;
			}
		}
		return true;
	}
}
