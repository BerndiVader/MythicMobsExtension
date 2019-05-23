package com.gmail.berndivader.mythicmobsext.utils;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.ConfigManager;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.VanillaManager;
import io.lumine.xikage.mythicmobs.mobs.WorldScaling;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntityType;
import io.lumine.xikage.mythicmobs.spawning.random.RandomSpawnPoint;

public
class
Pre44MobSpawnEvent 
implements 
Listener {
	
	static MythicMobs mythicmobs = Utils.mythicmobs;
	
	public Pre44MobSpawnEvent() {
		CreatureSpawnEvent.getHandlerList();
		RegisteredListener[]listeners=CreatureSpawnEvent.getHandlerList().getRegisteredListeners();
		for(int i1=0;i1<listeners.length;i1++) {
			Listener listener=listeners[i1].getListener();
			if(listener.getClass().getName().equals("io.lumine.xikage.mythicmobs.adapters.bukkit.listeners.MobListeners")) {
				CreatureSpawnEvent.getHandlerList().unregister(listener);
				Main.logger.warning("Unregistered MythicMobs 4.4.x MobSpawnEvent!");
				Main.pluginmanager.registerEvents(this,Main.getPlugin());
				Main.logger.warning("Registered MythicMobs pre 4.4.x MobSpawnEvent!");
				break;
			}
		}
	}
	
    @EventHandler(priority=EventPriority.HIGH)
    public void MobSpawnEvent(CreatureSpawnEvent e) {
        if (e.getEntity() == null) {
            return;
        }
        if (ConfigManager.debugMode) {
            return;
        }
        if (ConfigManager.debugSpawners) {
            return;
        }
        if (mythicmobs.getCompatibility().getWorldGuard().isPresent() && !mythicmobs.getCompatibility().getWorldGuard().get().LocationAllowsMobSpawning(e.getLocation())) {
            return;
        }
        RandomSpawnPoint rsp = new RandomSpawnPoint(BukkitAdapter.adapt((Entity)e.getEntity()), BukkitAdapter.adapt(e.getLocation()), e.getSpawnReason());
        final AbstractEntity spawn = BukkitAdapter.adapt((Entity)e.getEntity());
        Entity ee = BukkitAdapter.adapt(mythicmobs.getRandomSpawningManager().handleSpawnEvent(rsp));
        if (ee != null) {
            e.setCancelled(true);
        } else if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            MythicEntityType met = VanillaManager.getMythicEntityType((Entity)e.getEntity());
            Optional<MythicMob> maybeType = MythicMobs.inst().getMobManager().getVanillaType(met);
            if (maybeType.isPresent()) {
                MythicMob mm = maybeType.get();
                int level = 1 + WorldScaling.getLevelBonus(spawn.getLocation());
                ActiveMob am = Utils.mobmanager.registerActiveMob(spawn, mm, level);
                mm.getMythicEntity().applyOptions((Entity)e.getEntity());
                mm.applyMobOptions(am, level);
                mm.applyMobVolatileOptions(am);
                mm.applySpawnModifiers(am);
            }
        }
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            LivingEntity l;
            if (ConfigManager.UseCompatibilityMode) {
                l = e.getEntity();
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)mythicmobs, new Runnable(){
                    @Override
                    public void run() {
                        if (!Utils.mobmanager.isActiveMob(spawn.getUniqueId())) {
                            MythicMobs.debug(3, "Compatibility mode enabled and found custom mob spawn! Checking for MythicMob '" + l.getCustomName() + "'...");
                            MythicMob mm = Utils.mobmanager.getMythicMobByDisplayCompat(spawn);
                            if (mm != null) {
                                Utils.mobmanager.SetupMythicMobCompat(l, mm);
                            } else {
                            	Utils.mobmanager.getVoidList().add(l.getUniqueId());
                            }
                        }
                    }
                }, 10);
            } else {
                l = e.getEntity();
                TaskManager.get().runLater(new Runnable(){
                    @Override
                    public void run() {
                        if (!Utils.mobmanager.isActiveMob(l.getUniqueId())) {
                        	Utils.mobmanager.getVoidList().add(l.getUniqueId());
                        }
                    }
                }, 1);
            }
        }
    }
	

}
