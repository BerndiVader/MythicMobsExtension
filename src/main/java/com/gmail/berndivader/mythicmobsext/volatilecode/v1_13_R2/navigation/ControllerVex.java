package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.navigation;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.MathHelper;

public class ControllerVex
extends 
ControllerMove {
	public ControllerVex(EntityInsentient monster) {
		super(monster);
	}

    @Override
    public void a() {
        if (this.h==ControllerMove.Operation.MOVE_TO) {
        	this.a.setNoGravity(true);
            double d0=this.b-this.a.locX;
            double d1=this.c-this.a.locY;
            double d2=this.d-this.a.locZ;
            double d3=d0*d0+d1*d1+d2*d2;
            if ((d3=(double)MathHelper.sqrt(d3))<this.a.getBoundingBox().a()) {
                this.h=ControllerMove.Operation.WAIT;
                a.motX*=0.5;
                a.motY*=0.5;
                a.motZ*=0.5;
            } else {
            	a.motX+=d0/d3*0.05*this.e;
            	a.motY+=d1/d3*0.05*this.e;
            	a.motZ+=d2/d3*0.05*this.e;
                if (a.getGoalTarget()==null) {
                	a.aQ=a.yaw=(-(float)MathHelper.c(a.motX,a.motZ))*57.295776f;
                } else {
                    double d4=a.getGoalTarget().locX-a.locX;
                    double d5=a.getGoalTarget().locZ-a.locZ;
                    a.aQ=a.yaw=(-(float)MathHelper.c(d4,d5))*57.295776f;
                }
            }
        } else {
            this.a.setNoGravity(false);
            this.a.s(0.0f);
            this.a.r(0.0f);
        }
    }
}
