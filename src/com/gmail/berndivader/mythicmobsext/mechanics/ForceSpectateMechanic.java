package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

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
	private long d;
	public static String str="mmSpectate"; 

	public ForceSpectateMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.d=(long)mlc.getInteger(new String[] {"duration","dur"},120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isPlayer()
				&&(target.getBukkitEntity().getEntityId()==data.getCaster().getEntity().getBukkitEntity().getEntityId())) {
			Player p=(Player)target.getBukkitEntity();
			Volatile.handler.forceSpectate(p,p);
			target.getBukkitEntity().removeMetadata(str, Main.getPlugin());
			return true;
		} 
		if (target.isPlayer()
				&&!target.getBukkitEntity().hasMetadata(str)) {
			Player p=(Player)target.getBukkitEntity();
			Volatile.handler.forceSpectate(p,data.getCaster().getEntity().getBukkitEntity());
			p.setMetadata(str, new FixedMetadataValue(Main.getPlugin(),str));
			new BukkitRunnable() {
				@Override
				public void run() {
					if (p!=null&&p.isOnline()&&p.hasMetadata(str)) {
						Volatile.handler.forceSpectate(p,p);
						p.removeMetadata(str,Main.getPlugin());
					}
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(),d);
			return true;
		}
		return false;
	}
}
