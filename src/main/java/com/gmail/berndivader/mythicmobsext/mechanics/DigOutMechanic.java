package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ParticleMaker;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="digout",author="BerndiVader")
public 
class 
DigOutMechanic
extends
SkillMechanic 
implements
INoTargetSkill 
{
	long speed;
	int particle_amount;
	
	public DigOutMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		speed=(long)mlc.getInteger("speed",5);
		particle_amount=mlc.getInteger("amount",5);
	}
	
	@Override
	public boolean cast(SkillMetadata data) {
		if(!data.getCaster().getEntity().isLiving()) return false;
		final LivingEntity entity=(LivingEntity)data.getCaster().getEntity().getBukkitEntity();
		
		Location particle_location=entity.getLocation().getBlock().getLocation();
		Location location=particle_location.clone().add(0.5,0,0.5);
		Block block=location.getBlock().getRelative(BlockFace.DOWN);
		if(block.isLiquid()||block.getType()==Material.AIR) return false;
		
		final String particle_name="blockcrack_"+block.getTypeId()+"_"+block.getData();
		
		boolean gravity=entity.hasGravity();
		boolean invulnerable=entity.isInvulnerable();
		NMSUtils.setEntityMotion(entity,new Vector(0,0,0));
		NMSUtils.setInvulnerable(entity,true);
		entity.setInvulnerable(true);
		entity.setGravity(false);
		final double y=entity.getHeight()+0.5d;
		final double y_step=y/10;
		location.subtract(0,y,0);
		final double x=location.getX();
		final double z=location.getZ();
		entity.teleport(location);
		
		new BukkitRunnable() {
			int stage=0;
			double delta_x,delta_z;
			@Override
			public void run() {
				Volatile.handler.playBlockBreak(entity.getEntityId(),block.getLocation(),stage);
				delta_x=Main.random.nextDouble();
				delta_z=Main.random.nextDouble();
				new ParticleMaker.ParticlePacket(particle_name, 0, 0, 0, 0, particle_amount, true).sendAsync(particle_location.clone().add(delta_x,0.1,delta_z),512);
				if(stage>9) {
					NMSUtils.setInvulnerable(entity,invulnerable);
					entity.setGravity(gravity);
					this.cancel();
				}
				Location l=entity.getLocation();
				l.setX(x);
				l.setZ(z);
				l.add(0,y_step,0);
				entity.teleport(l);
				stage++;
			}
		}.runTaskTimer(Main.getPlugin(),1l,speed);
		
		return true;
	}
}
