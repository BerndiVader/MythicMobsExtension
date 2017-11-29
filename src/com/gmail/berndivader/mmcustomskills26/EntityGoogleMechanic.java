package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EntityGoogleMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill {
	public static String str="mmGoggle";
	private long dur;
	private VolatileHandler vh=Main.getPlugin().getVolatileHandler();

	public EntityGoogleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dur=(long)mlc.getInteger(new String[] {"duration","dur"},120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (data.getCaster().getEntity().isPlayer()
				||data.getCaster().getEntity().getBukkitEntity().hasMetadata(str)) return false;
		final AbstractEntity caster=data.getCaster().getEntity();
		final AbstractEntity target=t;
		caster.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final long d=this.dur;
		new BukkitRunnable() {
			long count=0;
			@Override
			public void run() {
				if (caster==null
						||target==null
						||count>d
						||caster.isDead()
						||target.isDead()) {
					caster.getBukkitEntity().removeMetadata(str, Main.getPlugin());
					this.cancel();
				} else {
					Vector v=CustomSkillStuff.lookAtVec(BukkitAdapter.adapt(caster.getEyeLocation()),
							target.getBukkitEntity().getLocation().add(0,target.getEyeHeight(), 0));
					EntityGoogleMechanic.this.vh.rotateEntityPacket(caster.getBukkitEntity(),(float)v.getX(),(float)v.getY());
				}
				count++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);
		return true;
	}

}
