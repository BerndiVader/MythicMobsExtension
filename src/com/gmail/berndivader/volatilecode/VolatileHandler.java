package com.gmail.berndivader.volatilecode;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface VolatileHandler {

    public void aiPathfinderGoal(LivingEntity livingEntity, String goalList, LivingEntity target);
    public boolean inMotion(LivingEntity entity);
    public void teleportEntityPacket(Entity entity);
    public void forceSetPositionRotation(Entity entity,double x,double y,double z,float yaw,float pitch,boolean f,boolean g);
    public void playerConnectionSpin(Entity entity,float s);
    void setItemMotion(Item i, Location ol, Location nl);
	void sendArmorstandEquipPacket(ArmorStand entity);
	void changeHitBox(Entity entity, double a0, double a1, double a2);
	void playerConnectionLookAt(Entity entity, float yaw, float pitch);
	void moveEntityPacket(Entity entity,Location ol, double x, double y, double z);
	void rotateEntityPacket(Entity entity, float y, float p);
	void forceSpectate(Player player, Entity entity);
	void playEndScreenForPlayer(Player player,float f);
	boolean playerIsSleeping(Player p);
	boolean playerIsRunning(Player p);
	boolean playerIsCrouching(Player p);
	boolean playerIsJumping(Player p);
	void forceCancelEndScreenPlayer(Player player);
	void fakeEntityDeath(Entity entity,long d);
	int arrowsOnEntity(Entity entity);
	void removeArrowsFromEntity(Entity entity, int a, char c);
}
