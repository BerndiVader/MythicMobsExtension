package com.gmail.berndivader.mythicmobsext.NMS;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.CompatibilityUtils;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.Vec3D;

import io.lumine.xikage.mythicmobs.drops.Drop;

public class NMSUtils extends CompatibilityUtils {

	protected static Class<?> class_IChatBaseComponent_ChatSerializer;
	protected static Class<?> class_EntitySnowman;

	protected static Class<?> class_Drop;

	protected static Class<?> class_PathfinderGoalSelector_PathfinderGoalSelectorItem;
	protected static Class<?> class_IInventory;
	protected static Class<?> class_CraftInventory;
	protected static Class<?> class_CraftInventoryCustom;

	protected static Class<?> class_MetadataStoreBase;

	protected static Field class_Entity_lastXField;
	protected static Field class_Entity_lastYField;
	protected static Field class_Entity_lastZField;
	protected static Field class_Entity_lastPitchField;
	protected static Field class_Entity_lastYawField;
	protected static Field class_MinecraftServer_currentTickField;
	protected static Field class_Entity_fireProof;
	protected static Field class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PathfinderField;
	protected static Field class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PriorityField;
	protected static Field class_MetadataStoreBase_metadataMapField;

	protected static Method class_Entity_getFlagMethod;
	protected static Method class_IChatBaseComponent_ChatSerializer_aMethod;
	protected static Method class_PathfinderGoalSelector_PathfinderGoalSelectorItem_equalsMethod;
	protected static Method class_EntityCreature_setGoalTargetMethod;
	protected static Method class_EntityPlayer_clearActiveItemMethod;
	protected static Method class_EntityLiving_getArmorStrengthMethod;
	protected static Method class_EntitySnowman_setHasPumpkinMethod;
	protected static Method class_EntityLiving_getArrowCountMethod;
	protected static Method class_EntityLiving_setArrowCountMethod;

	protected static Method class_Drop_getDropMethod;

	protected static Method class_CraftServer_getEntityMetadataStoreMethod;
	protected static Method class_CraftServer_getPlayerMetadataStoreMethod;

