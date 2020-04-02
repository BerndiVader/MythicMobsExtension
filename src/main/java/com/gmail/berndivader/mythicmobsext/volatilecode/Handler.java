package com.gmail.berndivader.mythicmobsext.volatilecode;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;

public interface Handler {
	public void aiPathfinderGoal(LivingEntity livingEntity, String goalList, LivingEntity target);

	public boolean inMotion(LivingEntity entity);

	public void teleportEntityPacket(Entity entity);

	public void forceSetPositionRotation(Entity entity, double x, double y, double z, float yaw, float pitch, boolean f,
			boolean g);

	public void playerConnectionSpin(Entity entity, float s);

	void setItemMotion(Item i, Location ol, Location nl);

	void sendArmorstandEquipPacket(ArmorStand entity);

	void changeHitBox(Entity entity, double a0, double a1, double a2);

	void playerConnectionLookAt(Entity entity, float yaw, float pitch);

	void moveEntityPacket(Entity entity, Location ol, double x, double y, double z);

	void rotateEntityPacket(Entity entity, float y, float p);

	void forceSpectate(Player player, Entity entity);

	void playEndScreenForPlayer(Player player, float f);

	boolean playerIsSleeping(Player p);

	boolean playerIsRunning(Player p);

	boolean playerIsCrouching(Player p);

	boolean playerIsJumping(Player p);

	void forceCancelEndScreenPlayer(Player player);

	void fakeEntityDeath(Entity entity, long d);

	void setDeath(Player p, boolean b);

	boolean testForCondition(Entity e, String ns, char m);

	float getItemCoolDown(Player p, int i1);

	public boolean setItemCooldown(Player p, int j1, int i1);

	void setFieldOfViewPacketSend(Player player, float f1);

	float getIndicatorPercentage(Player p);

	Parrot spawnCustomParrot(Location l1, boolean b1);

	boolean getNBTValueOf(Entity e1, String s1, boolean b1);

	void moveto(LivingEntity entity);

	boolean addNBTTag(Entity e1, String s);

	void aiTargetSelector(LivingEntity entity, String uGoal, LivingEntity target);

	void setMNc(LivingEntity e1, String s1);

	void forceBowDraw(LivingEntity e1, LivingEntity target, boolean bl1);

	void forceSpectate(Player player, Entity entity, boolean bl1);

	void changeResPack(Player p, String url, String hash);

	LivingEntity spawnCustomZombie(Location location, boolean sunBurn);

	void playBlockBreak(int eid, Location location, int stage);

	void playAnimationPacket(LivingEntity e, int id);

	void playAnimationPacket(LivingEntity e, Integer[] ints);

	void setWorldborder(Player p, int density, boolean play);

	Vec3D getPredictedMotion(LivingEntity bukkit_source, LivingEntity bukkit_target, float delta);

	boolean velocityChanged(Entity bukkit_entity);

	void sendPlayerAdvancement(Player player, Material material, String title, String description, String task);

	boolean isReachable1(LivingEntity bukkit_entity, LivingEntity bukkit_target);

	void addTravelPoint(Entity bukkit_entity, Vec3D vector);

	void addTravelPoint(Entity bukkit_entity, Vec3D vector, boolean remove);

	void clearTravelPoints(Entity bukkit_entity);

	List<Entity> getNearbyEntities(Entity bukkit_entity, int range);

	List<Entity> getNearbyEntities(Entity bukkit_entity, int range, Predicate<Entity> filter);

	List<Entity> getNearbyEntities(World bukkit_world, BoundingBox bukkit_aabb, Predicate<Entity> filter);

	List<Entity> getNearbyEntities(World bukkit_world, Location bukkit_location, double x, double y, double z,
			Predicate<Entity> filter);

}
