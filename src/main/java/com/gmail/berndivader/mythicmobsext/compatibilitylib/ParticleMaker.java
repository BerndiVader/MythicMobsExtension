package com.gmail.berndivader.mythicmobsext.compatibilitylib;

import io.lumine.xikage.mythicmobs.util.ReflectionUtils;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;

public class ParticleMaker {

    public static final class ParticlePacket {
        private static int version;
        private Class<?> enumParticle;
        private Constructor<?> packetConstructor;
        private Method getHandle;
        private Field playerConnection;
        private Method sendPacket;
        private boolean initialized;
        private ParticleType particle;
        private int[] particleData;
        private float offsetX;
        private float offsetY;
        private float offsetZ;
        private float speed;
        private int amount;
        private final boolean longDistance;
        private Object packet;

        public ParticlePacket(String name, Color color, float speed, int amount, boolean longDistance) throws IllegalArgumentException {
            this(name, color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1.0f, 0, longDistance);
            this.offsetX = (float)color.getRed() / 255.0f;
            this.offsetY = (float)color.getGreen() / 255.0f;
            this.offsetZ = (float)color.getBlue() / 255.0f;
            amount = 0;
            speed = 1.0f;
            if (this.offsetX < Float.MIN_NORMAL) {
                this.offsetX = Float.MIN_NORMAL;
            }
            switch (this.particle) {
                case SPELL_MOB: 
                case SPELL_MOB_AMBIENT: 
                case REDSTONE: 
                case NOTE: 
                    break;
                default: 
                    Main.logger.warning("Particle type " + this.particle.toString() + " cannot be colored");
                    amount=1;
            }
            try {
                this.packet=this.packetConstructor.newInstance(new Object[0]);
                if (version<8) {
                	ReflectionUtils.setValue(this.packet, true, "a", this.particle.getName());
                } else {
                    ReflectionUtils.setValue(this.packet, true, "a", this.enumParticle.getEnumConstants()[this.particle.getId()]);
                    ReflectionUtils.setValue(this.packet, true, "j", longDistance);
                    if (this.particleData != null) {
                        int[] packetData = this.particleData;
                        ReflectionUtils.setValue(this.packet, true, "k", packetData);
                    }
                }
                ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(this.offsetX));
                ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(this.offsetY));
                ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(this.offsetZ));
                ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(speed));
                ReflectionUtils.setValue(this.packet, true, "i", amount);
            }
            catch (Exception exception) {
                throw new PacketInstantiationException("Packet instantiation failed", exception);
            }
        }

        public ParticlePacket(String name, Vector vector, float speed, int amount, boolean longDistance) throws IllegalArgumentException {
            this(name, (float)vector.getX(), (float)vector.getY(), (float)vector.getZ(), speed, 0, longDistance);
            amount = 0;
            switch (this.particle) {
	            case SPELL_MOB: 
	            case SPELL_MOB_AMBIENT: 
	            case REDSTONE: 
	            	Main.logger.warning("Particle type " + this.particle.toString() + " cannot be directional");
	                break;
				default:
					break;
            }
            try {
                this.packet = this.packetConstructor.newInstance(new Object[0]);
                if (version < 8) {
                    ReflectionUtils.setValue(this.packet, true, "a", this.particle.getName());
                } else {
                    ReflectionUtils.setValue(this.packet, true, "a", this.enumParticle.getEnumConstants()[this.particle.getId()]);
                    ReflectionUtils.setValue(this.packet, true, "j", longDistance);
                    if (this.particleData != null) {
                        int[] packetData = this.particleData;
                        ReflectionUtils.setValue(this.packet, true, "k", packetData);
                    }
                }
                ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(this.offsetX));
                ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(this.offsetY));
                ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(this.offsetZ));
                ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(speed));
                ReflectionUtils.setValue(this.packet, true, "i", amount);
            }
            catch (Exception exception) {
                throw new PacketInstantiationException("Packet instantiation failed", exception);
            }
        }

        public ParticlePacket(String name, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance) throws IllegalArgumentException {
            this.initialize();
            this.particle = ParticleType.get(name);
            if (this.particle == null) {
                Main.logger.warning("== Particle type " + name + " is null. Invalid particle type!");
            }
            if (name.contains("_")) {
                String[] split = name.split("_");
                name = split[0] + "_";
                if (split.length > 1) {
                    String[] split2 = split[1].split(":");
                    int[] data = new int[split2.length];
                    for (int i = 0; i < data.length; ++i) {
                        data[i] = Integer.parseInt(split2[i]);
                    }
                    this.particleData = data;
                }
            }
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
            this.longDistance = longDistance;
            try {
                this.packet = this.packetConstructor.newInstance(new Object[0]);
                if (version < 8) {
                    ReflectionUtils.setValue(this.packet, true, "a", this.particle.getName());
                } else {
                    ReflectionUtils.setValue(this.packet, true, "a", this.enumParticle.getEnumConstants()[this.particle.getId()]);
                    ReflectionUtils.setValue(this.packet, true, "j", longDistance);
                    if (this.particleData != null) {
                        int[] packetData = this.particleData;
                        ReflectionUtils.setValue(this.packet, true, "k", packetData);
                    }
                }
                ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(offsetX));
                ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(offsetY));
                ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(offsetZ));
                ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(speed));
                ReflectionUtils.setValue(this.packet, true, "i", amount);
            }
            catch (Exception packetData) {
                // empty catch block
            }
        }

        public void initialize() throws VersionIncompatibleException {
            if (this.initialized) {
                return;
            }
            try {
                String[] split = ReflectionUtils.PackageType.getServerVersion().split("_");
                version = Integer.parseInt(split[1]);
                if (version > 7) {
                    this.enumParticle = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
                }
                Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
                this.packetConstructor = ReflectionUtils.getConstructor(packetClass, new Class[0]);
                this.getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", new Class[0]);
                this.playerConnection = ReflectionUtils.getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, false, "playerConnection");
                this.sendPacket = ReflectionUtils.getMethod(this.playerConnection.getType(), "sendPacket", ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet"));
            }
            catch (Exception exception) {
                throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with this library", exception);
            }
            this.initialized = true;
        }

        public boolean isInitialized() {
            return this.initialized;
        }

        public void send(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
            try {
                ReflectionUtils.setValue(this.packet, true, "b", Float.valueOf((float)center.getX()));
                ReflectionUtils.setValue(this.packet, true, "c", Float.valueOf((float)center.getY()));
                ReflectionUtils.setValue(this.packet, true, "d", Float.valueOf((float)center.getZ()));
            }
            catch (Exception exception) {
                throw new PacketInstantiationException("Setting particle location failed", exception);
            }
            try {
                this.sendPacket.invoke(this.playerConnection.get(this.getHandle.invoke((Object)player, new Object[0])), this.packet);
            }
            catch (Exception exception) {
                throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
            }
        }

        public void send(Location center, List<Player> players) throws IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalArgumentException("The player list is empty");
            }
            for (Player player : players) {
                this.send(center, player);
            }
        }

        public void send(Location center, double range) throws IllegalArgumentException {
            if (range < 1.0) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(worldName) || player.getLocation().distanceSquared(center) > squared) continue;
                this.send(center, player);
            }
        }

        public void sendAsync(final Location center, final int viewDistance) {
            new BukkitRunnable(){
                public void run() {
                    try {
                        ReflectionUtils.setValue(ParticlePacket.this.packet, true, "b", Float.valueOf((float)center.getX()));
                        ReflectionUtils.setValue(ParticlePacket.this.packet, true, "c", Float.valueOf((float)center.getY()));
                        ReflectionUtils.setValue(ParticlePacket.this.packet, true, "d", Float.valueOf((float)center.getZ()));
                    }
                    catch (Exception exception) {
                        throw new PacketInstantiationException("Setting particle location failed", exception);
                    }
                    for (Player p:center.getWorld().getPlayers()) {
                        if (!p.getWorld().equals((Object)center.getWorld()) || p.getLocation().distanceSquared(center) > (double)viewDistance) continue;
                        try {
                            ParticlePacket.this.sendPacket.invoke(ParticlePacket.this.playerConnection.get(ParticlePacket.this.getHandle.invoke((Object)p, new Object[0])), ParticlePacket.this.packet);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }.runTaskAsynchronously(Main.getPlugin());
        }

        public void sendAsync(final List<Location> ll, final int viewDistance) {
            new BukkitRunnable(){

                public void run() {
                    for (Location center : ll) {
                        try {
                            ReflectionUtils.setValue(ParticlePacket.this.packet, true, "b", Float.valueOf((float)center.getX()));
                            ReflectionUtils.setValue(ParticlePacket.this.packet, true, "c", Float.valueOf((float)center.getY()));
                            ReflectionUtils.setValue(ParticlePacket.this.packet, true, "d", Float.valueOf((float)center.getZ()));
                        }
                        catch (Exception exception) {
                            throw new PacketInstantiationException("Setting particle location failed", exception);
                        }
                        for (Player p : center.getWorld().getPlayers()) {
                            if (!p.getWorld().equals((Object)center.getWorld()) || p.getLocation().distanceSquared(center) > (double)viewDistance) continue;
                            try {
                                ParticlePacket.this.sendPacket.invoke(ParticlePacket.this.playerConnection.get(ParticlePacket.this.getHandle.invoke((Object)p, new Object[0])), ParticlePacket.this.packet);
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(Main.getPlugin());
        }

        private static final class PacketSendingException
        extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketSendingException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        private static final class PacketInstantiationException
        extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketInstantiationException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        private static final class VersionIncompatibleException
        extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public VersionIncompatibleException(String message, Throwable cause) {
                super(message, cause);
            }
        }

    }

    public static enum ParticleType {
        EXPLODE("explode", 0, -1, "EXPLODE", "EXPLOSION", "EXPLOSION_SMALL"),
        EXPLOSION_LARGE("largeexplode", 1, -1, "largeexplosion", "EXPLOSION_LARGE"),
        EXPLOSION_HUGE("hugeexplosion", 2, -1, "hugeexplode", "EXPLOSION_HUGE"),
        FIREWORKS_SPARK("fireworksSpark", 3, -1, "FIREWORKS_SPARK"),
        WATER_BUBBLE("bubble", 4, -1, "WATER_BUBBLE"),
        WATER_SPLASH("splash", 5, -1, "WATER_SPLASH"),
        WATER_WAKE("wake", 6, 7, "WATER_WAKE"),
        SUSPENDED("suspended", 7, -1, new String[0]),
        SUSPENDED_DEPTH("depthSuspend", 8, -1, "SUSPENDED_DEPTH"),
        CRIT("crit", 9, -1, new String[0]),
        CRIT_MAGIC("magicCrit", 10, -1, "CRIT_MAGIC"),
        SMOKE_NORMAL("smoke", 11, -1, "SMOKE_NORMAL"),
        SMOKE_LARGE("largesmoke", 12, -1, "SMOKE_LARGE"),
        SPELL("spell", 13, -1, new String[0]),
        SPELL_INSTANT("instantSpell", 14, -1, "SPELL_INSTANT"),
        SPELL_MOB("mobSpell", 15, -1, "SPELL_MOB"),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, "SPELL_MOB_AMBIENT"),
        SPELL_WITCH("witchMagic", 17, -1, "SPELL_WITCH"),
        DRIP_WATER("dripWater", 18, -1, new String[0]),
        DRIP_LAVA("dripLava", 19, -1, new String[0]),
        VILLAGER_ANGRY("angryVillager", 20, -1, "VILLAGER_ANGRY"),
        VILLAGER_HAPPY("happyVillager", 21, -1, "VILLAGER_HAPPY"),
        TOWN_AURA("townaura", 22, -1, "TOWN_AURA"),
        NOTE("note", 23, -1, new String[0]),
        PORTAL("portal", 24, -1, new String[0]),
        ENCHANTMENT_TABLE("enchantmenttable", 25, -1, "ENCHANTMENT_TABLE", "enchanttable"),
        FLAME("flame", 26, -1, new String[0]),
        LAVA("lava", 27, -1, new String[0]),
        FOOTSTEP("footstep", 28, -1, new String[0]),
        CLOUD("cloud", 29, -1, new String[0]),
        REDSTONE("reddust", 30, -1, new String[0]),
        SNOWBALL("snowballpoof", 31, -1, new String[0]),
        SNOW_SHOVEL("snowshovel", 32, -1, "SNOW_SHOVEL"),
        SLIME("slime", 33, -1, new String[0]),
        HEART("heart", 34, -1, new String[0]),
        BARRIER("barrier", 35, 8, new String[0]),
        ITEM_CRACK("iconcrack", 36, -1, "ITEM_CRACK"),
        BLOCK_CRACK("blockcrack", 37, -1, new String[0]),
        BLOCK_DUST("blockdust", 38, 7, new String[0]),
        WATER_DROP("droplet", 39, 8, new String[0]),
        ITEM_TAKE("take", 40, 8, new String[0]),
        MOB_APPEARANCE("mobappearance", 41, 8, new String[0]),
        END_ROD("endRod", 43, 9, new String[0]),
        DRAGON_BREATH("dragonbreath", 42, 9, new String[0]),
        DAMAGE_INDICATOR("damageIndicator", 44, 9, new String[0]),
        SWEEP_ATTACK("sweepAttack", 45, 9, new String[0]),
        FALLING_DUST("fallingdust", 46, 10, new String[0]),
        TOTEM("totem", 47, 11, new String[0]),
        SPIT("spit", 48, 11, new String[0]);
        
        private static final Map<String, ParticleType> LOOKUP_MAP;
        private final String name;
        private final int id;
        private final int requiredVersion;
        private final List<String> aliases;

        private /* varargs */ ParticleType(String name, int id, int requiredVersion, String ... aliases) {
            if (name.contains("_")) {
                String[] split = name.split("_");
                name = split[0];
            }
            this.name = name;
            this.id = id;
            this.requiredVersion = requiredVersion;
            this.aliases = Arrays.asList(aliases);
        }

        public static ParticleType get(String s) {
            if (s.contains("_")) {
                String[] split = s.split("_");
                s = split[0];
            }
            s = s.toUpperCase();
            if (LOOKUP_MAP.containsKey(s)) {
                return LOOKUP_MAP.get(s);
            }
            return null;
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        static {
            LOOKUP_MAP = new HashMap<String, ParticleType>();
            for (ParticleType t : ParticleType.values()) {
                LOOKUP_MAP.put(t.name.toUpperCase(), t);
                for (String a : t.aliases) {
                    LOOKUP_MAP.put(a.toUpperCase(), t);
                }
            }
        }
    }

}

