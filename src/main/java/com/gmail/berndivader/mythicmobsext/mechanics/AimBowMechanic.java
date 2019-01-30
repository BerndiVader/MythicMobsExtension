package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="bowaimbot",author="BerndiVader")
public 
class 
AimBowMechanic 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	boolean aim_nearest;
	static String meta_str;
	
	static {
		meta_str="MMEAIMBOT";
	}

	public AimBowMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		aim_nearest=mlc.getBoolean("aimnearest",false);
	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity a_target) {
		if(data.getCaster().getEntity().isPlayer()&&a_target.isLiving()) {
			final Player player=(Player)data.getCaster().getEntity().getBukkitEntity();
			
			if(!player.hasMetadata(meta_str)) {
				final LivingEntity target=(LivingEntity)a_target.getBukkitEntity();
				player.setMetadata(meta_str,new FixedMetadataValue(Main.getPlugin(),true));
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if(player==null||player.isDead()||!player.isOnline()||!player.isHandRaised()||!player.hasMetadata(meta_str)) {
							if(player.hasMetadata(meta_str)) player.removeMetadata(meta_str,Main.getPlugin());
							this.cancel();
						} else {
							float velocity=Utils.getBowTension(player);
							if(velocity>0.1f) {
								Vec3D target_position=Volatile.handler.getPredictedMotion(player,target,5.0f);
								double x=target_position.getX();
								double y=target_position.getY();
								double z=target_position.getZ();
								
								float yaw=(float)(Math.atan2(z,x)*180/Math.PI)-90;
								double distance=Math.sqrt(x*x+z*z);
								float G=0.006f;
								float pitch=(float)-Math.toDegrees(
										Math.atan((velocity*velocity
												-Math.sqrt(
														(float)(velocity*velocity*velocity
																*velocity-G*(G*(distance*distance)
																		+2*y*(velocity*velocity)))))
												/(G*distance)));
								if(!Float.isNaN(pitch)&&!Float.isNaN(yaw)) Volatile.handler.playerConnectionLookAt(player, yaw, pitch);
							}
						}
					}
				}.runTaskTimer(Main.getPlugin(),1l,1l);
			}
			return true;
		}
		return false;
	}
	
	

}
