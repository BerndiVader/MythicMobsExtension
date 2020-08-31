package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.logging.MythicLogger;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "spawnreason,spawnreason_ext", author = "BerndiVader")
public class SpawnReasonCondition extends AbstractCustomCondition implements IEntityCondition {
	SpawnReason[] reasons;
	String[] customs;
	boolean custom;

	public SpawnReasonCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		reasons = new SpawnReason[0];
		for (String s1 : mlc.getString(
				new String[] { "spawnreasons", "spawnreason", "reasons", "reason", "sr", "r", "types", "type", "t" },
				"NATURAL").toUpperCase().split(",")) {
			SpawnReason reason = null;
			try {
				reason = SpawnReason.valueOf(s1);
			} catch (Exception ex) {
				MythicLogger.error("Skipping SpawnReason: " + s1 + " because its unknown.");
				continue;
			}
			reasons = add(reasons, new SpawnReason[] { reason });
		}
		String s1 = mlc.getString(new String[] { "customs", "cr", "c" }, null);
		if (custom = s1 != null)
			customs = s1.toUpperCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!custom) {
			if (entity.getBukkitEntity().hasMetadata(Utils.meta_SPAWNREASON)) {
				SpawnReason r1 = (SpawnReason) entity.getBukkitEntity().getMetadata(Utils.meta_SPAWNREASON).get(0)
						.value();
				for (int i1 = 0; i1 < reasons.length; i1++) {
					if (r1 == reasons[i1])
						return true;
				}
			}
		} else {
			if (entity.getBukkitEntity().hasMetadata(Utils.meta_CUSTOMSPAWNREASON)) {
				String s1 = entity.getBukkitEntity().getMetadata(Utils.meta_CUSTOMSPAWNREASON).get(0).asString();
				for (int i1 = 0; i1 < reasons.length; i1++) {
					if (s1.equals(customs[i1]))
						return true;
				}
			}
		}
		return false;
	}

	static SpawnReason[] add(SpawnReason[] arr1, SpawnReason[] arr2) {
		SpawnReason[] arr = new SpawnReason[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, arr, 0, arr1.length);
		System.arraycopy(arr2, 0, arr, arr1.length, arr2.length);
		return arr;
	}
}
