package com.gmail.berndivader.mythicmobsext.volatilecode.v1_14_R1.pathfindergoals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.events.EntityCollideEvent;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.World;

public class PathfinderGoalNotifyOnCollide extends PathfinderGoal {
	int c;
	EntityInsentient e;
	Optional<ActiveMob> am;
	World w;
	HashMap<UUID, Integer> cooldown;

	public PathfinderGoalNotifyOnCollide(EntityInsentient e2, int i1) {
		this.c = i1;
		this.e = e2;
		this.am = Optional.ofNullable(Utils.mobmanager.getMythicMobInstance(e.getBukkitEntity()));
		this.w = e2.world;
		this.cooldown = new HashMap<>();
	}

	public boolean a() {
		return this.e.collides;
	}

	public boolean b() {
		Iterator<Map.Entry<UUID, Integer>> it = this.cooldown.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<UUID, Integer> pair = (Map.Entry<UUID, Integer>) it.next();
			int i1 = pair.getValue();
			if (i1-- < 1) {
				it.remove();
				continue;
			}
			pair.setValue(i1);
		}
		return true;
	}

	public void e() {
		ListIterator<Entity> it1 = this.w.getEntities((Entity) this.e, this.e.getBoundingBox()).listIterator();
		while (it1.hasNext()) {
			Entity ee = it1.next();
			if (this.cooldown.containsKey(ee.getUniqueID()))
				continue;
			this.cooldown.put(ee.getUniqueID(), this.c);
			Main.pluginmanager
					.callEvent(new EntityCollideEvent(am.get(), this.e.getBukkitEntity(), ee.getBukkitEntity()));
			this.e.getBukkitEntity().setMetadata(Utils.meta_LASTCOLLIDETYPE,
					new FixedMetadataValue(Main.getPlugin(), ee.getBukkitEntity().getType().toString()));
			if (am.isPresent())
				new TriggeredSkill(SkillTrigger.BLOCK, this.am.get(), BukkitAdapter.adapt(ee.getBukkitEntity()),
						new Pair[0]);
		}
	}
}