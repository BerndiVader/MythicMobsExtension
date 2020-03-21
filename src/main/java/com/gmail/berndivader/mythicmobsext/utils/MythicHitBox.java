package com.gmail.berndivader.mythicmobsext.utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;

public class MythicHitBox {
    AbstractWorld world;
    double lowX;
    double lowY;
    double lowZ;
    double highX;
    double highY;
    double highZ;

    public MythicHitBox(AbstractLocation center, double radius) {
        this(center, radius, radius);
    }

    public MythicHitBox(AbstractLocation center, double horizRadius, double vertRadius) {
        this.world = center.getWorld();
        this.lowX = center.getX() - horizRadius;
        this.lowY = center.getY() - vertRadius;
        this.lowZ = center.getZ() - horizRadius;
        this.highX = center.getX() + horizRadius;
        this.highY = center.getY() + vertRadius;
        this.highZ = center.getZ() + horizRadius;
    }

    public boolean contains(AbstractLocation location) {
        if (!location.getWorld().equals(this.world)) {
            return false;
        }
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return this.lowX <= x && x <= this.highX && this.lowY <= y && y <= this.highY && this.lowZ <= z && z <= this.highZ;
    }

    public boolean contains(AbstractEntity entity) {
        return this.contains(entity.getLocation());
    }
}

