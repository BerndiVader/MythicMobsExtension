package com.gmail.berndivader.nanpatch;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.NMS.NMSUtils;

public class NaNpatch implements Listener {
	protected Plugin plugin;

	public NaNpatch(Plugin plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOnline()) {
			if (Float.isNaN(NMSUtils.getAbsAmount(p))) {
				if (!NMSUtils.setAbsAmount(p, 0.0f)) {
					this.plugin.getLogger().warning("Unable to patch NaN for " + p.getName() + "!");
				}
			}
		}
	}

}
