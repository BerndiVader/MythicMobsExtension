package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.navigation;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.NavigationAbstract;
import net.minecraft.server.v1_13_R2.PathType;
import net.minecraft.server.v1_13_R2.PathfinderAbstract;

public class ControllerFly
extends
ControllerMove {
    public ControllerFly(EntityInsentient entityInsentient) {
        super(entityInsentient);
        a.getAttributeMap().b(GenericAttributes.e);
        a.getAttributeInstance(GenericAttributes.e).setValue(2);
    }

    @Override
    public void a() {
        if (this.h == Operation.STRAFE) {
            PathfinderAbstract pathfinderAbstract;
            float f2=(float)this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
            float f3=(float)this.e*f2;
            float f4=this.f;
            float f5=this.g;
            float f6=MathHelper.c(f4*f4+f5*f5);
            if (f6<1.0f) f6=1.0f;
            f6=f3/f6;
            float f7=MathHelper.sin(this.a.yaw*0.017453292f);
            float f8=MathHelper.cos(this.a.yaw*0.017453292f);
            float f9=(f4*=f6)*f8-(f5*=f6)*f7;
            float f10=f5*f8+f4*f7;
            NavigationAbstract navigationAbstract=this.a.getNavigation();
            if (navigationAbstract!=null&&(pathfinderAbstract=navigationAbstract.s())!=null&&pathfinderAbstract.a(this.a.world,MathHelper.floor(this.a.locX+(double)f9),MathHelper.floor(this.a.locY),MathHelper.floor(this.a.locZ+(double)f10))!=PathType.WALKABLE) {
                this.f=1.0f;
                this.g=0.0f;
                f3=f2;
            }
            this.a.o(f3);
            this.a.r(this.f);
            this.a.t(this.g);
            this.h = Operation.WAIT;
        } else if (this.h==ControllerMove.Operation.MOVE_TO) {
            this.h=ControllerMove.Operation.WAIT;
            this.a.setNoGravity(true);
            double d2=this.b-this.a.locX;
            double d3=this.c-this.a.locY;
            double d4=this.d-this.a.locZ;
            double d5=d2*d2+d3*d3+d4*d4;
            if (d5<2.500000277905201E-7) {
                this.a.s(0.0f);
                this.a.r(0.0f);
                return;
            }
            float f2=(float)(MathHelper.c(d4,d2)*57.2957763671875)-90.0f;
            this.a.yaw=this.a(this.a.yaw,f2,10.0f);
            float f3=this.a.onGround?(float)(this.e*this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()):(float)(this.e*this.a.getAttributeInstance(GenericAttributes.e).getValue());
            this.a.o(f3);
            double d6=MathHelper.sqrt(d2*d2+d4*d4);
            float f4=(float)(-MathHelper.c(d3,d6)*57.2957763671875);
            this.a.pitch=this.a(this.a.pitch,f4,10.0f);
            this.a.s(d3>0.0?f3:-f3);
        } else {
            this.a.setNoGravity(false);
            this.a.s(0.0f);
            this.a.r(0.0f);
        }
    }
}
