package com.gmail.berndivader.mythicmobsext.volatilecode.v1_14_R1.pathfindergoals;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PathfinderGoalMeleeAttack;

import org.bukkit.entity.Monster;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public 
class
PathfinderGoalAttack
extends
PathfinderGoalMeleeAttack {
	protected float r;
	boolean is_monster;
	
    public PathfinderGoalAttack(EntityCreature e,double d,boolean b,float r) {
		super(e,d,b);
		is_monster=e.getBukkitEntity() instanceof Monster;
		this.r=r;
	}
    
    @Override
    protected void a(EntityLiving entityLiving,double d2) {
        double d3 = this.a(entityLiving);
        if (d2 <= d3 && this.b <= 0) {
            this.b = 20;
            if(is_monster) {
                this.a.a(EnumHand.MAIN_HAND);
                this.a.C(entityLiving);
            }
        	ActiveMob am=Utils.mobmanager.getMythicMobInstance(this.a.getBukkitEntity());
        	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),Utils.signal_AIHIT);
        }
    }
    
    @Override
	protected double a(EntityLiving e) {
	    return (double)(this.a.getWidth()*this.r*this.a.getWidth()*this.r+e.getWidth());
	}
}	
