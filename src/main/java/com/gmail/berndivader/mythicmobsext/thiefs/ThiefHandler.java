package com.gmail.berndivader.mythicmobsext.thiefs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public class ThiefHandler {
	public BukkitTask taskid;
	private final HashSet<Thief>thiefs=new HashSet<>();

	public ThiefHandler() {
		this.taskid = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Iterator<Thief> ti = ThiefHandler.this.thiefs.iterator();
				while (ti.hasNext()) {
					Thief thief = ti.next();
					if (!Utils.mobmanager.isActiveMob(thief.getUuid()))
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
	
	public void cancelTask() {
		Bukkit.getServer().getScheduler().cancelTask(taskid.getTaskId());
	}
}
