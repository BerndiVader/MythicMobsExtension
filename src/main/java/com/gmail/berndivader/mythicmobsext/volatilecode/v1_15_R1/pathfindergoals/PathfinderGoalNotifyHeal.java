package com.gmail.berndivader.mythicmobsext.volatilecode.v1_15_R1.pathfindergoals;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.PathfinderGoal;

public
class 
PathfinderGoalNotifyHeal
extends
PathfinderGoal 
{
	static final String tag="mme_last_health";
	long time_stamp;
	String signal;
    LivingEntity entity;
    Optional<ActiveMob>am;
    float old_health,new_health;

    public PathfinderGoalNotifyHeal(EntityInsentient entity,String signal) {
        this.entity=(LivingEntity)entity.getBukkitEntity();
        this.am=Optional.ofNullable(Utils.mobmanager.getMythicMobInstance(this.entity));
        this.signal=signal;
        old_health=entity.getHealth();
        time_stamp=System.currentTimeMillis();
    }

    @Override
    public boolean a() {
       	return old_health!=(float)entity.getHealth();
    }
    
    @Override
    public boolean b() {
     	return old_health!=(float)entity.getHealth()&&System.currentTimeMillis()-time_stamp<40;
    }

    @Override
    public void d() {
    }
    
    @Override
    public void e() {
    	old_health=(float)entity.getHealth();
    }

    @Override
	public void c() {
    	time_stamp=System.currentTimeMillis();
    }
}