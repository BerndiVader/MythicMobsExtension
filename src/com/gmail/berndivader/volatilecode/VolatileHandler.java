package com.gmail.berndivader.volatilecode;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface VolatileHandler {

    public void aiPathfinderGoal(LivingEntity livingEntity, String goalList, LivingEntity target);
    public boolean inMotion(LivingEntity entity);
    public void teleportEntityPacket(Entity entity);
    public void setMotion(Entity entity);
	
}
