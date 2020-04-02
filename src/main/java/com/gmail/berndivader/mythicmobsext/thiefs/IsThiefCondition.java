package com.gmail.berndivader.mythicmobsext.thiefs;

import java.util.Iterator;
import java.util.UUID;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class IsThiefCondition extends AbstractCustomCondition implements IEntityCondition {
	String items[];
	boolean all;
	int length;

	public IsThiefCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);

		this.items = mlc.getString("items", "ANY").toUpperCase().split(",");
		all = items[0].equals("ANY");
		length = items.length;
	}

	@Override
	public boolean check(AbstractEntity arg0) {
		if (Thiefs.thiefhandler.getThief(arg0.getUniqueId()) == null) {
			return false;
		} else if (all) {
			return true;
		}
		Iterator<Thief> ti = Thiefs.thiefhandler.getThiefs().iterator();
		UUID uuid = arg0.getUniqueId();
		while (ti.hasNext()) {
			Thief thief = ti.next();
			if (uuid.equals((thief.getUuid()))) {
				for (int i1 = 0; i1 < length; i1++) {
					if (thief.getItem().getType().toString().equals(items[i1]))
						return true;
				}
			}
		}
		return false;
	}

}
