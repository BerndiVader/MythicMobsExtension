package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "lastcollided", author = "BerndiVader")
public class LastCollidedEntity extends AbstractCustomCondition implements IEntityCondition {
	String[] arr1;

	public LastCollidedEntity(String line, MythicLineConfig mlc) {
		super(line, mlc);
		arr1 = mlc.getString(new String[] { "types", "type", "t" }, "").toLowerCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity entity) {
		Entity e;
		if ((e = entity.getBukkitEntity()).hasMetadata(Utils.meta_LASTCOLLIDETYPE)) {
			String s1 = e.getMetadata(Utils.meta_LASTCOLLIDETYPE).get(0).asString().toLowerCase();
			for (int i1 = 0; i1 < arr1.length; i1++) {
				if (s1.equals(arr1[i1]))
					return true;
			}
		}
		return false;
	}
}
