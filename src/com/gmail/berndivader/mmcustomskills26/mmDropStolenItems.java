package com.gmail.berndivader.mmcustomskills26;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
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

		SkillCaster caster = data.getCaster();
		if (!(caster instanceof ActiveMob))
			return false;
		ActiveMob am = (ActiveMob) caster;
		Iterator<Thief> ti = thiefhandler.getThiefs().iterator();
		UUID uuid = am.getUniqueId();
		while (ti.hasNext()) {
			Thief thief = ti.next();
			if (uuid.equals((thief.getUuid()))) {
				Location loc = BukkitAdapter.adapt(am.getLocation());
				World world = BukkitAdapter.adapt(am.getLocation().getWorld());
				world.dropItem(loc, new ItemStack(thief.getItem()));
				ti.remove();
			}
		}
		return true;
	}
}
