package com.gmail.berndivader.mythicmobsext.volatilecode.v1_15_R1.pathfindergoals;

import java.util.EnumSet;

import com.gmail.berndivader.mythicmobsext.Main;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.PathfinderGoal;

public class 
PathfinderGoalVexD
extends
PathfinderGoal {
	
	EntityInsentient entity;
	BlockPosition c;
	
    public PathfinderGoalVexD(EntityInsentient monster) {
    	this.entity=monster;
		a(EnumSet.of(PathfinderGoal.Type.MOVE));
    }

    @Override
    public boolean a() {
        if (!entity.getControllerMove().b()&&Main.random.nextInt(7)==0) return true;
        return false;
    }

    @Override
    public boolean b() {
        return false;
    }

    @Override
    public void e() {
        BlockPosition blockposition=this.c;
        if (blockposition==null) blockposition = new BlockPosition(entity);
        int i2=0;
        while (i2<3) {
            BlockPosition blockposition1=blockposition.a((double)Main.random.nextInt(15)-7,(double)Main.random.nextInt(11)-5,(double)Main.random.nextInt(15)-7);
            if (entity.world.isEmpty(blockposition1)) {
                entity.getControllerMove().a((double)blockposition1.getX()+0.5,(double)blockposition1.getY()+0.5,(double)blockposition1.getZ()+0.5,0.25);
                if (entity.getGoalTarget()!=null)break;
                entity.getControllerLook().a((double)blockposition1.getX()+0.5,(double)blockposition1.getY()+0.5,(double)blockposition1.getZ()+0.5,180.0f,20.0f);
                break;
            }
            ++i2;
        }
    }
}
