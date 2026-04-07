package com.gmail.berndivader.mythicmobsext.compatibilitylib;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.CaseFormat;

/**
 * Contains some raw methods for doing some simple NMS utilities.
 * 
 * This is not meant to be a replacement for full-on NMS or Protocol libs,
 * but it is enough for Magic to use internally without requiring any
 * external dependencies.
 * 
 * Use any of this at your own risk!
 */
@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class NMSUtils {
    protected static boolean failed = false;
    protected static boolean legacy = false;
    protected static boolean isModernVersion = false;
    protected static boolean isCurrentVersion = false;
    protected static boolean hasStatistics = false;
    protected static boolean hasEntityTransformEvent = false;
    protected static boolean hasTimeSkipEvent = false;

    protected static String versionPrefix = "";
    protected static int version = -1;
    protected static int versionMinor = -1;
    protected static String npack = "";
    protected static String cpack = "";

    protected final static int NBT_TYPE_COMPOUND = 10;
    protected final static int NBT_TYPE_INT_ARRAY= 11;
    protected final static int NBT_TYPE_INTEGER = 3;
    protected final static int NBT_TYPE_FLOAT = 5;
    protected final static int NBT_TYPE_DOUBLE = 6;
    protected final static int NBT_TYPE_STRING = 8;

    protected static int WITHER_SKULL_TYPE = 66;
    protected static int FIREWORK_TYPE = 76;

    private static Logger logger;

    protected static Class<?> class_Block;
    protected static Class<?> class_BlockBase;
    protected static Class<?> class_ItemStack;
    protected static Class<?> class_NBTBase;
    protected static Class<?> class_NBTTagCompound;
    protected static Class<?> class_NBTTagList;
    protected static Class<?> class_NBTTagByte;
    protected static Class<?> class_NBTTagDouble;
    protected static Class<?> class_NBTTagFloat;
    protected static Class<?> class_NBTTagInt;
    protected static Class<?> class_NBTTagLong;
    protected static Class<?> class_NBTTagShort;
    protected static Class<?> class_NBTTagString;
    protected static Class<?> class_CraftTask;
    protected static Class<?> class_CraftInventoryCustom;
    protected static Class<?> class_CraftItemStack;
    protected static Class<?> class_CraftBlockState;
    protected static Class<?> class_CraftBlock;
    protected static Class<?> class_CraftLivingEntity;
    protected static Class<?> class_CraftWorld;
    protected static Class<?> class_Consumer;
    protected static Class<?> class_Entity;
    protected static Class<?> class_EntityCreature;
    protected static Class<?> class_EntityLiving;
    protected static Class<?> class_EntityHuman;
    protected static Class<?> class_DataWatcher;
    protected static Class<?> class_DamageSource;
    protected static Class<?> class_EntityDamageSource;
    protected static Class<?> class_World;
    protected static Class<?> class_WorldServer;
    protected static Class<?> class_Packet;
    protected static Class<Enum> class_EnumSkyBlock;
    protected static Class<Enum> class_PickupStatus;
    protected static Class<?> class_EntityPainting;
    protected static Class<?> class_EntityItemFrame;
    protected static Class<?> class_EntityMinecartRideable;
    protected static Class<?> class_EntityTNTPrimed;
    protected static Class<?> class_AxisAlignedBB;
    protected static Class<?> class_EntityFirework;
    protected static Class<?> class_CraftSkull;
    protected static Class<?> class_CraftMetaSkull;
    protected static Class<?> class_GameProfile;
    protected static Class<?> class_GameProfileProperty;
    protected static Class<?> class_BlockPosition;
    protected static Class<?> class_NBTCompressedStreamTools;
    protected static Class<?> class_TileEntity;
    protected static Class<?> class_IBlockData;
    protected static Class<?> class_TileEntitySign;
    protected static Class<?> class_TileEntityContainer;
    protected static Class<?> class_ChestLock;
    protected static Class<Enum> class_EnumDirection;
    protected static Class<Enum> class_EnumExplosionEffect;
    protected static Enum<?> enum_ExplosionEffect_BREAK;
    protected static Enum<?> enum_ExplosionEffect_NONE;
    protected static Class<?> class_EntityHorse;
    protected static Class<?> class_EntityWitherSkull;
    protected static Class<?> class_PacketPlayOutAttachEntity;
    protected static Class<?> class_PacketPlayOutEntityDestroy;
    protected static Class<?> class_PacketPlayOutSpawnEntity;
    protected static Class<?> class_PacketPlayOutSpawnEntityLiving;
    protected static Class<?> class_PacketPlayOutEntityMetadata;
    protected static Class<?> class_PacketPlayOutEntityStatus;
    protected static Class<?> class_PacketPlayOutCustomSoundEffect;
    protected static Class<?> class_PacketPlayOutExperience;
    protected static Class<?> class_PacketPlayOutAnimation;
    protected static Class<?> class_PacketPlayOutBlockBreakAnimation;
    protected static Enum<?> enum_SoundCategory_PLAYERS;
    protected static Class<Enum> class_EnumSoundCategory;
    protected static Class<?> class_EntityFallingBlock;
    protected static Class<?> class_EntityArmorStand;
    protected static Class<?> class_EntityPlayer;
    protected static Class<?> class_PlayerConnection;
    protected static Class<?> class_Chunk;
    protected static Class<?> class_CraftPlayer;
    protected static Class<?> class_CraftChunk;
    protected static Class<?> class_CraftEntity;
    protected static Class<?> class_EntityProjectile;
    protected static Class<?> class_EntityFireball;
    protected static Class<?> class_EntityArrow;
    protected static Class<?> class_CraftArrow;
    protected static Class<?> class_MinecraftServer;
    protected static Class<?> class_CraftServer;
    protected static Class<?> class_DataWatcherObject;
    protected static Class<?> class_PacketPlayOutChat;
    protected static Class<Enum> class_ChatMessageType;
    protected static Enum<?> enum_ChatMessageType_GAME_INFO;
    protected static Class<?> class_ChatComponentText;
    protected static Class<?> class_IChatBaseComponent;
    protected static Class<?> class_NamespacedKey;
    protected static Class<?> class_KnowledgeBookMeta;
    protected static Class<?> class_entityTypes;
    protected static Class<?> class_Powerable;
    protected static Class<?> class_Waterlogged;
    protected static Class<?> class_Bisected;
    protected static Class<Enum> class_BisectedHalf;
    protected static Enum<?> enum_BisectedHalf_TOP;
    protected static Class<?> class_Sittable;
    protected static Class<?> class_Lootable;
    protected static Class<?> class_RecipeChoice_ExactChoice;
    protected static Class<?> class_BlockActionContext;
    protected static Class<Enum> class_EnumHand;
    protected static Enum<?> enum_EnumHand_MAIN_HAND;
    protected static Class<?> class_MovingObjectPositionBlock;
    protected static Class<?> class_Vec3D;
    protected static Class<?> class_Phantom;
    protected static Class<?> class_Keyed;

    protected static Method class_NBTTagList_addMethod;
    protected static Method class_NBTTagList_getMethod;
    protected static Method class_NBTTagList_getDoubleMethod;
    protected static Method class_NBTTagList_sizeMethod;
    protected static Method class_NBTTagList_removeMethod;
    protected static Method class_NBTTagCompound_getKeysMethod;
    protected static Method class_NBTTagCompound_setMethod;
    protected static Method class_World_getEntitiesMethod;
    protected static Method class_Sitting_setSittingMethod;
    protected static Method class_Sitting_isSittingMethod;
    protected static Method class_Entity_setSilentMethod;
    protected static Method class_Entity_isSilentMethod;
    protected static Method class_Entity_setYawPitchMethod;
    protected static Method class_Entity_getBukkitEntityMethod;
    protected static Method class_EntityLiving_damageEntityMethod;
    protected static Method class_DamageSource_getMagicSourceMethod;
    protected static Method class_EntityDamageSource_setThornsMethod;
    protected static Method class_World_explodeMethod;
    protected static Method class_NBTTagCompound_setBooleanMethod;
    protected static Method class_NBTTagCompound_setStringMethod;
    protected static Method class_NBTTagCompound_setDoubleMethod;
    protected static Method class_NBTTagCompound_setLongMethod;
    protected static Method class_NBTTagCompound_setIntMethod;
    protected static Method class_NBTTagCompound_removeMethod;
    protected static Method class_NBTTagCompound_getStringMethod;
    protected static Method class_NBTTagCompound_getBooleanMethod;
    protected static Method class_NBTTagCompound_getIntMethod;
    protected static Method class_NBTTagCompound_getDoubleMethod;
    protected static Method class_NBTTagCompound_getByteMethod;
    protected static Method class_NBTTagCompound_getMethod;
    protected static Method class_NBTTagCompound_getCompoundMethod;
    protected static Method class_NBTTagCompound_getShortMethod;
    protected static Method class_NBTTagCompound_getByteArrayMethod;
    protected static Method class_NBTTagCompound_getIntArrayMethod;
    protected static Method class_NBTTagCompound_getListMethod;
    protected static Method class_Entity_saveMethod;
    protected static Method class_Entity_getTypeMethod;
    protected static Method class_TileEntity_loadMethod;
    protected static Method class_TileEntity_saveMethod;
    protected static Method class_TileEntity_updateMethod;
    protected static Method class_World_addEntityMethod;
    protected static Method class_World_setTypeAndDataMethod;
    protected static Method class_World_getTypeMethod;
    protected static Method class_NBTCompressedStreamTools_loadFileMethod;
    protected static Method class_CraftItemStack_asBukkitCopyMethod;
    protected static Method class_CraftItemStack_copyMethod;
    protected static Method class_CraftItemStack_mirrorMethod;
    protected static Method class_NBTTagCompound_hasKeyMethod;
    protected static Method class_CraftWorld_getTileEntityAtMethod;
    protected static Method class_CraftWorld_createEntityMethod;
    protected static Method class_CraftWorld_spawnMethod;
    protected static boolean class_CraftWorld_spawnMethod_isLegacy;
    protected static Method class_Entity_setLocationMethod;
    protected static Method class_Entity_getIdMethod;
    protected static Method class_Entity_getDataWatcherMethod;
    protected static Method class_Entity_getBoundingBox;
    protected static Method class_TileEntityContainer_setLock;
    protected static Method class_TileEntityContainer_getLock;
    protected static Method class_ChestLock_getString;
    protected static Method class_ArmorStand_setInvisible;
    protected static Method class_ArmorStand_setGravity;
    protected static Method class_Entity_setNoGravity;
    protected static Method class_CraftPlayer_getHandleMethod;
    protected static Method class_CraftPlayer_getProfileMethod;
    protected static Method class_CraftChunk_getHandleMethod;
    protected static Method class_CraftEntity_getHandleMethod;
    protected static Method class_CraftLivingEntity_getHandleMethod;
    protected static Method class_CraftWorld_getHandleMethod;
    protected static Method class_EntityPlayer_openSignMethod;
    protected static Method class_EntityPlayer_setResourcePackMethod;
    protected static Method class_CraftServer_getServerMethod;
    protected static Method class_MinecraftServer_getResourcePackMethod;
    protected static Method class_ItemStack_isEmptyMethod;
    protected static Method class_ItemStack_createStackMethod;
    protected static Method class_CraftMagicNumbers_getBlockMethod;
    protected static Method class_Block_fromLegacyData;
    protected static Method class_Chunk_setBlockMethod;
    protected static Method class_Arrow_setPickupStatusMethod;
    protected static Method class_ProjectileHitEvent_getHitBlockMethod;
    protected static Method class_Server_getEntityMethod;
    protected static Method class_UnsafeValues_fromLegacyDataMethod;
    protected static Method class_UnsafeValues_fromLegacyMethod;
    protected static Method class_Material_isLegacyMethod;
    protected static Method class_Material_getLegacyMethod;
    protected static Method class_Block_setTypeIdAndDataMethod;
    protected static Method class_Chunk_isReadyMethod;
    protected static Method class_ItemDye_bonemealMethod;
    protected static Method class_PotionMeta_getColorMethod;
    protected static Method class_PotionMeta_setColorMethod;
    protected static Method class_FallingBlock_getBlockDataMethod;
    protected static Method class_Block_getBlockDataMethod;
    protected static Method class_Block_setBlockDataMethod;
    protected static Method class_Server_createBlockDataMethod;
    protected static Method class_BlockData_getAsStringMethod;
    protected static Method class_KnowledgeBookMeta_addRecipeMethod;
    protected static Method class_World_getTileEntityMethod;
    protected static Method class_Bukkit_getMapMethod;
    protected static Method class_Bukkit_selectEntitiesMethod;
    protected static Method class_Waterlogged_setWaterloggedMethod;
    protected static Method class_Powerable_setPoweredMethod;
    protected static Method class_Powerable_isPoweredMethod;
    protected static Method class_Bisected_setHalfMethod;
    protected static Method class_ItemMeta_addAttributeModifierMethod;
    protected static Method class_ItemMeta_removeAttributeModifierMethod;
    protected static Method class_Player_stopSoundMethod;
    protected static Method class_Player_stopSoundStringMethod;
    protected static Method class_Chunk_addPluginChunkTicketMethod;
    protected static Method class_Chunk_removePluginChunkTicketMethod;
    protected static Method class_Lootable_setLootTableMethod;
    protected static Method class_CraftArt_NotchToBukkitMethod;
    protected static Method class_BlockPosition_getXMethod;
    protected static Method class_BlockPosition_getYMethod;
    protected static Method class_BlockPosition_getZMethod;
    protected static Method class_Recipe_setGroupMethod;
    protected static Method class_ShapedRecipe_setIngredientMethod;
    protected static Method class_CraftBlock_getNMSBlockMethod;
    protected static Method class_Block_getPlacedStateMethod;
    protected static Method class_MovingObjectPositionBlock_createMethod;
    protected static Method class_CraftBlock_setTypeAndDataMethod;
    protected static Method class_nms_Block_getBlockDataMethod;
    protected static Method class_Phantom_getSizeMethod;
    protected static Method class_Phantom_setSizeMethod;
    protected static Method class_Server_removeRecipeMethod;
    protected static Method class_HumanEntity_discoverRecipeMethod;
    protected static Method class_HumanEntity_undiscoverRecipeMethod;
    protected static Method class_EntityHuman_getBedMethod;
    protected static Method class_EntityPlayer_getSpawnDimensionMethod;
    protected static Method class_MinecraftServer_getWorldServerMethod;
    protected static Method class_WorldServer_worldMethod;
    protected static Method class_Keyed_getKeyMethod;
    protected static Method class_LivingEntity_setRemoveWhenFarAway;
    protected static Method class_World_getChunkAtAsyncMethod;

    protected static boolean legacyMaps;

    protected static Constructor class_CraftInventoryCustom_constructor;
    protected static Constructor class_EntityFireworkConstructor;
    protected static Constructor class_EntityPaintingConstructor;
    protected static Constructor class_EntityItemFrameConstructor;
    protected static Constructor class_BlockPosition_Constructor;
    protected static Constructor class_PacketSpawnEntityConstructor;
    protected static Constructor class_PacketSpawnLivingEntityConstructor;
    protected static Constructor class_PacketPlayOutEntityMetadata_Constructor;
    protected static Constructor class_PacketPlayOutEntityStatus_Constructor;
    protected static Constructor class_PacketPlayOutEntityDestroy_Constructor;
    protected static Constructor class_PacketPlayOutCustomSoundEffect_Constructor;
    protected static Constructor class_PacketPlayOutExperience_Constructor;
    protected static Constructor class_PacketPlayOutAnimation_Constructor;
    protected static Constructor class_PacketPlayOutBlockBreakAnimation_Constructor;
    protected static Constructor class_ChestLock_Constructor;
    protected static Constructor class_AxisAlignedBB_Constructor;
    protected static Constructor class_ItemStack_consructor;
    protected static Constructor class_NBTTagCompound_constructor;
    protected static Constructor class_NBTTagList_constructor;
    protected static NBTConstructor class_NBTTagString_consructor;
    protected static NBTConstructor class_NBTTagByte_constructor;
    protected static NBTConstructor class_NBTTagDouble_constructor;
    protected static NBTConstructor class_NBTTagInt_constructor;
    protected static NBTConstructor class_NBTTagFloat_constructor;
    protected static NBTConstructor class_NBTTagLong_constructor;
    protected static Constructor class_NBTTagIntArray_constructor;
    protected static Constructor class_NBTTagByteArray_constructor;
    protected static Constructor class_NBTTagLongArray_constructor;
    protected static Constructor class_PacketPlayOutChat_constructor;
    protected static Constructor class_ChatComponentText_constructor;
    protected static Constructor class_NamespacedKey_constructor;
    protected static Constructor class_ShapedRecipe_constructor;
    protected static Constructor class_GameProfile_constructor;
    protected static Constructor class_GameProfileProperty_constructor;
    protected static Constructor class_GameProfileProperty_noSignatureConstructor;
    protected static Constructor class_MinecraftKey_constructor;
    protected static Constructor class_Vec3D_constructor;
    protected static Constructor class_AttributeModifier_constructor;
    protected static Constructor class_RecipeChoice_ExactChoice_constructor;
    protected static Constructor class_RecipeChoice_ExactChoice_List_constructor;
    protected static Constructor class_BlockActionContext_constructor;

    protected static Method class_Entity_setInvulnerable;
    protected static Method class_Entity_isInvulnerable;
    protected static Method class_ItemStack_getTag;
    protected static Method class_ItemStack_setTag;

    protected static Method class_WorldServer_getEntityMethod;

    protected static Field class_Entity_persistField;
    protected static Field class_Entity_motXField;
    protected static Field class_Entity_motYField;
    protected static Field class_Entity_motZField;
    protected static Field class_Entity_motField;
    protected static Field class_Firework_ticksFlownField;
    protected static Field class_Firework_expectedLifespanField;
    protected static Field class_CraftSkull_profile;
    protected static Field class_CraftMetaSkull_profile;
    protected static Field class_GameProfile_properties;
    protected static Field class_GameProfileProperty_value;
    protected static Field class_GameProfileProperty_signature;
    protected static Field class_GameProfileProperty_name;
    protected static Field class_EntityTNTPrimed_source;
    protected static Field class_NBTTagList_list;
    protected static Field class_AxisAlignedBB_minXField;
    protected static Field class_AxisAlignedBB_minYField;
    protected static Field class_AxisAlignedBB_minZField;
    protected static Field class_AxisAlignedBB_maxXField;
    protected static Field class_AxisAlignedBB_maxYField;
    protected static Field class_AxisAlignedBB_maxZField;
    protected static Field class_EntityFallingBlock_hurtEntitiesField;
    protected static Field class_EntityFallingBlock_fallHurtMaxField;
    protected static Field class_EntityFallingBlock_fallHurtAmountField;
    protected static Field class_EntityArmorStand_disabledSlotsField;
    protected static Field class_EntityPlayer_playerConnectionField;
    protected static Field class_PlayerConnection_floatCountField;
    protected static Field class_CraftItemStack_getHandleField;
    protected static Field class_EntityArrow_lifeField = null;
    protected static Field class_EntityArrow_damageField;
    protected static Field class_CraftWorld_environmentField;
    protected static Field class_MemorySection_mapField;
    protected static Field class_NBTTagByte_dataField;
    protected static Field class_NBTTagDouble_dataField;
    protected static Field class_NBTTagFloat_dataField;
    protected static Field class_NBTTagInt_dataField;
    protected static Field class_NBTTagLong_dataField;
    protected static Field class_NBTTagShort_dataField;
    protected static Field class_NBTTagString_dataField;
    protected static Field class_Block_durabilityField;
    protected static Field class_Entity_jumpingField;
    protected static Field class_Entity_moveStrafingField;
    protected static Field class_Entity_moveForwardField;
    protected static Field class_TileEntityContainer_lock;
    protected static Field class_ChestLock_key;
    protected static Field class_EntityPainting_art;
    protected static Field class_EntityHanging_blockPosition;
    protected static Field class_EntityHuman_spawnWorldField;
    protected static Field class_EntityPlayer_serverField;

    protected static Object object_magicSource;
    protected static Object object_emptyChestLock;
    protected static Map<String, Object> damageSources;
    protected static Map<String, Object> entityTypes;

    protected static boolean chatPacketHasUUID = false;

    public static boolean initialize(Logger logger) {
        NMSUtils.logger = logger;
        // Find classes Bukkit hides from us. :-D
        // Much thanks to @DPOHVAR for sharing the PowerNBT code that powers the reflection approach.
        String className = Bukkit.getServer().getClass().getName();
        String[] packages = StringUtils.split(className, '.');
        if (packages.length == 5) {
            versionPrefix = packages[3] + ".";
        }

        String[] versions = className.replace(".", ",").split(",")[3].substring(1).split("_");
        version = Integer.parseInt(versions[1]); // 1.X
        versionMinor = Integer.parseInt(versions[2].substring(1)); // 1.X.Y
        npack = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        cpack = Bukkit.getServer().getClass().getPackage().getName() + ".";

        try {
            class_Block = fixBukkitClass("Block", true, "world.level.block");
            class_Entity = fixBukkitClass("Entity", true, "world.entity");
            class_EntityLiving = fixBukkitClass("EntityLiving", true, "world.entity");
            class_EntityHuman = fixBukkitClass("EntityHuman", true, "world.entity.player");
            class_ItemStack = fixBukkitClass("ItemStack", true, "world.item");
            class_DataWatcher = fixBukkitClass("DataWatcher", true, "network.syncher");
            class_DataWatcherObject = fixBukkitClass("DataWatcherObject", true, "network.syncher");
            class_NBTBase = fixBukkitClass("NBTBase", true, "nbt");
            class_NBTTagCompound = fixBukkitClass("NBTTagCompound", true, "nbt");
            class_NBTTagList = fixBukkitClass("NBTTagList", true, "nbt");
            class_NBTTagString = fixBukkitClass("NBTTagString", true, "nbt");
            class_NBTTagByte = fixBukkitClass("NBTTagByte", true, "nbt");
            class_NBTTagDouble = fixBukkitClass("NBTTagDouble", true, "nbt");
            class_NBTTagFloat = fixBukkitClass("NBTTagFloat", true, "nbt");
            class_NBTTagInt = fixBukkitClass("NBTTagInt", true, "nbt");
            class_NBTTagLong = fixBukkitClass("NBTTagLong", true, "nbt");
            class_NBTTagShort = fixBukkitClass("NBTTagShort", true, "nbt");
            class_CraftWorld = fixBukkitClass("CraftWorld", false);
            class_CraftInventoryCustom = fixBukkitClass("inventory.CraftInventoryCustom", false);
            class_CraftItemStack = fixBukkitClass("inventory.CraftItemStack", false);
            class_CraftBlockState = fixBukkitClass("block.CraftBlockState", false);
            class_CraftTask = fixBukkitClass("scheduler.CraftTask", false);
            class_CraftLivingEntity = fixBukkitClass("entity.CraftLivingEntity", false);
            class_Packet = fixBukkitClass("Packet", true, "network.protocol");
            class_World = fixBukkitClass("World", true, "world.level");
            class_WorldServer = fixBukkitClass("WorldServer", true, "server.level");
            class_EnumSkyBlock = (Class<Enum>)fixBukkitClass("EnumSkyBlock", true, "world.level");
            class_EnumSoundCategory = (Class<Enum>)fixBukkitClass("SoundCategory", true, "sounds");
            enum_SoundCategory_PLAYERS = Enum.valueOf(class_EnumSoundCategory, "PLAYERS");
            class_EntityPainting = fixBukkitClass("EntityPainting", true, "world.entity.decoration");
            class_EntityCreature = fixBukkitClass("EntityCreature", true, "world.entity");
            class_EntityItemFrame = fixBukkitClass("EntityItemFrame", true, "world.entity.decoration");
            class_EntityMinecartRideable = fixBukkitClass("EntityMinecartRideable", true, "world.entity.vehicle");
            class_EntityTNTPrimed = fixBukkitClass("EntityTNTPrimed", true, "world.entity.item");
            class_AxisAlignedBB = fixBukkitClass("AxisAlignedBB", true, "world.phys");
            class_DamageSource = fixBukkitClass("DamageSource", true, "world.damagesource");
            class_EntityDamageSource = fixBukkitClass("EntityDamageSource", true, "world.damagesource");
            class_EntityFirework = fixBukkitClass("EntityFireworks", true, "world.entity.projectile");
            class_CraftSkull = fixBukkitClass("block.CraftSkull", false);
            class_CraftMetaSkull = fixBukkitClass("inventory.CraftMetaSkull", false);
            class_NBTCompressedStreamTools = fixBukkitClass("NBTCompressedStreamTools", true, "nbt");
            class_TileEntity = fixBukkitClass("TileEntity", true, "world.level.block.entity");
            class_EntityHorse = fixBukkitClass("EntityHorse", true, "world.entity.animal.horse");
            class_EntityWitherSkull = fixBukkitClass("EntityWitherSkull", true, "world.entity.projectile");
            class_PacketPlayOutAttachEntity = fixBukkitClass("PacketPlayOutAttachEntity", true, "network.protocol.game");
            class_PacketPlayOutEntityDestroy = fixBukkitClass("PacketPlayOutEntityDestroy", true, "network.protocol.game");
            class_PacketPlayOutSpawnEntity = fixBukkitClass("PacketPlayOutSpawnEntity", true, "network.protocol.game");
            class_PacketPlayOutSpawnEntityLiving = fixBukkitClass("PacketPlayOutSpawnEntityLiving", true, "network.protocol.game");
            class_PacketPlayOutEntityMetadata = fixBukkitClass("PacketPlayOutEntityMetadata", true, "network.protocol.game");
            class_PacketPlayOutEntityStatus = fixBukkitClass("PacketPlayOutEntityStatus", true, "network.protocol.game");
            class_PacketPlayOutExperience = fixBukkitClass("PacketPlayOutExperience", true, "network.protocol.game");
            class_PacketPlayOutAnimation = fixBukkitClass("PacketPlayOutAnimation", true, "network.protocol.game");
            class_PacketPlayOutBlockBreakAnimation = fixBukkitClass("PacketPlayOutBlockBreakAnimation", true, "network.protocol.game");
            class_EntityFallingBlock = fixBukkitClass("EntityFallingBlock", true,"world.entity.item");
            class_EntityArmorStand = fixBukkitClass("EntityArmorStand", true, "world.entity.decoration");
            class_EntityPlayer = fixBukkitClass("EntityPlayer", true, "server.level");
            class_PlayerConnection = fixBukkitClass("PlayerConnection", true, "server.network");
            class_Chunk = fixBukkitClass("Chunk", true, "world.level.chunk");
            class_CraftPlayer = fixBukkitClass("entity.CraftPlayer", false);
            class_CraftChunk = fixBukkitClass("CraftChunk", false);
            class_CraftEntity = fixBukkitClass("entity.CraftEntity", false);
            class_TileEntitySign = fixBukkitClass("TileEntitySign", true, "world.level.block.entity");
            class_CraftServer = fixBukkitClass("CraftServer", false);
            class_MinecraftServer = fixBukkitClass("MinecraftServer", true, "server");
            class_BlockPosition = fixBukkitClass("BlockPosition", true, "core");

            class_EntityProjectile = fixBukkitClass("EntityProjectile", true, "world.entity.projectile");
            class_EntityFireball = fixBukkitClass("EntityFireball", true, "world.entity.projectile");
            class_EntityArrow = fixBukkitClass("EntityArrow", true, "world.entity.projectile");
            class_CraftArrow = fixBukkitClass("entity.CraftArrow", false);

            class_Entity_getBukkitEntityMethod = class_Entity.getMethod("getBukkitEntity");
            class_Entity_setYawPitchMethod = class_Entity.getDeclaredMethod("setYawPitch", Float.TYPE, Float.TYPE);
            class_Entity_setYawPitchMethod.setAccessible(true);
            class_NBTTagCompound_setBooleanMethod = class_NBTTagCompound.getMethod("setBoolean", String.class, Boolean.TYPE);
            class_NBTTagCompound_setStringMethod = class_NBTTagCompound.getMethod("setString", String.class, String.class);
            class_NBTTagCompound_setDoubleMethod = class_NBTTagCompound.getMethod("setDouble", String.class, Double.TYPE);
            class_NBTTagCompound_setLongMethod = class_NBTTagCompound.getMethod("setLong", String.class, Long.TYPE);
            class_NBTTagCompound_setIntMethod = class_NBTTagCompound.getMethod("setInt", String.class, Integer.TYPE);
            class_NBTTagCompound_removeMethod = class_NBTTagCompound.getMethod("remove", String.class);
            class_NBTTagCompound_getStringMethod = class_NBTTagCompound.getMethod("getString", String.class);
            class_NBTTagCompound_getShortMethod = class_NBTTagCompound.getMethod("getShort", String.class);
            class_NBTTagCompound_getIntMethod = class_NBTTagCompound.getMethod("getInt", String.class);
            class_NBTTagCompound_getDoubleMethod = class_NBTTagCompound.getMethod("getDouble", String.class);
            class_NBTTagCompound_getBooleanMethod = class_NBTTagCompound.getMethod("getBoolean", String.class);
            class_NBTTagCompound_getByteMethod = class_NBTTagCompound.getMethod("getByte", String.class);
            class_NBTTagCompound_getByteArrayMethod = class_NBTTagCompound.getMethod("getByteArray", String.class);
            class_NBTTagCompound_getListMethod = class_NBTTagCompound.getMethod("getList", String.class, Integer.TYPE);
            class_CraftItemStack_copyMethod = class_CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            class_CraftItemStack_asBukkitCopyMethod = class_CraftItemStack.getMethod("asBukkitCopy", class_ItemStack);
            class_CraftItemStack_mirrorMethod = class_CraftItemStack.getMethod("asCraftMirror", class_ItemStack);
            class_World_addEntityMethod = class_World.getMethod("addEntity", class_Entity, CreatureSpawnEvent.SpawnReason.class);
            class_Entity_setLocationMethod = class_Entity.getMethod("setLocation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            class_Entity_getIdMethod = class_Entity.getMethod("getId");
            class_Entity_getDataWatcherMethod = class_Entity.getMethod("getDataWatcher");
            class_ArmorStand_setInvisible = class_EntityArmorStand.getDeclaredMethod("setInvisible", Boolean.TYPE);
            class_CraftPlayer_getHandleMethod = class_CraftPlayer.getMethod("getHandle");
            class_CraftChunk_getHandleMethod = class_CraftChunk.getMethod("getHandle");
            class_CraftEntity_getHandleMethod = class_CraftEntity.getMethod("getHandle");
            class_CraftLivingEntity_getHandleMethod = class_CraftLivingEntity.getMethod("getHandle");
            class_CraftWorld_getHandleMethod = class_CraftWorld.getMethod("getHandle");
            class_EntityPlayer_openSignMethod = class_EntityPlayer.getMethod("openSign", class_TileEntitySign);
            // 1.17 : remove because it's setResourcePack(String, String, boolean, IChatBaseComponent)
//          class_EntityPlayer_setResourcePackMethod = class_EntityPlayer.getMethod("setResourcePack", String.class, String.class);
            class_CraftServer_getServerMethod = class_CraftServer.getMethod("getServer");
            class_MinecraftServer_getResourcePackMethod = class_MinecraftServer.getMethod("getResourcePack");
            
            class_CraftInventoryCustom_constructor = class_CraftInventoryCustom.getConstructor(InventoryHolder.class, Integer.TYPE, String.class);
            class_EntityFireworkConstructor = class_EntityFirework.getConstructor(class_World, Double.TYPE, Double.TYPE, Double.TYPE, class_ItemStack);
            class_PacketSpawnEntityConstructor = class_PacketPlayOutSpawnEntity.getConstructor(class_Entity, Integer.TYPE);
            class_PacketSpawnLivingEntityConstructor = class_PacketPlayOutSpawnEntityLiving.getConstructor(class_EntityLiving);
            class_PacketPlayOutEntityMetadata_Constructor = class_PacketPlayOutEntityMetadata.getConstructor(Integer.TYPE, class_DataWatcher, Boolean.TYPE);
            class_PacketPlayOutEntityStatus_Constructor = class_PacketPlayOutEntityStatus.getConstructor(class_Entity, Byte.TYPE);
            class_PacketPlayOutEntityDestroy_Constructor = class_PacketPlayOutEntityDestroy.getConstructor(int[].class);
            class_PacketPlayOutExperience_Constructor = class_PacketPlayOutExperience.getConstructor(Float.TYPE, Integer.TYPE, Integer.TYPE);
            class_PacketPlayOutAnimation_Constructor = class_PacketPlayOutAnimation.getConstructor(class_Entity, Integer.TYPE);
            class_PacketPlayOutBlockBreakAnimation_Constructor = class_PacketPlayOutBlockBreakAnimation.getConstructor(Integer.TYPE, class_BlockPosition, Integer.TYPE);

            class_CraftWorld_environmentField = class_CraftWorld.getDeclaredField("environment");
            class_CraftWorld_environmentField.setAccessible(true);
            class_Entity_setInvulnerable = class_Entity.getDeclaredMethod("setInvulnerable", boolean.class);
            class_Entity_isInvulnerable = class_Entity.getDeclaredMethod("isInvulnerable");

            class_ItemStack_getTag = class_ItemStack.getDeclaredMethod("getTag");
            class_ItemStack_setTag = class_ItemStack.getDeclaredMethod("setTag", class_NBTTagCompound);

            class_EntityTNTPrimed_source = class_EntityTNTPrimed.getDeclaredField(version < 17 ? "source" : "d"); // 1.17 : owner
            class_EntityPlayer_playerConnectionField = class_EntityPlayer.getDeclaredField(version < 17 ? "playerConnection" : "b"); // 1.17 : connection

            class_Firework_ticksFlownField = class_EntityFirework.getDeclaredField(version < 17 ? "ticksFlown" : "e"); // 1.17 : life
            class_Firework_ticksFlownField.setAccessible(true);
            class_Firework_expectedLifespanField = class_EntityFirework.getDeclaredField(version < 17 ? "expectedLifespan" : "f"); // 1.17 : lifetime
            class_Firework_expectedLifespanField.setAccessible(true);

            class_NBTTagCompound_constructor = class_NBTTagCompound.getConstructor();
            class_NBTTagList_constructor = class_NBTTagList.getConstructor();
            class_NBTTagString_consructor = new NBTConstructor(class_NBTTagString, String.class);
            class_NBTTagByte_constructor = new NBTConstructor(class_NBTTagByte, Byte.TYPE);
            class_NBTTagDouble_constructor = new NBTConstructor(class_NBTTagDouble, Double.TYPE);
            class_NBTTagInt_constructor = new NBTConstructor(class_NBTTagInt, Integer.TYPE);
            class_NBTTagFloat_constructor = new NBTConstructor(class_NBTTagFloat, Float.TYPE);
            class_NBTTagLong_constructor = new NBTConstructor(class_NBTTagLong, Long.TYPE);

            Class<?> class_NBTTagIntArray = fixBukkitClass("NBTTagIntArray", true, "nbt");
            Class<?> class_NBTTagByteArray = fixBukkitClass("NBTTagByteArray", true, "nbt");
            class_NBTTagIntArray_constructor = class_NBTTagIntArray.getConstructor(int[].class);
            class_NBTTagByteArray_constructor = class_NBTTagByteArray.getConstructor(byte[].class);

            try {
                Class<?> class_NBTTagLongArray = fixBukkitClass("NBTTagLongArray", true, "nbt");
                class_NBTTagLongArray_constructor = class_NBTTagLongArray.getConstructor(long[].class);
            } catch (Throwable ignore) {
                class_NBTTagLongArray_constructor = null;
            }

            class_NBTTagList_list = class_NBTTagList.getDeclaredField(version < 17 ? "list" : "w");
            class_NBTTagList_list.setAccessible(true);
            class_NBTTagByte_dataField = class_NBTTagByte.getDeclaredField(version < 17 ? "data" : "x");
            class_NBTTagByte_dataField.setAccessible(true);
            class_NBTTagDouble_dataField = class_NBTTagDouble.getDeclaredField(version < 17 ? "data" : "w");
            class_NBTTagDouble_dataField.setAccessible(true);
            class_NBTTagFloat_dataField = class_NBTTagFloat.getDeclaredField(version < 17 ? "data" : "w");
            class_NBTTagFloat_dataField.setAccessible(true);
            class_NBTTagInt_dataField = class_NBTTagInt.getDeclaredField(version < 17 ? "data" : "c");
            class_NBTTagInt_dataField.setAccessible(true);
            class_NBTTagLong_dataField = class_NBTTagLong.getDeclaredField(version < 17 ? "data" : "c");
            class_NBTTagLong_dataField.setAccessible(true);
            class_NBTTagShort_dataField = class_NBTTagShort.getDeclaredField(version < 17 ? "data" : "c");
            class_NBTTagShort_dataField.setAccessible(true);
            class_NBTTagString_dataField = class_NBTTagString.getDeclaredField(version < 17 ? "data" : "A");
            class_NBTTagString_dataField.setAccessible(true);
            class_NBTTagList_getMethod = class_NBTTagList.getMethod("get", Integer.TYPE);
            class_NBTTagList_sizeMethod = class_NBTTagList.getMethod("size");
            class_NBTTagList_removeMethod = class_NBTTagList.getMethod("remove", Integer.TYPE);
            class_NBTTagCompound_setMethod = class_NBTTagCompound.getMethod("set", String.class, class_NBTBase);
            class_NBTTagCompound_hasKeyMethod = class_NBTTagCompound.getMethod("hasKey", String.class);
            class_NBTTagCompound_getMethod = class_NBTTagCompound.getMethod("get", String.class);
            class_NBTTagCompound_getCompoundMethod = class_NBTTagCompound.getMethod("getCompound", String.class);

            class_EntityFallingBlock_hurtEntitiesField = class_EntityFallingBlock.getDeclaredField(version < 17 ? "hurtEntities" : "ap");
            class_EntityFallingBlock_hurtEntitiesField.setAccessible(true);
            class_EntityFallingBlock_fallHurtAmountField = class_EntityFallingBlock.getDeclaredField(version < 17 ? "fallHurtAmount" : "ar"); // 1.17 : fallDamagePerDistance
            class_EntityFallingBlock_fallHurtAmountField.setAccessible(true);
            class_EntityFallingBlock_fallHurtMaxField = class_EntityFallingBlock.getDeclaredField(version < 17 ? "fallHurtMax" : "aq"); // 1.17 : fallDamageMax
            class_EntityFallingBlock_fallHurtMaxField.setAccessible(true);

            class_CraftItemStack_getHandleField = class_CraftItemStack.getDeclaredField("handle");
            class_CraftItemStack_getHandleField.setAccessible(true);

            class_MemorySection_mapField = MemorySection.class.getDeclaredField("map");
            class_MemorySection_mapField.setAccessible(true);

            class_TileEntityContainer = fixBukkitClass("TileEntityContainer", true, "world.level.block.entity");
            class_ChestLock = fixBukkitClass("ChestLock", true, "world");
            class_Entity_getBoundingBox = class_Entity.getMethod("getBoundingBox");
            class_GameProfile = getClass("com.mojang.authlib.GameProfile");
            class_GameProfile_constructor = class_GameProfile.getConstructor(UUID.class, String.class);
            class_GameProfileProperty = getClass("com.mojang.authlib.properties.Property");
            class_CraftSkull_profile = class_CraftSkull.getDeclaredField("profile");
            class_CraftSkull_profile.setAccessible(true);
            class_CraftMetaSkull_profile = class_CraftMetaSkull.getDeclaredField("profile");
            class_CraftMetaSkull_profile.setAccessible(true);
            class_GameProfile_properties = class_GameProfile.getDeclaredField("properties");
            class_GameProfile_properties.setAccessible(true);
            class_GameProfileProperty_value = class_GameProfileProperty.getDeclaredField("value");
            class_GameProfileProperty_value.setAccessible(true);
            class_GameProfileProperty_signature = class_GameProfileProperty.getDeclaredField("signature");
            class_GameProfileProperty_signature.setAccessible(true);
            class_GameProfileProperty_name = class_GameProfileProperty.getDeclaredField("name");
            class_GameProfileProperty_name.setAccessible(true);
            class_GameProfileProperty_constructor = class_GameProfileProperty.getConstructor(String.class, String.class, String.class);
            class_GameProfileProperty_noSignatureConstructor = class_GameProfileProperty.getConstructor(String.class, String.class);

            class_EnumDirection = (Class<Enum>)fixBukkitClass("EnumDirection", true, "core");
            class_BlockPosition_Constructor = class_BlockPosition.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE);
            class_EntityPaintingConstructor = class_EntityPainting.getConstructor(class_World, class_BlockPosition, class_EnumDirection);
            class_EntityItemFrameConstructor = class_EntityItemFrame.getConstructor(class_World, class_BlockPosition, class_EnumDirection);

            // TODO: Server.getEntity(UUID) in 1.11+
            class_WorldServer_getEntityMethod = class_WorldServer.getDeclaredMethod("getEntity", UUID.class);

            // TODO: World.getNearbyEntities in 1.11+
            class_AxisAlignedBB_Constructor = class_AxisAlignedBB.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
            class_World_getEntitiesMethod = class_World.getMethod("getEntities", class_Entity, class_AxisAlignedBB);

            // We don't want to consider new-ish builds as "legacy" and print a warning, so keep a separate flag
            boolean current = true;

            try {
                Class.forName("org.bukkit.event.player.PlayerStatisticIncrementEvent");
                hasStatistics = true;
            } catch (Throwable ex) {
                hasStatistics = false;
                getLogger().warning("Statistics not available, jump trigger will not work");
            }

            try {
                Class.forName("org.bukkit.event.entity.EntityTransformEvent");
                hasEntityTransformEvent = true;
            } catch (Throwable ex) {
                hasEntityTransformEvent = false;
                getLogger().warning("EntityTransformEvent not found, can't prevent mobs naturally transforming");
            }

            try {
                Class.forName("org.bukkit.event.world.TimeSkipEvent");
                hasTimeSkipEvent = true;
            } catch (Throwable ex) {
                hasTimeSkipEvent = false;
                getLogger().warning("TimeSkipEvent not found, can't synchronize time between worlds");
            }

            try {
                class_World_getChunkAtAsyncMethod = World.class.getMethod("getChunkAtAsync", Integer.TYPE, Integer.TYPE, Boolean.TYPE, Consumer.class);
                logger.info("Async chunk loading API found");
            } catch (Throwable ignore) {
                class_World_getChunkAtAsyncMethod = null;
            }

            try {
                Class<?> class_RecipeChoice = Class.forName("org.bukkit.inventory.RecipeChoice");
                class_RecipeChoice_ExactChoice = Class.forName("org.bukkit.inventory.RecipeChoice$ExactChoice");
                class_RecipeChoice_ExactChoice_constructor = class_RecipeChoice_ExactChoice.getConstructor(ItemStack.class);
                class_RecipeChoice_ExactChoice_List_constructor = class_RecipeChoice_ExactChoice.getConstructor(List.class);
                class_Recipe_setGroupMethod = ShapedRecipe.class.getMethod("setGroup", String.class);
                class_ShapedRecipe_setIngredientMethod = ShapedRecipe.class.getMethod("setIngredient", Character.TYPE, class_RecipeChoice);
            } catch (Throwable ex) {
                class_Recipe_setGroupMethod = null;
                class_RecipeChoice_ExactChoice = null;
                getLogger().warning("Use an updated version of Spigot for better knowledge book recipes");
            }

            try {
                Class<?> class_CraftArt = fixBukkitClass("CraftArt", false);
                class_CraftArt_NotchToBukkitMethod = class_CraftArt.getMethod("BukkitToNotch", Art.class);
                Class<?> class_EntityPainting = fixBukkitClass("EntityPainting", true, "world.entity.decoration");
                class_EntityPainting_art = class_EntityPainting.getDeclaredField("art");
                class_EntityPainting_art.setAccessible(true);
                Class<?> class_EntityHanging = fixBukkitClass("EntityHanging", true, "world.entity.decoration");
                class_EntityHanging_blockPosition = class_EntityHanging.getField("blockPosition");
                class_EntityHanging_blockPosition.setAccessible(true);
                class_BlockPosition_getXMethod = class_BlockPosition.getMethod("getX");
                class_BlockPosition_getYMethod = class_BlockPosition.getMethod("getY");
                class_BlockPosition_getZMethod = class_BlockPosition.getMethod("getZ");
            } catch (Throwable ex) {
                class_EntityPainting_art = null;
                class_CraftArt_NotchToBukkitMethod = null;
                class_EntityHanging_blockPosition = null;
                getLogger().warning("Could not bind to painting art fields, restoring paintings may not work");
            }

            try {
                class_Chunk_addPluginChunkTicketMethod = Chunk.class.getMethod("addPluginChunkTicket", Plugin.class);
                class_Chunk_removePluginChunkTicketMethod = Chunk.class.getMethod("removePluginChunkTicket", Plugin.class);
            } catch (Throwable ex) {
                getLogger().warning("Could not bind to chunk ticket API, chunk locking will not work");
                class_Chunk_addPluginChunkTicketMethod = null;
                class_Chunk_removePluginChunkTicketMethod = null;
            }

            try {
                class_Phantom = Class.forName("org.bukkit.entity.Phantom");
                class_Phantom_getSizeMethod = class_Phantom.getMethod("getSize");
                class_Phantom_setSizeMethod = class_Phantom.getMethod("setSize", Integer.TYPE);
            } catch (Throwable ex) {
                getLogger().warning("No phantoms on this version");
                class_Phantom = null;
            }

            // Particularly volatile methods that we can live without
            try {
                class_ItemMeta_addAttributeModifierMethod = ItemMeta.class.getMethod("addAttributeModifier", Attribute.class, AttributeModifier.class);
                class_ItemMeta_removeAttributeModifierMethod = ItemMeta.class.getMethod("removeAttributeModifier", Attribute.class);
                class_AttributeModifier_constructor = AttributeModifier.class.getConstructor(UUID.class, String.class, Double.TYPE, AttributeModifier.Operation.class, EquipmentSlot.class);
            } catch (Throwable ignore) {
                class_ItemMeta_addAttributeModifierMethod = null;
            }

            try {
                class_Entity_persistField = class_Entity.getDeclaredField("persist");
                class_Entity_persistField.setAccessible(true);
                class_LivingEntity_setRemoveWhenFarAway = LivingEntity.class.getMethod("setRemoveWhenFarAway", Boolean.TYPE);
            } catch (Throwable ex) {
                class_Entity_persistField = null;
                getLogger().warning("Could not bind to persist entity tag, can't make mobs persistent");
            }

            try {
                class_Player_stopSoundMethod = Player.class.getMethod("stopSound", Sound.class);
                class_Player_stopSoundStringMethod = Player.class.getMethod("stopSound", String.class);
            } catch (Throwable ex) {
                class_Player_stopSoundMethod = null;
                class_Player_stopSoundStringMethod = null;
                getLogger().warning("Could not bind to stopSound method, StopSound action will not work.");
            }

            try {
                class_Sittable = Class.forName("org.bukkit.entity.Sittable");
                class_Sitting_isSittingMethod = class_Sittable.getMethod("isSitting");
                class_Sitting_setSittingMethod = class_Sittable.getMethod("setSitting", Boolean.TYPE);
            } catch (Throwable ex) {
                class_Sittable = null;
                getLogger().warning("Could not bind to Sittable interface, can't make mobs sit/stand");
            }

            // 1.13 Support
            Class<?> class_MinecraftKey = null;
            try {
                @SuppressWarnings("deprecation")
                Class<?> unsafe = org.bukkit.UnsafeValues.class;
                Class<?> materialData = org.bukkit.material.MaterialData.class;
                class_UnsafeValues_fromLegacyDataMethod = unsafe.getMethod("fromLegacy", materialData);
                class_UnsafeValues_fromLegacyMethod = unsafe.getMethod("fromLegacy", Material.class, Byte.TYPE);
                class_Material_isLegacyMethod = Material.class.getMethod("isLegacy");
                class_Material_getLegacyMethod = Material.class.getMethod("getMaterial", String.class, Boolean.TYPE);

                class_MinecraftKey = fixBukkitClass("MinecraftKey", true, "resources");
                class_MinecraftKey_constructor = class_MinecraftKey.getConstructor(String.class);
                class_Vec3D = fixBukkitClass("Vec3D", true, "world.phys");
                class_Vec3D_constructor = class_Vec3D.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE);

                Class<?> class_BlockData = Class.forName("org.bukkit.block.data.BlockData");
                class_Block_getBlockDataMethod = Block.class.getMethod("getBlockData");
                class_Block_setBlockDataMethod = Block.class.getMethod("setBlockData", class_BlockData, Boolean.TYPE);
                class_Server_createBlockDataMethod = Server.class.getMethod("createBlockData", String.class);
                class_BlockData_getAsStringMethod = class_BlockData.getMethod("getAsString");
                isModernVersion = true;
            } catch (Throwable ex) {
                class_UnsafeValues_fromLegacyMethod = null;
                class_UnsafeValues_fromLegacyDataMethod = null;
                class_Material_isLegacyMethod = null;
            }
            try {
                class_Bukkit_getMapMethod = org.bukkit.Bukkit.class.getMethod("getMap", Integer.TYPE);
                legacyMaps = false;
            } catch (Throwable not13) {
                try {
                    class_Bukkit_getMapMethod = org.bukkit.Bukkit.class.getMethod("getMap", Short.TYPE);
                    legacyMaps = true;
                } catch (Exception ex) {
                    getLogger().warning("Could not bind to getMap method, magic maps will not work");
                    class_Bukkit_getMapMethod = null;
                }
            }
            try {
                class_Bukkit_selectEntitiesMethod = org.bukkit.Bukkit.class.getMethod("selectEntities", CommandSender.class, String.class);
            } catch (Throwable not13) {
                getLogger().warning("Could not bind to selectEntities method, command target selectors may not work");
                class_Bukkit_selectEntitiesMethod = null;
            }
            try {
                class_Waterlogged = Class.forName("org.bukkit.block.data.Waterlogged");
                class_Waterlogged_setWaterloggedMethod = class_Waterlogged.getMethod("setWaterlogged", Boolean.TYPE);
            } catch (Exception ex) {
                class_Waterlogged_setWaterloggedMethod = null;
            }
            try {
                class_Lootable = Class.forName("org.bukkit.loot.Lootable");
                Class<?> class_LootTable = Class.forName("org.bukkit.loot.LootTable");
                class_Lootable_setLootTableMethod = class_Lootable.getMethod("setLootTable", class_LootTable);
            } catch (Exception ex) {
                class_Waterlogged_setWaterloggedMethod = null;
            }
            try {
                class_Powerable = Class.forName("org.bukkit.block.data.Powerable");
                class_Powerable_setPoweredMethod = class_Powerable.getMethod("setPowered", Boolean.TYPE);
                class_Powerable_isPoweredMethod = class_Powerable.getMethod("isPowered");
            } catch (Exception ignore) {
                class_Powerable = null;
            }
            try {
                class_Bisected = Class.forName("org.bukkit.block.data.Bisected");
                class_BisectedHalf = (Class<Enum>)Class.forName("org.bukkit.block.data.Bisected$Half");
                enum_BisectedHalf_TOP =  Enum.valueOf(class_BisectedHalf, "TOP");
                class_Bisected_setHalfMethod = class_Bisected.getMethod("setHalf", class_BisectedHalf);
            } catch (Exception ignore) {
                class_Bisected = null;
            }

            // 1.14 Support
            try {
                class_EnumExplosionEffect = (Class<Enum>)fixBukkitClass("Explosion$Effect", true, "world.level");
                enum_ExplosionEffect_BREAK = Enum.valueOf(class_EnumExplosionEffect, "BREAK");
                enum_ExplosionEffect_NONE = Enum.valueOf(class_EnumExplosionEffect, "NONE");
                class_World_explodeMethod = class_World.getMethod("createExplosion", class_Entity, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Boolean.TYPE, class_EnumExplosionEffect);

                isCurrentVersion = true;
                isModernVersion = true;
            } catch (Throwable not14) {
                try {
                    class_World_explodeMethod = class_World.getMethod("createExplosion", class_Entity, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Boolean.TYPE, Boolean.TYPE);
                } catch (Throwable ex) {
                    getLogger().warning("Could not bind to createExplosion method, magic explosions will not be attributed to the caster");
                    class_World_explodeMethod = null;
                }
            }
            if (isCurrentVersion) {
                try {
                    entityTypes = new HashMap<>();
                    class_entityTypes = fixBukkitClass("EntityTypes", true, "world.entity");
                    for (Field field : class_entityTypes.getFields()) {
                        if (field.getType().equals(class_entityTypes)) {
                            Object entityType = field.get(null);
                            // This won't really work for all entity types, but it should work for most projectiles
                            // which is all this map is used for.
                            String name = field.getName();
                            name = "Entity" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
                            entityTypes.put(name, entityType);

                            // We may need to do some manual fixups here.
                            if (name.equals("EntityArrow")) {
                                entityTypes.put("EntityTippedArrow", entityType);
                            } else if (name.equals("EntityFireball")) {
                                entityTypes.put("EntityLargeFireball", entityType);
                            }
                        }
                    }
                } catch (Throwable not14) {
                    getLogger().warning("Could not bind to entity types, projectile launches will not work");
                }
            }

            try {
                class_Entity_motField = class_Entity.getDeclaredField("mot");
                class_Entity_motField.setAccessible(true);
            } catch (Throwable not14) {
                try {
                    class_Entity_motXField = class_Entity.getDeclaredField("motX");
                    class_Entity_motXField.setAccessible(true);
                    class_Entity_motYField = class_Entity.getDeclaredField("motY");
                    class_Entity_motYField.setAccessible(true);
                    class_Entity_motZField = class_Entity.getDeclaredField("motZ");
                    class_Entity_motZField.setAccessible(true);
                } catch (Throwable ex) {
                    getLogger().warning("Could not bind to motion setters, some things may be broken");
                }
            }
            try {
                class_NBTTagList_addMethod = class_NBTTagList.getMethod("add", Integer.TYPE, class_NBTBase);
            } catch (Throwable not14) {
                class_NBTTagList_addMethod = class_NBTTagList.getMethod("add", class_NBTBase);
            }

            // Changed in 1.13.2
            try {
                class_AxisAlignedBB_minXField = class_AxisAlignedBB.getField("a");
                class_AxisAlignedBB_minYField = class_AxisAlignedBB.getField("b");
                class_AxisAlignedBB_minZField = class_AxisAlignedBB.getField("c");
                class_AxisAlignedBB_maxXField = class_AxisAlignedBB.getField("d");
                class_AxisAlignedBB_maxYField = class_AxisAlignedBB.getField("e");
                class_AxisAlignedBB_maxZField = class_AxisAlignedBB.getField("f");
            } catch (Throwable ignore) {
                try {
                    class_AxisAlignedBB_minXField = class_AxisAlignedBB.getField("minX");
                    class_AxisAlignedBB_minYField = class_AxisAlignedBB.getField("minY");
                    class_AxisAlignedBB_minZField = class_AxisAlignedBB.getField("minZ");
                    class_AxisAlignedBB_maxXField = class_AxisAlignedBB.getField("maxX");
                    class_AxisAlignedBB_maxYField = class_AxisAlignedBB.getField("maxY");
                    class_AxisAlignedBB_maxZField = class_AxisAlignedBB.getField("maxZ");
                } catch (Throwable ex) {
                     getLogger().warning("Could not bind to AABB methods, vanilla hitboxes aren't readable");
                     class_Entity_getBoundingBox = null;
                }
            }

            // Changed in 1.13
            try {
                class_FallingBlock_getBlockDataMethod = FallingBlock.class.getMethod("getBlockData");
            } catch (Throwable ignore) {

            }
            try {
                class_PacketPlayOutCustomSoundEffect = fixBukkitClass("PacketPlayOutCustomSoundEffect", true, "network.protocol.game");

                if (class_MinecraftKey_constructor != null) {
                    class_PacketPlayOutCustomSoundEffect_Constructor = class_PacketPlayOutCustomSoundEffect.getConstructor(class_MinecraftKey, class_EnumSoundCategory, class_Vec3D, Float.TYPE, Float.TYPE);
                } else {
                    class_PacketPlayOutCustomSoundEffect_Constructor = class_PacketPlayOutCustomSoundEffect.getConstructor(String.class, class_EnumSoundCategory, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                }
            } catch (Throwable ex) {
                 getLogger().warning("Could not bind to custom effect method, custom sound effects will not work");
            }

            try {
                Class<?> class_ItemBoneMeal = fixBukkitClass("ItemBoneMeal", true, "world.item");
                class_ItemDye_bonemealMethod = class_ItemBoneMeal.getMethod("a", class_ItemStack, class_World, class_BlockPosition);
            } catch (Throwable not13) {
                try {
                    Class<?> class_ItemDye = fixBukkitClass("ItemDye", true, "world.item");
                    class_ItemDye_bonemealMethod = class_ItemDye.getMethod("a", class_ItemStack, class_World, class_BlockPosition);
                } catch (Throwable ex) {
                    class_ItemDye_bonemealMethod = null;
                    getLogger().info("Couldn't bind to ItemDye bonemeal method, Bonemeal action will not work");
                }
            }


            try {
                class_NBTTagCompound_getIntArrayMethod = class_NBTTagCompound.getMethod("getIntArray", String.class);
            } catch (Throwable ex) {
                class_NBTTagCompound_getIntArrayMethod = null;
                getLogger().info("Couldn't bind to NBT getIntArray method, pasting tile entities from schematics may not work");
            }

            try {
                try {
                    // 1.14
                    class_NBTTagList_getDoubleMethod = class_NBTTagList.getMethod("h", Integer.TYPE);
                    if (class_NBTTagList_getDoubleMethod.getReturnType() != Double.TYPE) {
                        throw new Exception("Not 1.14");
                    }
                } catch (Throwable not14) {
                    try {
                        // 1.13
                        class_NBTTagList_getDoubleMethod = class_NBTTagList.getMethod("k", Integer.TYPE);
                        if (class_NBTTagList_getDoubleMethod.getReturnType() != Double.TYPE) {
                            throw new Exception("Not 1.13");
                        }
                    } catch (Throwable not13) {
                        try {
                            // 1.12
                            class_NBTTagList_getDoubleMethod = class_NBTTagList.getMethod("f", Integer.TYPE);
                            if (class_NBTTagList_getDoubleMethod.getReturnType() != Double.TYPE) {
                                throw new Exception("Not 1.12");
                            }
                        } catch (Throwable not12) {
                            // 1.11 and lower
                            current = false;
                            class_NBTTagList_getDoubleMethod = class_NBTTagList.getMethod("e", Integer.TYPE);
                        }
                    }
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering NBTTagList.getDouble, loading entities from schematics will not work", ex);
                class_NBTTagList_getDoubleMethod = null;
            }
            try {
                Class<?> class_IBlockData = fixBukkitClass("IBlockData", true, "world.level.block.state");
                class_World_getTypeMethod = class_World.getMethod("getType", class_BlockPosition);
                class_World_setTypeAndDataMethod = class_World.getMethod("setTypeAndData", class_BlockPosition, class_IBlockData, Integer.TYPE);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering World.setTypeAndData, Deferred physics updates will not work", ex);
                class_World_setTypeAndDataMethod = null;
            }

            try {
                try {
                    class_DamageSource_getMagicSourceMethod = class_DamageSource.getMethod("c", class_Entity, class_Entity);
                } catch (Throwable not13) {
                    class_DamageSource_getMagicSourceMethod = class_DamageSource.getMethod("b", class_Entity, class_Entity);
                }
                class_EntityLiving_damageEntityMethod = class_EntityLiving.getMethod("damageEntity", class_DamageSource, Float.TYPE);
                Field damageSource_MagicField = class_DamageSource.getField("MAGIC");
                object_magicSource = damageSource_MagicField.get(null);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, magic damage will not work, using normal damage instead");
                //getLogger().log(Level.WARNING, "An error occurred, magic damage will not work, using normal damage instead", ex);
                class_EntityLiving_damageEntityMethod = null;
                class_DamageSource_getMagicSourceMethod = null;
                object_magicSource = null;
            }

            try {
                try {
                    class_EntityHuman_getBedMethod = class_EntityPlayer.getMethod("getSpawn");
                    class_EntityPlayer_getSpawnDimensionMethod = class_EntityPlayer.getMethod("getSpawnDimension");
                    class_EntityPlayer_serverField = class_EntityPlayer.getField("server");
                    Class<?> class_resourceKey = fixBukkitClass("ResourceKey", true, "resources");
                    class_MinecraftServer_getWorldServerMethod = class_MinecraftServer.getMethod("getWorldServer", class_resourceKey);
                    class_WorldServer_worldMethod = class_WorldServer.getMethod("getWorld");
                } catch (Exception notCurrent) {
                    class_EntityHuman_getBedMethod = class_EntityHuman.getMethod("getBed");
                    class_EntityHuman_spawnWorldField = class_EntityHuman.getField("spawnWorld");
                }
            } catch (Throwable ex) {
                class_EntityHuman_getBedMethod = null;
                getLogger().log(Level.WARNING, "An error occurred, could not get bed location directly, will use API");
                //getLogger().log(Level.WARNING, "An error occurred, could not get bed location directly, will use API", ex);
            }

            try {
                class_NBTTagCompound_getKeysMethod = class_NBTTagCompound.getMethod("getKeys");
            } catch (Throwable not13) {
                // We can't actually live without this one.
                class_NBTTagCompound_getKeysMethod = class_NBTTagCompound.getMethod("c");
            }

            // 1.12 and lower
            try {
                class_PotionMeta_getColorMethod = PotionMeta.class.getMethod("getColor");
                class_PotionMeta_setColorMethod = PotionMeta.class.getMethod("setColor", Color.class);
            } catch (Throwable ex) {
                 getLogger().warning("Could not bind to PotionMeta color methods, Custom colored potions will not work.");
            }
            try {
                class_Block_setTypeIdAndDataMethod = Block.class.getMethod("setTypeIdAndData", Integer.TYPE, Byte.TYPE, Boolean.TYPE);
            } catch (Throwable ex) {
                if (!isModernVersion) {
                    getLogger().warning("Could not bind to setTypeIdAndData, Magic will have issues modifying blocks");
                }
            }
            try {
                Class<?> class_IBlockData = fixBukkitClass("IBlockData", true, "world.level.block.state");
                class_Block_fromLegacyData = class_Block.getMethod("fromLegacyData", Integer.TYPE);
                class_Chunk_setBlockMethod = class_Chunk.getMethod("a", class_BlockPosition, class_IBlockData);
            } catch (Throwable ex) {
                if (!isModernVersion) {
                    getLogger().log(Level.WARNING, "An error occurred while registering Block.fromLegacyData, setting fast blocks will not work.");
                }
            }

            try {
                class_Chunk_isReadyMethod = class_Chunk.getMethod("isReady");
            } catch (Throwable ex) {
                class_Chunk_isReadyMethod = null;
                if (!isCurrentVersion) {
                    getLogger().warning("Couldn't bind to Chunk.isReady, building in ungenerated chunks may be glitchy.");
                }
            }

            try {
                class_NamespacedKey = Class.forName("org.bukkit.NamespacedKey");
                class_NamespacedKey_constructor = class_NamespacedKey.getConstructor(Plugin.class, String.class);
                class_ShapedRecipe_constructor = ShapedRecipe.class.getConstructor(class_NamespacedKey, ItemStack.class);
                class_Keyed = Class.forName("org.bukkit.Keyed");
                class_Keyed_getKeyMethod = class_Keyed.getMethod("getKey");
            } catch (Throwable ex) {
                class_NamespacedKey = null;
                class_NamespacedKey_constructor = null;
                class_ShapedRecipe_constructor = null;
                getLogger().warning("Couldn't find NamespacedKey for registering recipes. This is normal for legacy Minecraft versions.");
            }

            if (class_NamespacedKey != null) {
                try {
                    class_Server_removeRecipeMethod = Server.class.getMethod("removeRecipe", class_NamespacedKey);
                } catch (Throwable ex) {
                    class_Server_removeRecipeMethod = null;
                    getLogger().warning("Couldn't find recipe removal method, this is odd since we did find NamespacedKey");
                }
                try {
                    class_HumanEntity_discoverRecipeMethod = HumanEntity.class.getMethod("discoverRecipe", class_NamespacedKey);
                    class_HumanEntity_undiscoverRecipeMethod = HumanEntity.class.getMethod("undiscoverRecipe", class_NamespacedKey);
                } catch (Throwable ex) {
                    class_HumanEntity_discoverRecipeMethod = null;
                    class_HumanEntity_undiscoverRecipeMethod = null;
                    getLogger().warning("Couldn't find recipe discover method, this is odd since we did find NamespacedKey");
                }
            }

            if (class_NamespacedKey != null) {
                try {
                    class_KnowledgeBookMeta = Class.forName("org.bukkit.inventory.meta.KnowledgeBookMeta");
                    Class<?> keyArray = Array.newInstance(class_NamespacedKey, 0).getClass();
                    class_KnowledgeBookMeta_addRecipeMethod = class_KnowledgeBookMeta.getMethod("addRecipe", keyArray);
                } catch (Throwable ex) {
                    class_KnowledgeBookMeta = null;
                    class_KnowledgeBookMeta_addRecipeMethod = null;
                    getLogger().warning("Couldn't register knowledge book methods, recipe books unavailable. This is normal for pre-1.13 Minecraft versions.");
                }
            }

            try {
                class_Server_getEntityMethod = Server.class.getMethod("getEntity", UUID.class);
            } catch (Throwable ex) {
                class_Server_getEntityMethod = null;
                getLogger().warning("Could not register Server.getEntity, entity lookups will be slightly less optimal");
            }
            try {
                class_ProjectileHitEvent_getHitBlockMethod = ProjectileHitEvent.class.getMethod("getHitBlock");
            } catch (Throwable ex) {
                class_ProjectileHitEvent_getHitBlockMethod = null;
                getLogger().warning("Could not register ProjectileHitEvent.getHitBlock, arrow hit locations will be fuzzy");
            }
            try {
                class_PickupStatus = (Class<Enum>)Class.forName("org.bukkit.entity.AbstractArrow$PickupStatus");
                Class<?> arrowClass = Class.forName("org.bukkit.entity.AbstractArrow");
                class_Arrow_setPickupStatusMethod = arrowClass.getMethod("setPickupStatus", class_PickupStatus);
            } catch (Throwable not141) {
                try {
                    class_PickupStatus = (Class<Enum>)Class.forName("org.bukkit.entity.Arrow$PickupStatus");
                    class_Arrow_setPickupStatusMethod = Arrow.class.getMethod("setPickupStatus", class_PickupStatus);
                } catch (Throwable ex) {
                    class_PickupStatus = null;
                    class_Arrow_setPickupStatusMethod = null;
                    getLogger().warning("Could not register Arrow.PickupStatus, arrows can not be made to be picked up");
                }
            }
            try {
                class_CraftPlayer_getProfileMethod = class_CraftPlayer.getMethod("getProfile");
            } catch (Throwable ex) {
                class_CraftPlayer_getProfileMethod = null;
                getLogger().log(Level.WARNING, "An error occurred while registering Player.getProfile, player portrait maps may not work as well", ex);
            }

            try {
                try {
                    if (!isCurrentVersion) {
                        throw new Exception("Not 1.16");
                    }
                    try {
                        class_Entity_jumpingField = class_EntityLiving.getDeclaredField("jumping");
                        class_Entity_jumpingField.setAccessible(true);
                        class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("aR");
                        class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("aT");
                        if (!isPublic(class_Entity_moveStrafingField) || !isPublic(class_Entity_moveForwardField)) {
                            throw new Exception("Not 1.162");
                        }
                        if (class_Entity_moveStrafingField.getType() != Float.TYPE || class_Entity_moveForwardField.getType() != Float.TYPE) {
                            throw new Exception("Not 1.162");
                        }
                    } catch (Throwable not162) {
                        class_Entity_jumpingField = class_EntityLiving.getDeclaredField("jumping");
                        class_Entity_jumpingField.setAccessible(true);
                        class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("aY");
                        class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("ba");
                        if (!isPublic(class_Entity_moveStrafingField) || !isPublic(class_Entity_moveForwardField)) {
                            throw new Exception("Not 1.16");
                        }
                        if (class_Entity_moveStrafingField.getType() != Float.TYPE || class_Entity_moveForwardField.getType() != Float.TYPE) {
                            throw new Exception("Not 1.16");
                        }
                    }
                } catch (Throwable not16) {
                    try {
                        if (!isCurrentVersion) {
                            throw new Exception("Not 1.15");
                        }
                        class_Entity_jumpingField = class_EntityLiving.getDeclaredField("jumping");
                        class_Entity_jumpingField.setAccessible(true);
                        class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("aZ");
                        class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bb");
                        if (!isPublic(class_Entity_moveStrafingField) || !isPublic(class_Entity_moveForwardField)) {
                            throw new Exception("Not 1.15");
                        }
                    } catch (Throwable not15) {
                        // 1.14
                        try {
                            if (!isCurrentVersion) {
                                throw new Exception("Not 1.14");
                            }
                            class_Entity_jumpingField = class_EntityLiving.getDeclaredField("jumping");
                            class_Entity_jumpingField.setAccessible(true);
                            class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("bb");
                            class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bd");
                        } catch (Throwable not14) {
                            // 1.13
                            try {
                                if (!isModernVersion) {
                                    throw new Exception("Not 1.13");
                                }
                                class_Entity_jumpingField = class_EntityLiving.getDeclaredField("bg");
                                class_Entity_jumpingField.setAccessible(true);
                                class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("bh");
                                class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bj");
                            } catch (Throwable not13) {
                                // 1.12
                                try {
                                    if (!current) {
                                        throw new Exception("Not 1.12");
                                    }
                                    class_Entity_jumpingField = class_EntityLiving.getDeclaredField("bd");
                                    class_Entity_jumpingField.setAccessible(true);
                                    class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("be");
                                    class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bg");
                                } catch (Throwable not12) {
                                    // 1.11
                                    current = false;
                                    try {
                                        class_Entity_jumpingField = class_EntityLiving.getDeclaredField("bd");
                                        class_Entity_jumpingField.setAccessible(true);
                                        class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("be");
                                        class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bf");
                                    } catch (Throwable not11) {
                                        // 1.10
                                        try {
                                            class_Entity_jumpingField = class_EntityLiving.getDeclaredField("be");
                                            class_Entity_jumpingField.setAccessible(true);
                                            class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("bf");
                                            class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("bg");
                                        } catch (Throwable not10) {
                                            class_Entity_jumpingField = class_EntityLiving.getDeclaredField("bc");
                                            class_Entity_jumpingField.setAccessible(true);
                                            class_Entity_moveStrafingField = class_EntityLiving.getDeclaredField("bd");
                                            class_Entity_moveForwardField = class_EntityLiving.getDeclaredField("be");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!isPublic(class_Entity_moveStrafingField) || !isPublic(class_Entity_moveForwardField)) {
                        throw new Exception("Could not find accessible methods");
                    }
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering entity movement accessors, vehicle control will not work");
                //getLogger().log(Level.WARNING, "An error occurred while registering entity movement accessors, vehicle control will not work", ex);
                class_Entity_jumpingField = null;
                class_Entity_moveStrafingField = null;
                class_Entity_moveForwardField = null;
            }

            try {
                class_BlockBase = fixBukkitClass("BlockBase", true, "world.level.block.state");
                class_Block_durabilityField = class_BlockBase.getDeclaredField("durability");
                class_Block_durabilityField.setAccessible(true);
            } catch (Exception not16) {
                try {
                    class_Block_durabilityField = class_Block.getDeclaredField("durability");
                    class_Block_durabilityField.setAccessible(true);
                    Class<?> craftMagicNumbers = fixBukkitClass("util.CraftMagicNumbers", false);
                    class_CraftMagicNumbers_getBlockMethod = craftMagicNumbers.getMethod("getBlock", Material.class);
                } catch (Throwable ex) {
                    getLogger().log(Level.WARNING, "An error occurred while registering block durability accessor, durability-based block checks will not work");
                    //getLogger().log(Level.WARNING, "An error occurred while registering block durability accessor, durability-based block checks will not work", ex);
                    class_Block_durabilityField = null;
                    class_CraftMagicNumbers_getBlockMethod = null;
                }
            }

            try {
                Class<?> craftMagicNumbers = fixBukkitClass("util.CraftMagicNumbers", false);
                class_CraftMagicNumbers_getBlockMethod = craftMagicNumbers.getMethod("getBlock", Material.class);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering block accessor, durability-based block checks will not work", ex);
                class_CraftMagicNumbers_getBlockMethod = null;
            }

            try {
                // 1.12
                try {
                    // Common to 1.12 and below
                    class_PacketPlayOutChat = fixBukkitClass("PacketPlayOutChat", true, "network.protocol.game");
                    class_ChatComponentText = fixBukkitClass("ChatComponentText", true, "network.chat");
                    class_IChatBaseComponent = fixBukkitClass("IChatBaseComponent", true, "network.chat");
                    class_ChatComponentText_constructor = class_ChatComponentText.getConstructor(String.class);

                    // Common to 1.16 and below
                    class_ChatMessageType = (Class<Enum>)fixBukkitClass("ChatMessageType", true, "network.chat");
                    enum_ChatMessageType_GAME_INFO = Enum.valueOf(class_ChatMessageType, "GAME_INFO");

                    // 1.16 specific
                    try {
                        class_PacketPlayOutChat_constructor = class_PacketPlayOutChat.getConstructor(class_IChatBaseComponent, class_ChatMessageType, UUID.class);
                        chatPacketHasUUID = true;
                    } catch (Throwable not16) {
                        // 1.12 specific
                        class_PacketPlayOutChat_constructor = class_PacketPlayOutChat.getConstructor(class_IChatBaseComponent, class_ChatMessageType);
                    }

                } catch (Throwable ex) {
                    // 1.11 fallback
                    current = false;
                    class_PacketPlayOutChat_constructor = class_PacketPlayOutChat.getConstructor(class_IChatBaseComponent, Byte.TYPE);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering action bar methods, action bar messages will not work", ex);
                class_PacketPlayOutChat = null;
            }

            try {
                try {
                    // 1.11
                    class_CraftWorld_createEntityMethod = class_CraftWorld.getMethod("createEntity", Location.class, Class.class);
                    class_Consumer = fixBukkitClass("org.bukkit.util.Consumer");
                    class_CraftWorld_spawnMethod = class_CraftWorld.getMethod("spawn", Location.class, Class.class, class_Consumer, CreatureSpawnEvent.SpawnReason.class);
                    class_CraftWorld_spawnMethod_isLegacy = false;
                } catch (Throwable ignore) {
                    setLegacy();
                    class_CraftWorld_spawnMethod_isLegacy = true;
                    class_CraftWorld_spawnMethod = class_CraftWorld.getMethod("spawn", Location.class, Class.class, CreatureSpawnEvent.SpawnReason.class);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred while registering custom spawn method, spawn reasons will not work", ex);
                class_CraftWorld_spawnMethod = null;
                class_Consumer = null;
            }

            try {
                try {
                    class_IBlockData = fixBukkitClass("IBlockData", true, "world.level.block.state");
                    class_TileEntity_loadMethod = class_TileEntity.getMethod("load", class_IBlockData, class_NBTTagCompound);
                } catch (Throwable not16) {
                    class_IBlockData = null;
                    try {
                        // 1.12.1
                        class_TileEntity_loadMethod = class_TileEntity.getMethod("load", class_NBTTagCompound);
                    } catch (Throwable ignore) {
                        class_TileEntity_loadMethod = class_TileEntity.getMethod("a", class_NBTTagCompound);
                    }
                }
                try {
                    class_CraftWorld_getTileEntityAtMethod = class_CraftWorld.getMethod("getTileEntityAt", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                } catch (Throwable ignore) {
                    // This should actually work in 1.10 and up at least, and is the preferred method to use.
                    // For now we're going to keep old versions doing it the old way, though.

                    class_World_getTileEntityMethod = class_World.getMethod("getTileEntity", class_BlockPosition);
                }
                class_TileEntity_updateMethod = class_TileEntity.getMethod("update");
                class_TileEntity_saveMethod = class_TileEntity.getMethod("save", class_NBTTagCompound);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, handling of tile entities may not work well", ex);
                class_TileEntity_loadMethod = null;
                class_TileEntity_updateMethod = null;
                class_TileEntity_saveMethod = null;
            }

            try {
                class_NBTCompressedStreamTools_loadFileMethod = class_NBTCompressedStreamTools.getMethod("a", InputStream.class);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, schematics will not load", ex);
            }

            try {
                damageSources = new HashMap<>();
                Field[] fields = class_DamageSource.getFields();
                for (Field field : fields) {
                    if (class_DamageSource.isAssignableFrom(field.getType())) {
                        damageSources.put(field.getName(), field.get(null));
                    }
                }
            } catch (Throwable ex) {
                damageSources = null;
                getLogger().log(Level.WARNING, "An error occurred, using specific damage types will not work, will use normal damage instead", ex);
            }
            try {
                class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("disabledSlots");
            } catch (Exception notcurrent) {
                try {
                    try {
                        class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bv");
                        if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.15/1.14, maybe");
                    } catch (Exception not16) {
                        try {
                            class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bE");
                            if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.13, maybe");
                        } catch (Exception not14) {
                            // 1.13
                            try {
                                class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bH");
                                if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.12, maybe");
                            } catch (Exception not13) {
                                try {
                                    // 1.12, same as 1.10
                                    class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bB");
                                    if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.11, maybe");
                                } catch (Throwable not12) {
                                    try {
                                        // 1.11
                                        class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bA");
                                        if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.10");
                                    } catch (Throwable ignore) {
                                        // 1.10 and earlier
                                        setLegacy();
                                        try {
                                            class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bB");
                                            if (class_EntityArmorStand_disabledSlotsField.getType() != Integer.TYPE) throw new Exception("Looks like 1.9");
                                        } catch (Throwable ignore2) {
                                            try {
                                                // 1.9.4
                                                class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bA");
                                            } catch (Throwable ignore3) {
                                                // 1.9.2
                                                class_EntityArmorStand_disabledSlotsField = class_EntityArmorStand.getDeclaredField("bz");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable ex) {
                    getLogger().log(Level.WARNING, "An error occurred, armor stand slots cannot be locked", ex);
                    class_EntityArmorStand_disabledSlotsField = null;
                }
            }
            if (class_EntityArmorStand_disabledSlotsField != null) {
                class_EntityArmorStand_disabledSlotsField.setAccessible(true);
            }

            // TODO: Lockable API in 1.11+
            try {
                try {
                    // Common
                    class_ChestLock_Constructor = class_ChestLock.getConstructor(String.class);

                    // 1.14 only
                    class_TileEntityContainer_lock = class_TileEntityContainer.getField("chestLock");
                    class_ChestLock_key = class_ChestLock.getField("key");
                    Field class_ChestLock_defaultField = class_ChestLock.getField("a");
                    if (class_ChestLock.isAssignableFrom(class_ChestLock_defaultField.getType())) {
                        object_emptyChestLock = class_ChestLock_defaultField.get(null);
                    } else {
                        getLogger().log(Level.WARNING, "An error occurred, chest unlocking will not work");
                    }
                } catch (Throwable not14) {
                    try {
                        // 1.12 and below
                        class_TileEntityContainer_getLock = class_TileEntityContainer.getMethod("getLock");

                        // 1.12 only
                        class_ChestLock_getString = class_ChestLock.getMethod("getKey");
                        class_TileEntityContainer_setLock = class_TileEntityContainer.getMethod("setLock", class_ChestLock);
                    } catch (Throwable not12) {
                        try {
                            // 1.11
                            class_ChestLock_getString = class_ChestLock.getMethod("b");
                            class_TileEntityContainer_setLock = class_TileEntityContainer.getMethod("a", class_ChestLock);
                        } catch (Throwable ignore) {
                            // 1.10 and earlier
                            setLegacy();
                            class_TileEntityContainer_setLock = class_TileEntityContainer.getMethod("a", class_ChestLock);
                            class_TileEntityContainer_getLock = class_TileEntityContainer.getMethod("y_");
                        }
                    }
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, chest locking and unlocking will not work");
                //getLogger().log(Level.WARNING, "An error occurred, chest locking and unlocking will not work", ex);
                class_TileEntityContainer_setLock = null;
                class_TileEntityContainer_getLock = null;
            }

            try {
                try {
                    // 1.10 and 1.11
                    class_PlayerConnection_floatCountField = class_PlayerConnection.getDeclaredField("C");
                    if (class_PlayerConnection_floatCountField.getType() != Integer.TYPE) throw new Exception("Looks like 1.9");
                    class_PlayerConnection_floatCountField.setAccessible(true);
                } catch (Throwable ignore) {
                    // 1.9 and earlier
                    setLegacy();
                    class_PlayerConnection_floatCountField = class_PlayerConnection.getDeclaredField("g");
                    class_PlayerConnection_floatCountField.setAccessible(true);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, player flight exemption will not work");
                //getLogger().log(Level.WARNING, "An error occurred, player flight exemption will not work", ex);
                class_PlayerConnection_floatCountField = null;
            }

            try {
                try { // Purpur fork
                    Class<?> class_IProjectile = fixBukkitClass("IProjectile", true, "world.entity.projectile");
                    class_EntityArrow_lifeField = class_IProjectile.getDeclaredField("despawnCounter");
                } catch (Throwable ignore) {
                    try {
                        // 1.13
                        class_EntityArrow_lifeField = class_EntityArrow.getDeclaredField("despawnCounter");
                    } catch (Throwable ignore6) {
                        try {
                            // 1.11
                            class_EntityArrow_lifeField = class_EntityArrow.getDeclaredField("ax");
                        } catch (Throwable ignore5) {
                            try {
                                // 1.10
                                class_EntityArrow_lifeField = class_EntityArrow.getDeclaredField("ay"); // ayyyyy lmao
                            } catch (Throwable ignore4) {
                                setLegacy();
                                // 1.8.3
                                class_EntityArrow_lifeField = class_EntityArrow.getDeclaredField("ar");
                            }
                        }
                    }
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "Could not find arrow lifetime field, setting arrow lifespan will not work");
                class_EntityArrow_lifeField = null;
            }
            if (class_EntityArrow_lifeField != null)
            {
                class_EntityArrow_lifeField.setAccessible(true);
            }

            try {
                // 1.13 and up
                try {
                    class_EntityDamageSource_setThornsMethod = class_EntityDamageSource.getMethod("x");
                    if (!class_EntityDamageSource_setThornsMethod.getReturnType().isAssignableFrom(class_EntityDamageSource)) {
                        throw new Exception ("Wrong return type");
                    }
                } catch (Throwable not13) {
                    // 1.9 and up
                    class_EntityDamageSource_setThornsMethod = class_EntityDamageSource.getMethod("w");
                    if (!class_EntityDamageSource_setThornsMethod.getReturnType().isAssignableFrom(class_EntityDamageSource)) {
                        throw new Exception ("Wrong return type");
                    }
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, thorn damage override to hurt ender dragon will not work");
                //getLogger().log(Level.WARNING, "An error occurred, thorn damage override to hurt ender dragon will not work", ex);
                class_EntityDamageSource_setThornsMethod = null;
            }

            try {
                try {
                    // 1.12
                    class_Entity_getTypeMethod = class_Entity.getDeclaredMethod("getSaveID");
                    class_Entity_saveMethod = class_Entity.getMethod("save", class_NBTTagCompound);
                } catch (Throwable not12) {
                    try {
                        // 1.10 and 1.11
                        class_Entity_getTypeMethod = class_Entity.getDeclaredMethod("at");
                    } catch (Throwable ignore) {
                        // 1.9 and earlier
                        setLegacy();
                        class_Entity_getTypeMethod = class_Entity.getDeclaredMethod("as");
                    }
                    class_Entity_saveMethod = class_Entity.getMethod("e", class_NBTTagCompound);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, saving entities to spawn eggs will not work");
                //getLogger().log(Level.WARNING, "An error occurred, saving entities to spawn eggs will not work", ex);
                class_Entity_getTypeMethod = null;
                class_Entity_saveMethod = null;
            }
            if (class_Entity_getTypeMethod != null) {
                class_Entity_getTypeMethod.setAccessible(true);
            }

            try {
                try {
                    // 1.11
                    class_ItemStack_consructor = class_ItemStack.getDeclaredConstructor(class_NBTTagCompound);
                    class_ItemStack_consructor.setAccessible(true);
                } catch (Throwable ignore) {
                    // 1.10 and earlier
                    setLegacy();
                    class_ItemStack_createStackMethod = class_ItemStack.getMethod("createStack", class_NBTTagCompound);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, restoring inventories from schematics will not work");
                //getLogger().log(Level.WARNING, "An error occurred, restoring inventories from schematics will not work", ex);
                class_ItemStack_createStackMethod = null;
            }

            try {
                class_EntityArrow_damageField = class_EntityArrow.getDeclaredField("damage");
                class_EntityArrow_damageField.setAccessible(true);
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, setting arrow damage will not work");
                //getLogger().log(Level.WARNING, "An error occurred, setting arrow damage will not work", ex);
                class_EntityArrow_damageField = null;
            }

            // TODO: setSilent API in 1.11+
            try {
                try {
                    // 1.10 and 1.11
                    class_Entity_setSilentMethod = class_Entity.getDeclaredMethod("setSilent", Boolean.TYPE);
                    class_Entity_isSilentMethod = class_Entity.getDeclaredMethod("isSilent");
                } catch (Throwable ignore) {
                    // 1.9 and earlier
                    setLegacy();
                    class_Entity_setSilentMethod = class_Entity.getDeclaredMethod("c", Boolean.TYPE);
                    class_Entity_isSilentMethod = class_Entity.getDeclaredMethod("ad");
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, silent entities will not work");
                //getLogger().log(Level.WARNING, "An error occurred, silent entities will not work", ex);
                class_Entity_setSilentMethod = null;
                class_Entity_isSilentMethod = null;
            }

            // TODO: ArmorStand.setGravity in 1.11+
            // Different behavior, but less hacky.
            try {
                try {
                    // 1.10 and 1.11
                    class_Entity_setNoGravity = class_Entity.getDeclaredMethod("setNoGravity", Boolean.TYPE);
                } catch (Throwable ignore) {
                    // 1.9 and earlier
                    setLegacy();
                    class_ArmorStand_setGravity = class_EntityArmorStand.getDeclaredMethod("setGravity", Boolean.TYPE);
                }
            } catch (Throwable ex) {
                getLogger().log(Level.WARNING, "An error occurred, hacky no-gravity armor stands won't work", ex);
                class_Entity_setNoGravity = null;
                class_ArmorStand_setGravity = null;
            }

            // TODO ItemStack.isEmpty in 1.11+
            try {
                // 1.11
                class_ItemStack_isEmptyMethod = class_ItemStack.getMethod("isEmpty");
            } catch (Throwable ignore) {
                // 1.10 and earlier
                setLegacy();
            }

            // Auto block state creation
            try {
                class_CraftBlock = fixBukkitClass("block.CraftBlock");
                class_CraftBlock_getNMSBlockMethod = class_CraftBlock.getDeclaredMethod("getNMSBlock");
                class_CraftBlock_getNMSBlockMethod.setAccessible(true);
                class_BlockActionContext = fixBukkitClass("BlockActionContext", true, "world.item.context");
                class_Block_getPlacedStateMethod = class_Block.getMethod("getPlacedState", class_BlockActionContext);
                class_EnumHand = (Class<Enum>)fixBukkitClass("EnumHand", true, "world");
                enum_EnumHand_MAIN_HAND = Enum.valueOf(class_EnumHand, "MAIN_HAND");
                class_MovingObjectPositionBlock = fixBukkitClass("MovingObjectPositionBlock", true, "world.phys");
                class_BlockActionContext_constructor = class_BlockActionContext.getDeclaredConstructor(class_World, class_EntityHuman, class_EnumHand, class_ItemStack, class_MovingObjectPositionBlock);
                class_BlockActionContext_constructor.setAccessible(true);
                class_MovingObjectPositionBlock_createMethod = class_MovingObjectPositionBlock.getMethod("a", class_Vec3D, class_EnumDirection, class_BlockPosition);
                if (!class_MovingObjectPositionBlock_createMethod.getReturnType().isAssignableFrom(class_MovingObjectPositionBlock)) {
                    throw new Exception("MovingObjectPositionBlock factory returns wrong type");
                }
                class_CraftBlock_setTypeAndDataMethod = class_CraftBlock.getMethod("setTypeAndData", class_IBlockData, Boolean.TYPE);
                class_nms_Block_getBlockDataMethod = class_Block.getMethod("getBlockData");
            } catch (Throwable ex) {
                class_CraftBlock = null;
                getLogger().log(Level.WARNING, "Could not bind to auto block state methods");
            }
        }
        catch (Throwable ex) {
            failed = true;
            getLogger().log(Level.SEVERE, "An unexpected error occurred initializing Magic");
            //getLogger().log(Level.SEVERE, "An unexpected error occurred initializing Magic", ex);
        }

        return !failed;
    }

    public static boolean getFailed() {
        return failed;
    }

    private static void setLegacy() {
        legacy = true;
        // Thread.dumpStack();
    }
    
    public static boolean isLegacy() {
        return legacy;
    }

    public static boolean isCurrentVersion() {
        return isModernVersion;
    }

    public static Class<?> getClass(String className) {
        Class<?> result = null;
        try {
            result = NMSUtils.class.getClassLoader().loadClass(className);
        } catch (Exception ex) {
            result = null;
        }

        return result;
    }

    public static Class<?> fixBukkitClass(String className, boolean nms, String package17) throws ClassNotFoundException {
        if (nms) {
            className = (version < 17 ? npack : "net.minecraft." + package17) + "." + className;
        } else {
            className = cpack + className;
        }

        return NMSUtils.class.getClassLoader().loadClass(className);
    }

    public static Class<?> fixBukkitClass(String className, boolean nms) throws ClassNotFoundException {
        return fixBukkitClass(className, nms, null);
    }

    public static Class<?> fixBukkitClass(String className) throws ClassNotFoundException {
        return NMSUtils.class.getClassLoader().loadClass(className);
    }

    public static Object getHandle(org.bukkit.Server server) {
        Object handle = null;
        try {
            handle = class_CraftServer_getServerMethod.invoke(server);
        } catch (Throwable ex) {
            handle = null;
        }
        return handle;
    }

    public static Object getHandle(org.bukkit.inventory.ItemStack stack) {
        Object handle = null;
        try {
            handle = class_CraftItemStack_getHandleField.get(stack);
        } catch (Throwable ex) {
            handle = null;
        }
        return handle;
    }

    public static Object getHandle(org.bukkit.World world) {
        if (world == null) return null;
        Object handle = null;
        try {
            handle = class_CraftWorld_getHandleMethod.invoke(world);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return handle;
    }

    public static Object getHandle(org.bukkit.entity.Entity entity) {
        if (entity == null) return null;
        Object handle = null;
        try {
            handle = class_CraftEntity_getHandleMethod.invoke(entity);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return handle;
    }

    public static Object getHandle(org.bukkit.entity.LivingEntity entity) {
        if (entity == null) return null;
        Object handle = null;
        try {
            handle = class_CraftLivingEntity_getHandleMethod.invoke(entity);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return handle;
    }

    // Here to support older versions of MagicWorlds
    @Deprecated
    public static boolean isDone(org.bukkit.Chunk chunk) {
        return isReady(chunk);
    }

    public static boolean isReady(org.bukkit.Chunk chunk) {
        if (class_Chunk_isReadyMethod == null) return true;

        Object chunkHandle = getHandle(chunk);
        boolean ready = true;
        try {
            ready = (Boolean)class_Chunk_isReadyMethod.invoke(chunkHandle);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return ready;
    }

    public static Object getHandle(org.bukkit.Chunk chunk) {
        Object handle = null;
        try {
            handle = class_CraftChunk_getHandleMethod.invoke(chunk);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return handle;
    }

    public static Object getHandle(org.bukkit.entity.Player player) {
        Object handle = null;
        try {
            handle = class_CraftPlayer_getHandleMethod.invoke(player);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return handle;
    }

    protected static void sendPacket(Server server, Location source, Collection<? extends Player> players, Object packet) throws Exception  {
        players = ((players != null && players.size() > 0) ? players : server.getOnlinePlayers());

        int viewDistance = Bukkit.getServer().getViewDistance() * 16;
        int viewDistanceSquared =  viewDistance * viewDistance;
        World sourceWorld = source.getWorld();
        for (Player player : players) {
            Location location = player.getLocation();
            if (!location.getWorld().equals(sourceWorld)) continue;
            if (location.distanceSquared(source) <= viewDistanceSquared) {
                sendPacket(player, packet);
            }
        }
    }

    protected static void sendPacket(Player player, Object packet) throws Exception {
        Object playerHandle = getHandle(player);
        Field connectionField = playerHandle.getClass().getField("playerConnection");
        Object connection = connectionField.get(playerHandle);
        Method sendPacketMethod = connection.getClass().getMethod("sendPacket", class_Packet);
        sendPacketMethod.invoke(connection, packet);
    }
    
    public static int getFacing(BlockFace direction)
    {
        int dir;
        switch (direction) {
        case SOUTH:
        default:
            dir = 0;
            break;
        case WEST:
            dir = 1;
            break;
        case NORTH:
            dir = 2;
            break;
        case EAST:
            dir = 3;
            break;
        }
        
        return dir;
    }

    public static org.bukkit.entity.Entity getBukkitEntity(Object entity)
    {
        if (entity == null) return null;
        try {
            Method getMethod = entity.getClass().getMethod("getBukkitEntity");
            Object bukkitEntity = getMethod.invoke(entity);
            if (!(bukkitEntity instanceof org.bukkit.entity.Entity)) return null;
            return (org.bukkit.entity.Entity)bukkitEntity;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Object getTag(Object mcItemStack) {
        Object tag = null;
        try {
            tag = class_ItemStack_getTag.invoke(mcItemStack);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return tag;
    }

    protected static Object getNMSCopy(ItemStack stack) {
        Object nms = null;
        try {
            nms = class_CraftItemStack_copyMethod.invoke(null, stack);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return nms;
    }

    public static ItemStack getCopy(ItemStack stack) {
        if (stack == null) return null;
        if (class_CraftItemStack_mirrorMethod == null) return stack;

        try {
            Object craft = getNMSCopy(stack);
            stack = (ItemStack)class_CraftItemStack_mirrorMethod.invoke(null, craft);
        } catch (Throwable ex) {
            stack = null;
        }

        return stack;
    }

    public static ItemStack makeReal(ItemStack stack) {
        if (stack == null) return null;
        Object nmsStack = getHandle(stack);
        if (nmsStack == null) {
            stack = getCopy(stack);
            nmsStack = getHandle(stack);
        }
        if (nmsStack == null) {
            return null;
        }
        try {
            Object tag = class_ItemStack_getTag.invoke(nmsStack);
            if (tag == null) {
                class_ItemStack_setTag.invoke(nmsStack, class_NBTTagCompound_constructor.newInstance());
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }

        return stack;
    }

    public static String getMetaString(ItemStack stack, String tag, String defaultValue) {
        String result = getMetaString(stack, tag);
        return result == null ? defaultValue : result;
    }

    public static boolean hasMeta(ItemStack stack, String tag) {
        if (NMSUtils.isEmpty(stack)) return false;
        return getNode(stack, tag) != null;
    }

    public static Object getTag(ItemStack itemStack) {
        Object tag = null;
        try {
            Object mcItemStack = getHandle(itemStack);
            if (mcItemStack == null) return null;
            tag = class_ItemStack_getTag.invoke(mcItemStack);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return tag;
    }

    public static Object getNode(ItemStack stack, String tag) {
        if (NMSUtils.isEmpty(stack)) return null;
        Object meta = null;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return null;
            Object tagObject = getTag(craft);
            if (tagObject == null) return null;
            meta = class_NBTTagCompound_getMethod.invoke(tagObject, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static boolean containsNode(Object nbtBase, String tag) {
        if (nbtBase == null) return false;
        Boolean result = false;
        try {
            result = (Boolean)class_NBTTagCompound_hasKeyMethod.invoke(nbtBase, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Object getNode(Object nbtBase, String tag) {
        if (nbtBase == null) return null;
        Object meta = null;
        try {
            meta = class_NBTTagCompound_getMethod.invoke(nbtBase, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static Object createNode(Object nbtBase, String tag) {
        if (nbtBase == null) return null;
        Object meta = null;
        try {
            meta = class_NBTTagCompound_getCompoundMethod.invoke(nbtBase, tag);
            class_NBTTagCompound_setMethod.invoke(nbtBase, tag, meta);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static Object createNode(ItemStack stack, String tag) {
        if (NMSUtils.isEmpty(stack)) return null;
        Object outputObject = getNode(stack, tag);
        if (outputObject == null) {
            try {
                Object craft = getHandle(stack);
                if (craft == null) return null;
                Object tagObject = getTag(craft);
                if (tagObject == null) {
                    tagObject = class_NBTTagCompound_constructor.newInstance();
                    class_ItemStack_setTag.invoke(craft, tagObject);
                }
                outputObject = class_NBTTagCompound_constructor.newInstance();
                class_NBTTagCompound_setMethod.invoke(tagObject, tag, outputObject);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return outputObject;
    }

    public static String getMetaString(Object node, String tag, String defaultValue) {
        String meta = getMetaString(node, tag);
        return meta == null || meta.length() == 0 ? defaultValue : meta;
    }

    public static String getMetaString(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        String meta = null;
        try {
            meta = (String)class_NBTTagCompound_getStringMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static String getMeta(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        String meta = null;
        try {
            meta = (String)class_NBTTagCompound_getStringMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static Byte getMetaByte(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        Byte meta = null;
        try {
            meta = (Byte)class_NBTTagCompound_getByteMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static Integer getMetaInt(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        Integer meta = null;
        try {
            meta = (Integer)class_NBTTagCompound_getIntMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static int getMetaInt(ItemStack stack, String tag, int defaultValue) {
        if (NMSUtils.isEmpty(stack)) return defaultValue;
        int result = defaultValue;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return defaultValue;
            Object tagObject = getTag(craft);
            if (tagObject == null) return defaultValue;
            Integer value = getMetaInt(tagObject, tag);
            result = value == null ? defaultValue : value;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Double getMetaDouble(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        Double meta = null;
        try {
            meta = (Double)class_NBTTagCompound_getDoubleMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static Boolean getMetaBoolean(Object node, String tag) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return null;
        Boolean meta = null;
        try {
            meta = (Boolean)class_NBTTagCompound_getBooleanMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static void setMeta(Object node, String tag, String value) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            if (value == null || value.length() == 0) {
                class_NBTTagCompound_removeMethod.invoke(node, tag);
            } else {
                class_NBTTagCompound_setStringMethod.invoke(node, tag, value);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaLong(Object node, String tag, long value) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            class_NBTTagCompound_setLongMethod.invoke(node, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaBoolean(Object node, String tag, boolean value) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            class_NBTTagCompound_setBooleanMethod.invoke(node, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaDouble(Object node, String tag, double value) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            class_NBTTagCompound_setDoubleMethod.invoke(node, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaInt(Object node, String tag, int value) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            class_NBTTagCompound_setIntMethod.invoke(node, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaInt(ItemStack stack, String tag, int value) {
        if (NMSUtils.isEmpty(stack)) return;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;
            setMetaInt(tagObject, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void removeMeta(Object node, String tag) {
        if (node == null|| !class_NBTTagCompound.isInstance(node)) return;
        try {
            class_NBTTagCompound_removeMethod.invoke(node, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void removeMeta(ItemStack stack, String tag) {
        if (NMSUtils.isEmpty(stack)) return;

        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;
            removeMeta(tagObject, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaTyped(Object node, String tag, String value) {
        if (value == null) {
            removeMeta(node, tag);
            return;
        }

        boolean isTrue = value.equals("true");
        boolean isFalse = value.equals("false");
        if (isTrue || isFalse) {
            setMetaBoolean(node, tag, isTrue);
        } else {
            try {
                Integer i = Integer.parseInt(value);
                setMetaInt(node, tag, i);
            } catch (Exception ex) {
                try {
                    Double d = Double.parseDouble(value);
                    setMetaDouble(node, tag, d);
                } catch (Exception ex2) {
                    setMeta(node, tag, value);
                }
            }
        }
    }

    public static void setMetaNode(Object node, String tag, Object child) {
        if (node == null || !class_NBTTagCompound.isInstance(node)) return;
        try {
            if (child == null) {
                class_NBTTagCompound_removeMethod.invoke(node, tag);
            } else {
                class_NBTTagCompound_setMethod.invoke(node, tag, child);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static boolean setMetaNode(ItemStack stack, String tag, Object child) {
        if (NMSUtils.isEmpty(stack)) return false;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return false;
            Object node = getTag(craft);
            if (node == null) return false;
            if (child == null) {
                class_NBTTagCompound_removeMethod.invoke(node, tag);
            } else {
                class_NBTTagCompound_setMethod.invoke(node, tag, child);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    public static String getMetaString(ItemStack stack, String tag) {
        if (NMSUtils.isEmpty(stack)) return null;
        String meta = null;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return null;
            Object tagObject = getTag(craft);
            if (tagObject == null) return null;
            meta = (String)class_NBTTagCompound_getStringMethod.invoke(tagObject, tag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return meta;
    }

    public static void setMeta(ItemStack stack, String tag, String value) {
        if (NMSUtils.isEmpty(stack)) return;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;
            class_NBTTagCompound_setStringMethod.invoke(tagObject, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void setMetaBoolean(ItemStack stack, String tag, boolean value) {
        if (NMSUtils.isEmpty(stack)) return;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;
            setMetaBoolean(tagObject, tag, value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static boolean getMetaBoolean(ItemStack stack, String tag, boolean defaultValue) {
        if (NMSUtils.isEmpty(stack)) return defaultValue;
        boolean result = defaultValue;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return defaultValue;
            Object tagObject = getTag(craft);
            if (tagObject == null) return defaultValue;
            Boolean value = getMetaBoolean(tagObject, tag);
            result = value == null ? defaultValue : value;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void addGlow(ItemStack stack) {
        if (NMSUtils.isEmpty(stack)) return;

        try {
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            stack.setItemMeta(meta);
       } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void removeGlow(ItemStack stack) {
        if (NMSUtils.isEmpty(stack)) return;

        try {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasEnchant(Enchantment.LUCK)) {
                meta.removeEnchant(Enchantment.LUCK);
                stack.setItemMeta(meta);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isUnbreakable(ItemStack stack) {
        if (NMSUtils.isEmpty(stack)) return false;
        Boolean unbreakableFlag = null;
        try {
            Object craft = getHandle(stack);
            if (craft == null) return false;
            Object tagObject = getTag(craft);
            if (tagObject == null) return false;
            unbreakableFlag = getMetaBoolean(tagObject, "Unbreakable");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        
        return unbreakableFlag != null && unbreakableFlag;
    }

    public static void makeUnbreakable(ItemStack stack) {
        if (NMSUtils.isEmpty(stack)) return;

        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;

            Object unbreakableFlag = null;
            unbreakableFlag = class_NBTTagByte_constructor.newInstance((byte) 1);
            class_NBTTagCompound_setMethod.invoke(tagObject, "Unbreakable", unbreakableFlag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void removeUnbreakable(ItemStack stack) {
        removeMeta(stack, "Unbreakable");
    }

    public static void hideFlags(ItemStack stack, int flags) {
        if (NMSUtils.isEmpty(stack)) return;

        try {
            Object craft = getHandle(stack);
            if (craft == null) return;
            Object tagObject = getTag(craft);
            if (tagObject == null) return;

            Object hideFlag = null;
            hideFlag = class_NBTTagInt_constructor.newInstance(flags);
            class_NBTTagCompound_setMethod.invoke(tagObject, "HideFlags", hideFlag);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static boolean createExplosion(Entity entity, World world, double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        boolean result = false;
        if (world == null) return false;
        if (class_World_explodeMethod == null) {
            return world.createExplosion(x, y, z, power, setFire, breakBlocks);
        }
        try {
            Object worldHandle = getHandle(world);
            if (worldHandle == null) return false;
            Object entityHandle = entity == null ? null : getHandle(entity);

            Object explosion = class_EnumExplosionEffect != null ?
                    class_World_explodeMethod.invoke(worldHandle, entityHandle, x, y, z, power, setFire, breakBlocks ? enum_ExplosionEffect_BREAK : enum_ExplosionEffect_NONE) :
                    class_World_explodeMethod.invoke(worldHandle, entityHandle, x, y, z, power, setFire, breakBlocks);
            Field cancelledField = explosion.getClass().getDeclaredField("wasCanceled");
            result = (Boolean)cancelledField.get(explosion);
        } catch (Throwable ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public static void makeTemporary(ItemStack itemStack, String message) {
        setMeta(itemStack, "temporary", message);
    }

    public static boolean isTemporary(ItemStack itemStack) {
        return hasMeta(itemStack, "temporary");
    }

    public static void makeUnplaceable(ItemStack itemStack) {
        setMeta(itemStack, "unplaceable", "true");
    }
    
    public static void removeUnplaceable(ItemStack itemStack) {
        removeMeta(itemStack, "unplaceable");
    }

    public static boolean isUnplaceable(ItemStack itemStack) {
        return hasMeta(itemStack, "unplaceable");
    }

    public static String getTemporaryMessage(ItemStack itemStack) {
        return getMetaString(itemStack, "temporary");
    }

    public static void setReplacement(ItemStack itemStack, ItemStack replacement) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("item", replacement);
        setMeta(itemStack, "replacement", configuration.saveToString());
    }

    public static ItemStack getReplacement(ItemStack itemStack) {
        String serialized = getMetaString(itemStack, "replacement");
        if (serialized == null || serialized.isEmpty()) {
            return null;
        }
        YamlConfiguration configuration = new YamlConfiguration();
        ItemStack replacement = null;
        try {
            configuration.loadFromString(serialized);
            replacement = configuration.getItemStack("item");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return replacement;
    }

    protected static Object getTagString(String value) {
        try {
            return class_NBTTagString_consructor.newInstance(value);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public static Object setStringList(Object nbtBase, String tag, Collection<String> values) {
        if (nbtBase == null) return null;
        Object listMeta = null;
        try {
            listMeta = class_NBTTagList_constructor.newInstance();

            for (String value : values) {
                Object nbtString = getTagString(value);
                addToList(listMeta, nbtString);
            }

            class_NBTTagCompound_setMethod.invoke(nbtBase, tag, listMeta);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
        return listMeta;
    }

    public static ItemStack getItem(Object itemTag) {
        if (itemTag == null) return null;
        ItemStack item = null;
        try {
            Object nmsStack = null;
            if (class_ItemStack_consructor != null) {
                nmsStack = class_ItemStack_consructor.newInstance(itemTag);
            } else {
                nmsStack = class_ItemStack_createStackMethod.invoke(null, itemTag);
            }
            item = (ItemStack)class_CraftItemStack_mirrorMethod.invoke(null, nmsStack);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static ItemStack[] getItems(Object rootTag, String tagName) {
        try {
            Object itemList = class_NBTTagCompound_getListMethod.invoke(rootTag, tagName, NBT_TYPE_COMPOUND);
            Integer size = (Integer)class_NBTTagList_sizeMethod.invoke(itemList);
            ItemStack[] items = new ItemStack[size];
            for (int i = 0; i < size; i++) {
                try {
                    Object itemData = class_NBTTagList_getMethod.invoke(itemList, i);
                    if (itemData != null) {
                        items[i] = getItem(itemData);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object getTileEntityData(Location location) {
       if (class_TileEntity_saveMethod == null) return null;
        Object tileEntity = getTileEntity(location);
        if (tileEntity == null) return null;
        Object data = null;
        try {
            data = class_NBTTagCompound_constructor.newInstance();
            class_TileEntity_saveMethod.invoke(tileEntity, data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static Object getTileEntity(Location location) {
        if (class_World_getTileEntityMethod != null) {
            Object tileEntity = null;
            try {
                World world = location.getWorld();
                Object blockLocation = class_BlockPosition_Constructor.newInstance(location.getX(), location.getY(), location.getZ());
                tileEntity = class_World_getTileEntityMethod.invoke(getHandle(world), blockLocation);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return tileEntity;
        }

        if (class_CraftWorld_getTileEntityAtMethod == null) return null;
        Object tileEntity = null;
        try {
            World world = location.getWorld();
            tileEntity = class_CraftWorld_getTileEntityAtMethod.invoke(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tileEntity;
    }

    public static void clearItems(Location location) {
        if (class_TileEntity_loadMethod == null || class_TileEntity_updateMethod == null || class_TileEntity_saveMethod == null) return;
        if (location == null) return;
        Object tileEntity = getTileEntity(location);
        if (tileEntity == null) return;
        try {
            Object entityData = class_NBTTagCompound_constructor.newInstance();
            class_TileEntity_saveMethod.invoke(tileEntity, entityData);
            Object itemList = class_NBTTagCompound_getListMethod.invoke(entityData, "Items", NBT_TYPE_COMPOUND);
            if (itemList != null) {
                List items = (List)class_NBTTagList_list.get(itemList);
                items.clear();
            }
            class_NBTTagCompound_removeMethod.invoke(entityData,"Item");
            if (class_IBlockData != null) {
                Object worldHandle = getHandle(location.getWorld());
                Object blockLocation = class_BlockPosition_Constructor.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                Object blockType = class_World_getTypeMethod.invoke(worldHandle, blockLocation);
                class_TileEntity_loadMethod.invoke(tileEntity, blockType, entityData);
            } else {
                class_TileEntity_loadMethod.invoke(tileEntity, entityData);
            }
            class_TileEntity_updateMethod.invoke(tileEntity);

            if (class_Lootable_setLootTableMethod != null && class_Lootable != null) {
                Block block = location.getBlock();
                BlockState blockState = block.getState();
                if (class_Lootable.isAssignableFrom(blockState.getClass())) {
                    class_Lootable_setLootTableMethod.invoke(blockState, new Object[]{ null });
                    blockState.update();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setTileEntityData(Location location, Object data) {
        if (class_TileEntity_loadMethod == null || class_TileEntity_updateMethod == null) return;
        if (location == null || data == null) return;
        Object tileEntity = getTileEntity(location);
        if (tileEntity == null) return;
        try {
            class_NBTTagCompound_setIntMethod.invoke(data, "x", location.getBlockX());
            class_NBTTagCompound_setIntMethod.invoke(data, "y", location.getBlockY());
            class_NBTTagCompound_setIntMethod.invoke(data, "z", location.getBlockZ());

            if (class_IBlockData != null) {
                Object worldHandle = getHandle(location.getWorld());
                Object blockLocation = class_BlockPosition_Constructor.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                Object blockType = class_World_getTypeMethod.invoke(worldHandle, blockLocation);
                class_TileEntity_loadMethod.invoke(tileEntity, blockType, data);
            } else {
                class_TileEntity_loadMethod.invoke(tileEntity, data);
            }
            class_TileEntity_updateMethod.invoke(tileEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Vector getPosition(Object entityData, String tag) {
        if (class_NBTTagList_getDoubleMethod == null) return null;
        try {
            Object posList = class_NBTTagCompound_getListMethod.invoke(entityData, tag, NBT_TYPE_DOUBLE);
            Double x = (Double)class_NBTTagList_getDoubleMethod.invoke(posList, 0);
            Double y = (Double)class_NBTTagList_getDoubleMethod.invoke(posList, 1);
            Double z = (Double)class_NBTTagList_getDoubleMethod.invoke(posList, 2);
            if (x != null && y != null && z != null) {
                return new Vector(x, y, z);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static BlockVector getBlockVector(Object entityData, String tag) {
        if (class_NBTTagCompound_getIntArrayMethod == null) return null;
        try {
            int[] coords = (int[])class_NBTTagCompound_getIntArrayMethod.invoke(entityData, tag);
            if (coords == null || coords.length < 3) return null;
            return new BlockVector(coords[0], coords[1], coords[2]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setEnvironment(World world, World.Environment environment) {
        try {
            class_CraftWorld_environmentField.set(world, environment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void playCustomSound(Player player, Location location, String sound, float volume, float pitch)
    {
        if (class_PacketPlayOutCustomSoundEffect_Constructor == null || sound == null) return;
        try {
            Object packet = null;
            if (class_MinecraftKey_constructor != null) {
                Object key = class_MinecraftKey_constructor.newInstance(sound);
                Object vec = class_Vec3D_constructor.newInstance(location.getX(), location.getY(), location.getZ());
                packet = class_PacketPlayOutCustomSoundEffect_Constructor.newInstance(key, enum_SoundCategory_PLAYERS, vec, volume, pitch);
            } else {
                packet = class_PacketPlayOutCustomSoundEffect_Constructor.newInstance(sound, enum_SoundCategory_PLAYERS, location.getX(), location.getY(), location.getZ(), volume, pitch);
            }
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map<String, Object> getMap(ConfigurationSection section) {
        return getTypedMap(section);
    }

    public static <T> Map<String, T> getTypedMap(ConfigurationSection section)
    {
        if (section == null) return null;
        if (section instanceof MemorySection && class_MemorySection_mapField != null)
        {
            try {
                Object mapObject = class_MemorySection_mapField.get(section);
                if (mapObject instanceof Map) {
                    return (Map<String, T>)mapObject;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Do it the slow way
        Map<String, T> map = new HashMap<>();
        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            map.put(key, (T)section.get(key));
        }
        
        return map;
    }

    public static boolean setMap(ConfigurationSection section, Map<String, Object> map)
    {
        if (section == null || class_MemorySection_mapField == null) return false;
        if (section instanceof MemorySection)
        {
            try {
                class_MemorySection_mapField.set(section, map);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static boolean isEmpty(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return true;
        if (class_ItemStack_isEmptyMethod == null) return false;
        try {
            Object handle = getHandle(itemStack);
            if (handle == null) return false;
            return (Boolean)class_ItemStack_isEmptyMethod.invoke(handle);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String getVersionPrefix() {
        return versionPrefix;
    }

    public static byte getBlockData(FallingBlock falling) {
        // @deprecated Magic value
        byte data = 0;
        try {
            if (class_FallingBlock_getBlockDataMethod != null) {
                data = (byte)class_FallingBlock_getBlockDataMethod.invoke(falling);
            }
        } catch (Exception ignore) {

        }
        return data;
    }

    public static MapView getMapById(int id) {
        if (class_Bukkit_getMapMethod == null) return null;
        try {
            if (legacyMaps) {
                return (MapView)class_Bukkit_getMapMethod.invoke(null, (short)id);
            }
            return (MapView)class_Bukkit_getMapMethod.invoke(null, (short)id);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected static void addToList(Object listObject, Object node) throws InvocationTargetException, IllegalAccessException {
        if (isCurrentVersion) {
            int size = (Integer)class_NBTTagList_sizeMethod.invoke(listObject);
            class_NBTTagList_addMethod.invoke(listObject, size, node);
        } else {
            class_NBTTagList_addMethod.invoke(listObject, node);
        }
    }

    public static boolean isPublic(Field field) {
        if (field == null) return false;
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers);
    }

    public static List<Entity> selectEntities(CommandSender sender, String selector) {
        if (class_Bukkit_selectEntitiesMethod == null) return null;
        if (!selector.startsWith("@")) return null;
        try {
            return (List<Entity>)class_Bukkit_selectEntitiesMethod.invoke(null, sender, selector);
        } catch (Throwable ex) {
            if (logger != null) {
                getLogger().warning("Invalid selector: " + ex.getMessage());
            }
        }
        return null;
    }

    public static boolean hasStatistics() {
        return hasStatistics;
    }

    public static boolean hasEntityTransformEvent() {
        return hasEntityTransformEvent;
    }

    public static boolean hasTimeSkipEvent() {
        return hasTimeSkipEvent;
    }

    protected static Logger getLogger() {
        return logger == null ? Bukkit.getLogger() : logger;
    }
}
