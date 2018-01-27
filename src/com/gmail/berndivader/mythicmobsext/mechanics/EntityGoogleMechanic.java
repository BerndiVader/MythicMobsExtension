package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.utils.Vec2D;
import com.gmail.berndivader.volatilecode.Handler;

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
	private boolean b;
	private Handler vh=Main.getPlugin().getVolatileHandler();

	public EntityGoogleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		b(skill.toLowerCase().startsWith("entitylookin"));
		this.dur=(long)mlc.getInteger(new String[] { "duration", "dur" }, 120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
		if (data.getCaster().getEntity().isPlayer()) return false;
		if (data.getCaster().getEntity().getBukkitEntity().hasMetadata(str)) data.getCaster().getEntity().getBukkitEntity().removeMetadata(str, Main.getPlugin());
		final AbstractEntity caster=data.getCaster().getEntity();
		final AbstractEntity target=t;
		caster.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final Long d=this.dur;
		final Boolean b1=this.b;
		new BukkitRunnable() {
			long l=0;
			Vec2D v2=new Vec2D(target.getEyeLocation().getYaw(),target.getEyeLocation().getPitch());
			@Override
			public void run() {
				if (caster==null || target==null || l>d || caster.isDead() || target.isDead() || !caster.getBukkitEntity().hasMetadata(str)) {
					caster.getBukkitEntity().removeMetadata(str, Main.getPlugin());
					this.cancel();
				} else {
					if (!b1) {
						v2=Utils.lookAtVec(BukkitAdapter.adapt(caster.getEyeLocation()),
								target.getBukkitEntity().getLocation().add(0,target.getEyeHeight(), 0));
					} else {
						v2=new Vec2D(target.getEyeLocation().getYaw(),target.getEyeLocation().getPitch());
					}
					EntityGoogleMechanic.this.vh.rotateEntityPacket(caster.getBukkitEntity(),(float)v2.getX(),(float)v2.getY());
				}
				l++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);
		return true;
	}
	
	private void b(Boolean b) {
		this.b=b;
	}

}
