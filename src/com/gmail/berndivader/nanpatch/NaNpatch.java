package com.gmail.berndivader.nanpatch;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.NMS.NMSUtils;

public class NaNpatch implements Listener {
	protected NMSUtils nmsutils = Main.getPlugin().getNMSUtils();
	protected Plugin plugin = Main.getPlugin();

	public NaNpatch() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOnline()) {
			if (Float.isNaN(nmsutils.getAbsAmount(p))) {
				if (!nmsutils.setAbsAmount(p, 0.0f)) {
					plugin.getLogger().warning("Unable to patch NaN for " + p.getName() + "!");
				}
				;
			}
		}
	}

}
