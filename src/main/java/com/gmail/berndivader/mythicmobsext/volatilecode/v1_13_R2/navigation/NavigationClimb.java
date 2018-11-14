package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2.navigation;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.PathEntity;
import net.minecraft.server.v1_13_R2.World;

public
class
NavigationClimb
extends
Navigation 
{
    private BlockPosition i;

    public NavigationClimb(EntityInsentient entityInsentient, World world) {
        super(entityInsentient, world);
    }

    @Override
    public PathEntity b(BlockPosition blockPosition) {
        this.i = blockPosition;
        return super.b(blockPosition);
    }

    @Override
    public PathEntity a(Entity entity) {
        this.i = new BlockPosition(entity);
        return super.a(entity);
    }

    @Override
    public boolean a(Entity entity, double d2) {
        PathEntity pathEntity = this.a(entity);
        if (pathEntity != null) {
            return this.a(pathEntity, d2);
        }
        this.i = new BlockPosition(entity);
        this.d = d2;
        return true;
    }

    @Override
    public void d() {
        if (this.p()) {
            if (this.i != null) {
                double d2 = this.a.width * this.a.width;
                if (this.a.d(this.i) < d2 || this.a.locY > (double)this.i.getY() && this.a.d(new BlockPosition(this.i.getX(), MathHelper.floor(this.a.locY), this.i.getZ())) < d2) {
                    this.i = null;
                } else {
                    this.a.getControllerMove().a(this.i.getX(), this.i.getY()+1d, this.i.getZ(), this.d);
                }
            }
            return;
        }
        super.d();
    }
}

