package com.gmail.berndivader.mythicmobsext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class ThiefHandler {
	protected Plugin plugin = Main.getPlugin();
	public BukkitTask taskid;
	private final Set<Thief> thiefs = new HashSet<>();
	protected MobManager mobmanager;

	public ThiefHandler() {
		this.mobmanager = MythicMobs.inst().getMobManager();
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
