package com.gmail.berndivader.nanpatch;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.NMS.NMSUtils;

public class NaNpatch implements Listener {
	protected NMSUtils nmsutils;
	
	public NaNpatch() {
		this.nmsutils = Main.NMSUtils();
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOnline()) {
			if (Float.isNaN(nmsutils.getAbsAmount(p))) {
				if (!nmsutils.setAbsAmount(p, 0.0f)) {
					Bukkit.getLogger().warning("Unable to patch NaN for " + p.getName()+"!");
				};
			}
		}
	}
	
}
