package com.gmail.berndivader.mythicmobsext.thiefs;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class DropStolenItemsMechanic extends SkillMechanic implements INoTargetSkill {

	public DropStolenItemsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Entity e1 = data.getCaster().getEntity().getBukkitEntity();
		Iterator<Thief> ti = Thiefs.thiefhandler.getThiefs().iterator();
		UUID uuid = e1.getUniqueId();
		while (ti.hasNext()) {
			Thief thief = ti.next();
			if (uuid.equals((thief.getUuid()))) {
				e1.getWorld().dropItem(e1.getLocation(), new ItemStack(thief.getItem()));
				ti.remove();
			}
		}
		return true;
	}
}
