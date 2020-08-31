package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R1.pathfindergoals;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.events.EntityGrownEvent;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.PathfinderGoal;

public class PathfinderGoalEntityGrowNotify extends PathfinderGoal {
	protected EntityInsentient e;
	ActiveMob am;
	byte state;
	String signal;

	public PathfinderGoalEntityGrowNotify(EntityInsentient e2, String signal) {
		this.e = e2;
		this.signal = signal;
		this.am = Utils.mobmanager.getMythicMobInstance(e2.getBukkitEntity());
		this.state = e2.isBaby() ? (byte) 1 : (byte) -1;
	}

	@Override
	public boolean a() {
		return !e.isBaby() && state == 1;
	}

	@Override
	public boolean b() {
		if (state == 1) {
			state = 2;
			if (am != null && signal != null && !signal.isEmpty())
				am.signalMob(null, signal);
			Main.pluginmanager.callEvent(new EntityGrownEvent(e.getBukkitEntity(), am));
			return true;
		}
		return false;
	}
}
