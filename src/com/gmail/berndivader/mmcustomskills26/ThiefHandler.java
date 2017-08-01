package com.gmail.berndivader.mmcustomskills26;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class ThiefHandler implements Listener {
	protected Plugin plugin = Main.getPlugin();
	public BukkitTask taskid;
	private final Set<Thief> thiefs = new HashSet<>();
	protected MobManager mobmanager;

	public ThiefHandler() {
		this.mobmanager = MythicMobs.inst().getMobManager();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.taskid = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				Iterator<Thief> ti = ThiefHandler.this.thiefs.iterator();
				while (ti.hasNext()) {
					Thief thief = ti.next();
					if (!ThiefHandler.this.mobmanager.isActiveMob(thief.getUuid()))
						ti.remove();
				}
			}
		}, 1200L, 1200L);
	}

	@EventHandler
	public void onThiefDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof ArmorStand) {
			return;
		}
		if (this.mobmanager.isActiveMob(e.getDamager().getUniqueId())) {
			if (e.getEntityType() == EntityType.PLAYER) {
				ActiveMob am = this.mobmanager.getMythicMobInstance(e.getDamager());
				if (am.getStance().equalsIgnoreCase("gostealing")) {
					e.setCancelled(true);
				}
			}
		}
	}

	public Set<Thief> getThiefs() {
		return thiefs;
	}

	public boolean addThief(UUID uuid, ItemStack item) {
		thiefs.add(new Thief(uuid, item));
		return true;
	}

	public Thief getThief(UUID uuid) {
		for (Thief thief : thiefs) {
			if (thief.getUuid().equals(uuid)) {
				return thief;
			}
		}
		return null;
	}

	public void removeThief(Thief thief) {
		thiefs.remove(thief);
	}

	public int Size() {
		return thiefs.size();
	}
}
