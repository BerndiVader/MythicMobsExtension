package com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.IRangedEntity;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.PathfinderGoal;

public class PathFinderGoalShoot extends PathfinderGoal {
    private final EntityInsentient a;
    private final double b;
    private int c,h1;
    private final float d,i1;
    private int d1 = -1;
    private int f;
    private boolean g;
    private boolean h;
    private int i = -1;

    public PathFinderGoalShoot(EntityInsentient t, double d2, int n,int n1, float f2) {
        this.a=t;
        this.b=d2;
        this.c=n;
        this.h1=n1;
        this.d = f2 * f2;
        this.i1=f2;
        this.a(3);
    }

    public void b(int n) {
        this.c = n;
    }

    @Override
    public boolean a() {
        if (this.a.getGoalTarget() == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean b() {
        return (this.a() || !this.a.getNavigation().n());
    }

    @Override
    public void c() {
        super.c();
    }

    @Override
    public void d() {
        super.d();
        this.f = 0;
        this.d1 = -1;
        this.a.cE();
    }

    @Override
    public void e() {
        boolean bl;
        EntityLiving entityLiving = this.a.getGoalTarget();
        if (entityLiving == null) {
            return;
        }
        double d2 = this.a.e(entityLiving.locX, entityLiving.getBoundingBox().b, entityLiving.locZ);
        boolean bl2 = this.a.getEntitySenses().a(entityLiving);
        boolean bl3 = bl = this.f > 0;
        if (bl2 != bl) {
            this.f = 0;
        }
        this.f = bl2 ? ++this.f : --this.f;
        if (d2 > (double)this.d || this.f < 20) {
            this.a.getNavigation().a(entityLiving, this.b);
            this.i = -1;
        } else {
            this.a.getNavigation().o();
            ++this.i;
        }
        if (this.i >= 20) {
            if ((double)this.a.getRandom().nextFloat() < 0.3) {
                boolean bl4 = this.g = !this.g;
            }
            if ((double)this.a.getRandom().nextFloat() < 0.3) {
                this.h = !this.h;
            }
            this.i = 0;
        }
        if (this.i > -1) {
            if (d2 > (double)(this.d * 0.75f)) {
                this.h = false;
            } else if (d2 < (double)(this.d * 0.25f)) {
                this.h = true;
            }
            this.a.getControllerMove().a(this.h ? -0.5f : 0.5f, this.g ? 0.5f : -0.5f);
            this.a.a(entityLiving, 30.0f, 30.0f);
        } else {
            this.a.getControllerLook().a(entityLiving, 30.0f, 30.0f);
        }
        
        if (--this.d1 == 0) {
            float f2;
            if (!bl2) {
                return;
            }
            float f3 = f2 = MathHelper.sqrt(d2) / this.i1;
            f3 = MathHelper.a(f3, 0.1f, 1.0f);
            if (this.a instanceof IRangedEntity) {
	            ((IRangedEntity)this.a).a(entityLiving, f3);
            } else {
            	ActiveMob am=Utils.mobmanager.getMythicMobInstance(this.a.getBukkitEntity());
            	if (am!=null) am.signalMob(BukkitAdapter.adapt(entityLiving.getBukkitEntity()),"AISHOOT");
            }
            this.d1 = MathHelper.d(f2 * (float)(this.h1 - this.c) + (float)this.c);
        } else if (this.d1 < 0) {
            float f4 = MathHelper.sqrt(d2) / this.i1;
            this.d1 = MathHelper.d(f4 * (float)(this.h1 - this.c) + (float)this.c);
        }
    }
}
