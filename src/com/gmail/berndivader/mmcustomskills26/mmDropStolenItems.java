package com.gmail.berndivader.mmcustomskills26;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmDropStolenItems extends SkillMechanic implements INoTargetSkill {
	protected ThiefHandler thiefhandler;

	public mmDropStolenItems(CustomMechanic skill, MythicLineConfig mlc) {
		super(skill.getConfigLine(), mlc);
		this.ASYNC_SAFE = false;
		thiefhandler = Main.getPlugin().getThiefHandler();
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Entity e1=data.getCaster().getEntity().getBukkitEntity();
		Iterator<Thief>ti=thiefhandler.getThiefs().iterator();
		UUID uuid = e1.getUniqueId();
		while (ti.hasNext()) {
			Thief thief = ti.next();
			if (uuid.equals((thief.getUuid()))) {
				e1.getWorld().dropItem(e1.getLocation(),new ItemStack(thief.getItem()));
				ti.remove();
			}
		}
		return true;
	}
}
