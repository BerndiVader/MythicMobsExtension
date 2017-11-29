package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class PlayerGoggleMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	public static String str="mmGoggle";
	private long dur;
	private VolatileHandler vh=Main.getPlugin().getVolatileHandler();

	public PlayerGoggleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dur=(long)mlc.getInteger(new String[] {"duration","d"},120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()||target.getBukkitEntity().hasMetadata(str)) return false;
		target.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final Player p=(Player)target.getBukkitEntity();
		final AbstractEntity caster=data.getCaster().getEntity();
		final long d=this.dur;
		new BukkitRunnable() {
			long count=0;
			@Override
			public void run() {
				if (p==null
						||p.isDead()
						||count>d) {
					p.removeMetadata(str, Main.getPlugin());
					this.cancel();
				} else {
					Vector v=CustomSkillStuff.lookAtVec(p.getEyeLocation(),
							caster.getBukkitEntity().getLocation().add(0,caster.getEyeHeight(), 0));
					PlayerGoggleMechanic.this.vh.playerConnectionLookAt(p,(float)v.getX(),(float)v.getY());
				}
				count++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);
		return true;
	}

}
