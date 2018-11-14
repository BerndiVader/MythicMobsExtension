package com.gmail.berndivader.mythicmobsext.compatibilitylib;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;

public enum AbstractParticle {
    EXPLOSION_NORMAL("poof", "explode", "explosion", "explosion_small"),
    EXPLOSION_LARGE("largeexplode", "largeexplosion"),
    EXPLOSION_HUGE("explosion_emitter", "hugeexplode", "hugeexplosion"),
    FIREWORKS_SPARK("firework", "fireworksspark"),
    WATER_BUBBLE("bubble"),
    WATER_SPLASH("splash"),
    WATER_WAKE("fishing", "wake"),
    SUSPENDED("underwater"),
    SUSPENDED_DEPTH("underwater", "depthsuspend"),
    CRIT("crit"),
    CRIT_MAGIC("enchanted_hit", "magiccrit"),
    SMOKE_NORMAL("smoke"),
    SMOKE_LARGE("large_smoke", "largesmoke"),
    SPELL("effect"),
    SPELL_INSTANT("instant_effect", "instantSpell"),
    SPELL_MOB("entity_effect", "mobSpell"),
    SPELL_MOB_AMBIENT("ambient_entity_effect", "mobSpellAmbient"),
    SPELL_WITCH("witch", "witchMagic"),
    DRIP_WATER("dripping_water", "dripWater"),
    DRIP_LAVA("dripping_lava", "dripLava"),
    VILLAGER_ANGRY("angry_villager", "angryVillager"),
    VILLAGER_HAPPY("happy_villager", "happyVillager"),
    TOWN_AURA("mycelium", "townaura"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchant", "enchantmenttable", "enchantingtable"),
    FLAME("flame"),
    LAVA("lava"),
    CLOUD("cloud"),
    REDSTONE("dust", "reddust"),
    SNOWBALL("item_snowball", "snowballpoof"),
    SNOW_SHOVEL("item_snowball", "snowshovel"),
    SLIME("item_slime"),
    HEART("heart"),
    BARRIER("barrier"),
    ITEM_CRACK("item", "iconcrack", "itemcrack"),
    BLOCK_CRACK("block", "blockcrack"),
    BLOCK_DUST("dust", "blockdust"),
    WATER_DROP("rain", "droplet"),
    MOB_APPEARANCE("elder_guardian", "mobappearance"),
    DRAGON_BREATH("dragon_breath", "dragonbreath"),
    END_ROD("end_rod", "endRod"),
    DAMAGE_INDICATOR("damage_indicator", "damageIndicator"),
    SWEEP_ATTACK("sweep_attack", "sweepAttack"),
    FALLING_DUST("falling_dust", "fallingDust"),
    TOTEM("totem_of_undying"),
    SPIT("spit"),
    SQUID_INK("squid_ink", "squidink"),
    BUBBLE_POP("bubble_pop", "bubblepop"),
    CURRENT_DOWN("current_down", "currentdown"),
    BUBBLE_COLUMN_UP("bubble_column_up", "bubblecolumn", "bubble_column"),
    NAUTILUS("nautilus"),
    DOLPHIN("dolphin");
    
    private static final Map<String, AbstractParticle> PARTICLE_ALIASES;
    private final String[] aliases;

    public static AbstractParticle get(String key) {
        AbstractParticle particle = PARTICLE_ALIASES.getOrDefault(key.toUpperCase(), null);
        if (particle == null) {
            Main.logger.warning("Particle '" + key + "' is not supported default to cloud.");
            return CLOUD;
        }
        return particle;
    }

    private AbstractParticle(String ... aliases) {
        this.aliases = aliases;
    }

    public Particle toBukkitParticle() {
        return Particle.valueOf((String)this.toString());
    }

    public boolean requiresData() {
        return !this.toBukkitParticle().getDataType().equals(Void.class);
    }

    public boolean validateData(Object obj) {
        Particle particle = this.toBukkitParticle();
        if (particle.getDataType().equals(Void.class)) {
            return false;
        }
        if (particle.getDataType().equals(ItemStack.class)) {
            return obj instanceof ItemStack;
        }
        if (particle.getDataType() == BlockData.class) {
            return obj instanceof BlockData;
        }
        if (particle.getDataType() == MaterialData.class) {
            return obj instanceof MaterialData;
        }
        if (particle.getDataType() == Particle.DustOptions.class) {
            return obj instanceof Particle.DustOptions;
        }
        return true;
    }

    public Object parseDataOptions(MythicLineConfig config) {
        Particle particle = this.toBukkitParticle();
        if (particle.getDataType().equals(ItemStack.class)) {
            String strMaterial = config.getString(new String[]{"material", "m"}, "STONE", new String[0]);
            try {
                return new ItemStack(Material.matchMaterial((String)strMaterial.toUpperCase()));
            }
            catch (Exception ex) {
                return new ItemStack(Material.STONE);
            }
        }
        if (particle.getDataType().equals(BlockData.class)) {
            String strMaterial = config.getString(new String[]{"material", "m"}, "STONE", new String[0]);
            try {
                Material material = Material.matchMaterial((String)strMaterial.toUpperCase());
                return Bukkit.getServer().createBlockData(material);
            }
            catch (Exception ex) {
                return Bukkit.getServer().createBlockData(Material.STONE);
            }
        }
        if (particle.getDataType().equals(MaterialData.class)) {
            String strMaterial = config.getString(new String[]{"material", "m"}, "STONE", new String[0]);
            try {
                Material.matchMaterial((String)strMaterial.toUpperCase()).getData();
            }
            catch (Exception ex) {
                return Material.STONE.getData();
            }
        }
        if (particle.getDataType().equals(Particle.DustOptions.class)) {
            String strColor = config.getString(new String[]{"color", "c"}, "#FF0000", new String[0]);
            Color color = Color.decode(strColor);
            float size = config.getFloat(new String[]{"size"}, 1.0f);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            org.bukkit.Color c = org.bukkit.Color.fromRGB((int)r, (int)g, (int)b);
            return new Particle.DustOptions(c, size);
        }
        return null;
    }

    public void send(Player player, Location loc, float speed, int amount, float offsetX, float offsetY, float offsetZ) {
        Particle particle = this.toBukkitParticle();
        player.spawnParticle(particle, loc, amount, (double)offsetX, (double)offsetY, (double)offsetZ, (double)speed);
    }

    public void send(Location loc, float speed, int amount, float offsetX, float offsetY, float offsetZ) {
        Particle particle = this.toBukkitParticle();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(particle, loc, amount, (double)offsetX, (double)offsetY, (double)offsetZ, (double)speed);
        }
    }

