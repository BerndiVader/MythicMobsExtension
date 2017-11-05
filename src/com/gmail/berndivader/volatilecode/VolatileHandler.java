package com.gmail.berndivader.volatilecode;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

public interface VolatileHandler {

    public void aiPathfinderGoal(LivingEntity livingEntity, String goalList, LivingEntity target);
    public boolean inMotion(LivingEntity entity);
    public void teleportEntityPacket(Entity entity);
    public void forceSetPositionRotation(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f);
    public void playerConnectionSpin(Entity entity,float s);
    void setItemMotion(Item i, Location ol, Location nl);
	void sendArmorstandEquipPacket(ArmorStand entity);
	void changeHitBox(Entity entity, double a0, double a1, double a2);
}
