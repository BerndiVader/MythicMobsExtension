package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.pathfindergoals;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;

import java.util.Optional;

import org.bukkit.entity.Monster;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public 
class 
PathfinderGoalAttack
extends
PathfinderGoalMeleeAttack 
{
	protected float r;
	boolean is_monster;
	Optional<ActiveMob>am;
	
    public PathfinderGoalAttack(EntityCreature e,double d,boolean b,float r) {
		super(e,d,b);
		is_monster=e.getBukkitEntity() instanceof Monster;
		this.r=r;
		am=Utils.mobmanager.getActiveMob(e.getUniqueID());
	}
    
    @Override
    protected void a(EntityLiving entityLiving,double d2) {

    	if(am.isPresent()) {
    		ActiveMob active_mob=am.get();
    		if(active_mob.getOwner().isPresent()) {
    			if(active_mob.getOwner().get()==entityLiving.getUniqueID()) return;
    		}
    	}
    	
        double d3 = this.a(entityLiving);
        if (d2 <= d3 && this.b <= 0) {
            this.b = 20;
            if(is_monster) {
                this.a.a(EnumHand.MAIN_HAND);
                this.a.B(entityLiving);
            }
        	ActiveMob am=Utils.mobmanager.getMythicMobInstance(this.a.getBukkitEntity());
        	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),Utils.signal_AIHIT);
        }
    }
    
	@Override
	protected double a(EntityLiving e) {
	    return (double)(this.a.width*this.r*this.a.width*this.r+e.width);
	}
}	