	public static boolean initialize() {
		boolean bool = com.gmail.berndivader.mythicmobsext.compatibilitylib.NMSUtils.initialize(Main.getPlugin().getLogger());
		try {
			class_IChatBaseComponent_ChatSerializer = fixBukkitClass("IChatBaseComponent$ChatSerializer", true, "network.chat");
			class_EntitySnowman = fixBukkitClass("EntitySnowman", true, "world.entity.animal");
			class_Drop = fixBukkitClass("io.lumine.xikage.mythicmobs.drops.Drop");
			class_PathfinderGoalSelector_PathfinderGoalSelectorItem = fixBukkitClass(Utils.serverV < 14 ?
							"PathfinderGoalSelector$PathfinderGoalSelectorItem" : "PathfinderGoalWrapped",
					true, "world.entity.ai.goal");
			class_IInventory = fixBukkitClass("IInventory", true, "world");
			class_CraftInventory = fixBukkitClass("inventory.CraftInventory", false);
			class_MetadataStoreBase = fixBukkitClass("org.bukkit.metadata.MetadataStoreBase");

			class_Entity_lastXField = class_Entity.getDeclaredField(version < 17 ? "lastX" : "u"); // 1.17 : xo
			class_Entity_lastXField.setAccessible(true);
			class_Entity_lastYField = class_Entity.getDeclaredField(version < 17 ? "lastY" : "v"); // 1.17 : yo
			class_Entity_lastYField.setAccessible(true);
			class_Entity_lastZField = class_Entity.getDeclaredField(version < 17 ? "lastZ" : "w"); // 1.17 : zo
			class_Entity_lastZField.setAccessible(true);
			class_Entity_lastPitchField = class_Entity.getDeclaredField(version < 17 ? "lastPitch" : "x"); // 1.17 : yRotO
			class_Entity_lastPitchField.setAccessible(true);
			class_Entity_lastYawField = class_Entity.getDeclaredField(version < 17 ? "lastYaw" : "y"); // 1.17 : xRotO
			class_Entity_lastYawField.setAccessible(true);

			if (Utils.serverV < 14) {
				class_Entity_fireProof = class_Entity.getDeclaredField("fireProof");
				class_Entity_fireProof.setAccessible(true);
			}
			class_MetadataStoreBase_metadataMapField = class_MetadataStoreBase.getDeclaredField("metadataMap");
			class_MetadataStoreBase_metadataMapField.setAccessible(true);

			class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PathfinderField = class_PathfinderGoalSelector_PathfinderGoalSelectorItem
					.getDeclaredField("a");
			class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PathfinderField.setAccessible(true);
			class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PriorityField = class_PathfinderGoalSelector_PathfinderGoalSelectorItem
					.getDeclaredField("b");
			class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PriorityField.setAccessible(true);

			class_MinecraftServer_currentTickField = class_MinecraftServer.getDeclaredField("currentTick");
			class_MinecraftServer_currentTickField.setAccessible(true);

			class_Entity_getFlagMethod = class_Entity.getMethod(version < 18 ? "getFlag" : "h", Integer.TYPE);
			class_IChatBaseComponent_ChatSerializer_aMethod = class_IChatBaseComponent_ChatSerializer.getMethod("a",
					String.class);
			class_EntityCreature_setGoalTargetMethod = class_EntityCreature.getMethod(version < 18 ? "setGoalTarget" : "setTarget",
					class_EntityLiving, TargetReason.class, Boolean.TYPE);
			class_EntityPlayer_clearActiveItemMethod = class_EntityPlayer.getMethod(version < 18 ? "clearActiveItem" : "eR");
			class_EntityLiving_getArmorStrengthMethod = class_EntityLiving.getMethod(version < 18 ? "getArmorStrength" : "ei");
			class_EntitySnowman_setHasPumpkinMethod = class_EntitySnowman.getMethod(version < 18 ? "setHasPumpkin" : "v", Boolean.TYPE);
			class_EntityLiving_getArrowCountMethod = class_EntityLiving.getMethod(version < 18 ? "getArrowCount" : "em");
			class_EntityLiving_setArrowCountMethod = class_EntityLiving.getMethod("setArrowCount", Integer.TYPE, Boolean.TYPE);
			class_PathfinderGoalSelector_PathfinderGoalSelectorItem_equalsMethod = class_PathfinderGoalSelector_PathfinderGoalSelectorItem
					.getMethod("equals", Object.class);

			class_CraftServer_getEntityMetadataStoreMethod = class_CraftServer.getMethod("getEntityMetadata");
			class_CraftServer_getPlayerMetadataStoreMethod = class_CraftServer.getMethod("getPlayerMetadata");

			class_Drop_getDropMethod = class_Drop.getMethod("getDrop", String.class, String.class);


		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return bool;
	}

	/**
	 * 
	 * @param object {@link PathfinderGoalSelector}
	 * @return priority {@link Integer}
	 */
	public static int getPathfinderGoalSelectorItemPriority(Object object) {
		int priority = -1;
		try {
			priority = class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PriorityField.getInt(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return priority;
	}

	/**
	 * 
	 * @param object {@link PathfinderGoalSelector}
	 * @return object {@link PathfinderGoal}
	 */
	public static Object getPathfinderGoalFromPathFinderSelectorItem(Object object) {
		Object pathfindergoal = null;
		try {
			pathfindergoal = class_PathfinderGoalSelector_PathfinderGoalSelectorItem_PathfinderField.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathfindergoal;
	}

	/**
	 * @param entity {@link Entity}
	 * @return motion vector - {@link Vec3D}
	 */
	public static Vec3D getEntityLastMot(Entity entity) {
		Object o = getHandle(entity);
		Vec3D v3 = null;
		try {
			double dx = (double) class_Entity_motXField.get(o), dy = (double) class_Entity_motYField.get(o),
					dz = (double) class_Entity_motZField.get(o);
			v3 = new Vec3D(-dx, -dy, -dz);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v3;
	}

	/**
	 * @param entity {@link Entity}
	 * @return float - last yaw
	 */
	public static float getLastYawFloat(Entity entity) {
		try {
			return (float) class_Entity_lastYawField.get(getHandle(entity));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0f;
	}

	/**
	 * @param server {@link Server}
	 * @return int - ticks
	 */
	public static int getCurrentTick(Server server) {
		int i1 = 0;
		Object o1;
		try {
			if ((o1 = getHandle(server)) != null) {
				i1 = class_MinecraftServer_currentTickField.getInt(o1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i1;
	}

	/**
	 * @param s1  - fieldname {@link String}
	 * @param cl1 - class {@link Class}
	 * @param o1  - instance {@link Object}
	 * @param o2  - value {@link Object}
	 */
	public static void setFinalField(String s1, Class<?> cl1, Object o1, Object o2) {
		try {
			Field f1 = cl1.getDeclaredField(s1);
			f1.setAccessible(true);
			Field f2 = Field.class.getDeclaredField("modifiers");
			f2.setAccessible(true);
			f2.setInt(f1, f2.getModifiers() & ~Modifier.FINAL);
			f1.set(o1, o2);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param s1  - fieldname
	 * @param cl1 - class
	 * @param o1  - instance
	 * @return object {@link Object}
	 */
	public static Object getField(String s1, Class<?> cl1, Object o1) {
		Object o2 = null;
		try {
			Field f1 = cl1.getDeclaredField(s1);
			f1.setAccessible(true);
			o2 = f1.get(o1);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o2;
	}

	/**
	 * @param s1  - fieldname {@link String}
	 * @param cl1 - class {@link Class}
	 * @param o1  - instance {@link Object}
	 * @param o2  - value {@link Object}
	 */
	public static void setField(String s1, Class<?> cl1, Object o1, Object o2) {
		try {
			Field f1 = cl1.getDeclaredField(s1);
			f1.setAccessible(true);
			f1.set(o1, o2);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param instance {@link Object}
	 * @param name     - fieldname {@link String}
	 * @param value    {@link Object}
	 */
	public static void setField(Object instance, String name, Object value) {
		if (instance == null)
			return;
		Field field;
		try {
			field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param clazz    {@link Class}
	 * @param name     - fieldname {@link String}
	 * @param instance {@link Object}
	 * @return object {@link Object}
	 */
	public static Object getField(Class<?> clazz, String name, Object instance) {
		Object o1 = null;
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			o1 = field.get(instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return o1;
	}

	/**
	 * @param entity {@link Entity}
	 * @param flag   - <br>
	 *               <b>0</b>=burning,<br>
	 *               <b>1</b>=sneaking,<br>
	 *               <b>3</b>=sprinting,<br>
	 *               <b>4</b>=swimming,<br>
	 *               <b>5</b>=invisible,<br>
	 *               <b>6</b>=glowing
	 * @return boolean
	 */
	public static boolean getFlag(Entity entity, int flag) {
		boolean bl1 = false;
		try {
			bl1 = (boolean) class_Entity_getFlagMethod.invoke(getHandle(entity), flag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return bl1;
	}

	/**
	 * @param string {@link String}
	 * @return object
	 */
	public static Object getJSONfromString(String string) {
		Object o1 = null;
		try {
			o1 = class_IChatBaseComponent_ChatSerializer_aMethod.invoke(null, string);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return o1;
	}

	/**
	 * @param entity     {@link LivingEntity}
	 * @param target     {@link Creature}
	 * @param reason     {@link TargetReason}
	 * @param fire_event {@link Boolean}
	 */
	public static void setGoalTarget(Entity entity, Entity target, TargetReason reason, boolean fire_event) {
		try {
			class_EntityCreature_setGoalTargetMethod.invoke(getHandle(entity), getHandle(target), reason, fire_event);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param player {@link Player}
	 */
	public static void clearActiveItem(Player player) {
		try {
			class_EntityPlayer_clearActiveItemMethod.invoke(getHandle(player));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param entity {@link LivingEntity}
	 * @return int {@link Integer}
	 */
	public static int getArmorStrength(LivingEntity entity) {
		int armor;
		try {
			armor = (int) class_EntityLiving_getArmorStrengthMethod.invoke(getHandle(entity));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			armor = -1;
			e.printStackTrace();
		}
		return armor;
	}

	/**
	 * @param snowman {@link Snowman
	 * @param bool    {@link Boolean}
	 */
	public static void setSnowmanPumpkin(Snowman snowman, boolean bool) {
		try {
			class_EntitySnowman_setHasPumpkinMethod.invoke(getHandle(snowman), bool);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param entity {@link LivingEntity}
	 * @return int {@link Integer}
	 */
	public static int getArrowsOnEntity(LivingEntity entity) {
		int arrow_count;
		try {
			arrow_count = (int) class_EntityLiving_getArrowCountMethod.invoke(getHandle(entity));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			arrow_count = -1;
		}
		return arrow_count;
	}

	/**
	 * @param entity {@link LivingEntity}
	 * @param amount {@link Integer}
	 */
	public static void setArrowsOnEntity(LivingEntity entity, int amount) {
		try {
			class_EntityLiving_setArrowCountMethod.invoke(getHandle(entity), amount);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param entity {@link Entity}
	 * @param bool   {@link Boolean}
	 */
	public static void setFireProofEntity(Entity entity, boolean bool) {
		if (Utils.serverV > 13) {
			Main.logger.warning("nosunburn not avail for 14.x or heigher!");
			return;
		}
		try {
			class_Entity_fireProof.set(getHandle(entity), bool);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param item_name {@link String}
	 * @return drop {@link Drop}
	 */
	public static Drop getDrop(String item_name) {
		Drop drop = null;
		try {
			drop = (Drop) class_Drop_getDropMethod.invoke(null, "MMEDrop", item_name);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return drop;
	}

	/**
	 * @param entity {@link Entity}
	 * @return Map<String,Map<Plugin,MetadataValue>> {@link MetadataStoreBase}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<Plugin, MetadataValue>> getEntityMetadataMap(Server server) {
		Map<String, Map<Plugin, MetadataValue>> metadata_map = new HashMap<>();
		try {
			Object craft_server = server;
			Object entity_metadata_store = class_CraftServer_getEntityMetadataStoreMethod.invoke(craft_server);
			metadata_map = (Map<String, Map<Plugin, MetadataValue>>) class_MetadataStoreBase_metadataMapField
					.get(entity_metadata_store);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return metadata_map;
	}
	
	

	/**
	 * @param player {@link Player}
	 * @return Map<String,Map<Plugin,MetadataValue>> {@link MetadataStoreBase}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<Plugin, MetadataValue>> getPlayerMetadataMap(Entity entity) {
		Map<String, Map<Plugin, MetadataValue>> metadata_map = new HashMap<>();
		try {
			Object craft_server = entity.getServer();
			Object entity_metadata_store = class_CraftServer_getPlayerMetadataStoreMethod.invoke(craft_server);
			metadata_map = (Map<String, Map<Plugin, MetadataValue>>) class_MetadataStoreBase_metadataMapField
					.get(entity_metadata_store);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return metadata_map;
	}

	public static Object getTagValue(Object nbt) {
		try {
			return class_NBTTagString_dataField.get(nbt);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