    public void send(Location loc, float speed, int amount, float offsetX, float offsetY, float offsetZ, Object data) {
        if (!this.validateData(data)) {
            Main.logger.warning("Could not send particle: invalid particle data supplied.");
            return;
        }
        Particle particle = this.toBukkitParticle();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(particle, loc, amount, (double)offsetX, (double)offsetY, (double)offsetZ, (double)speed, data);
        }
    }

    public void sendDirectional(Location loc, float speed, int amount, float offsetX, float offsetY, float offsetZ, Vector direction) {
        Particle particle = this.toBukkitParticle();
        for (int i = 0; i < amount; ++i) {
            Location ln = loc.clone().add((double)(0.0f - offsetX) + Main.random.nextDouble() * (double)offsetX * 2.0, (double)(offsetY - offsetY) + Main.random.nextDouble() * (double)offsetY * 2.0, (double)(0.0f - offsetZ) + Main.random.nextDouble() * (double)offsetZ * 2.0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spawnParticle(particle, ln, 0, (double)((float)direction.getX()), (double)((float)direction.getY()), (double)((float)direction.getZ()), (double)speed);
            }
        }
    }

    public void sendLegacyColored(Location loc, float speed, int amount, float offsetX, float offsetY, float offsetZ, Color color) {
        Particle particle = this.toBukkitParticle();
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        if (r < Float.MIN_NORMAL) {
            r = Float.MIN_NORMAL;
        }
        for (int i = 0; i < amount; ++i) {
            Location ln = loc.clone().add((double)(0.0f - offsetX) + Main.random.nextDouble() * (double)offsetX * 2.0, (double)(offsetY - offsetY) + Main.random.nextDouble() * (double)offsetY * 2.0, (double)(0.0f - offsetZ) + Main.random.nextDouble() * (double)offsetZ * 2.0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spawnParticle(particle, ln, 0, (double)r, (double)g, (double)b, speed > 0.0f ? (double)speed : 1.0);
            }
        }
    }

    public String[] getAliases() {
        return this.aliases;
    }

    static {
        PARTICLE_ALIASES = new HashMap<String, AbstractParticle>();
        for (AbstractParticle particle : AbstractParticle.values()) {
            PARTICLE_ALIASES.put(particle.toString(), particle);
            for (String alias : particle.getAliases()) {
                PARTICLE_ALIASES.put(alias.toUpperCase(), particle);
            }
        }
    }
}

