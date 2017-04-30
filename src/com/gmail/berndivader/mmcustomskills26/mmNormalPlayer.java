package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmNormalPlayer extends SkillMechanic implements ITargetedEntitySkill {

	public mmNormalPlayer(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!MythicMobs.inst().getMobManager().isActiveMob(target)) return false;
		ActiveMob am = MythicMobs.inst().getMobManager().getMythicMobInstance(target);
		am.signalMob(am.getEntity(), "NOACTIVEMOB");
		Location l = target.getBukkitEntity().getLocation();
		l.setY(0);
		AbstractEntity d = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
		target.getBukkitEntity().removeMetadata("MythicPlayer", Main.getPlugin());
		MythicMobs.inst().getMobManager().unregisterActiveMob(am.getEntity().getUniqueId());
		am.setEntity(d);
		d.remove();
		return true;
	}
	
}
