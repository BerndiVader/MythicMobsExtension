package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ForceSpectateMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private int d;

	public ForceSpectateMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.d=mlc.getInteger("duration",120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()) {
			Main.getPlugin().getVolatileHandler().forceSpectate((Player)target.getBukkitEntity(),data.getCaster().getEntity().getBukkitEntity());
			new BukkitRunnable() {
				final Player p=(Player)target.getBukkitEntity();
				@Override
				public void run() {
					if (p!=null&&p.isOnline()) {
						Main.getPlugin().getVolatileHandler().forceSpectate(p,p);
					}
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(),(long)d);
			return true;
		}
		return false;
	}
}
