package com.gmail.berndivader.mythicmobsext.volatilecode.v1_12_R1.pathfindergoals;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.google.common.base.Predicate;

import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalTarget;
import net.minecraft.server.v1_12_R1.ScoreboardTeam;
import org.bukkit.event.entity.EntityTargetEvent;

public 
class
PathfinderGoalOtherTeams
<T extends EntityLiving> 
extends 
PathfinderGoalNearestAttackableTarget
<T>
{
	ScoreboardTeam team1,team2;
	int i;
	
    public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
        this(entitycreature, oclass, flag, false);
	}

    public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
        this(entitycreature, oclass, 10, flag, flag1, null);
	}
    
    public PathfinderGoalOtherTeams(EntityCreature entitycreature, Class<T> oclass, int i2, boolean flag, boolean flag1, Predicate<? super T> predicate) {
        super(entitycreature, oclass,i2,flag, flag1, predicate);
        this.i=i2;
	}
    
    public boolean a() {
        if (this.i>0&&this.e.getRandom().nextInt(this.i)!=0) return false;
        
        return true;
    }
    
    @Override
    public boolean b() {
        EntityLiving entityliving=this.e.getGoalTarget();
        if (entityliving==null) entityliving=this.g;
        if (entityliving==null||!entityliving.isAlive()) return false;
        teams(entityliving);
        if (this.team1!=null&&this.team1==this.team2) {
        	this.d();
        	return false;
        }
        double d0=this.i();
        if (this.e.h(entityliving)>d0*d0) return false;
        if (this.f) {
            if (this.e.getEntitySenses().a(entityliving)) {
            	NMSUtils.setField("d",PathfinderGoalTarget.class,(Object)this,0);
            } else {
            	int d=(int)NMSUtils.getField("d",PathfinderGoalTarget.class,(Object)this);
            	NMSUtils.setField("d",PathfinderGoalTarget.class,(Object)this,d++);
            	if(d++>this.h) return false;
            }
        }
        if (entityliving instanceof EntityHuman && ((EntityHuman)entityliving).abilities.isInvulnerable) {
            return false;
        }
        this.e.setGoalTarget(entityliving,EntityTargetEvent.TargetReason.CLOSEST_ENTITY,true);
        return true;
    }
    
    private void teams(EntityLiving entityliving) {
		this.team1=this.e.world.scoreboard.getPlayerTeam(this.e.getUniqueID().toString());
		this.team2=entityliving instanceof EntityPlayer?this.e.world.scoreboard.getPlayerTeam(entityliving.getName()):this.e.world.scoreboard.getPlayerTeam(entityliving.getUniqueID().toString());
    }


}