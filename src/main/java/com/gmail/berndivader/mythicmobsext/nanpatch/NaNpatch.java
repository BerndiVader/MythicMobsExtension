package main.java.com.gmail.berndivader.mythicmobsext.nanpatch;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import main.java.com.gmail.berndivader.mythicmobsext.Main;

public class NaNpatch implements Listener {

	public NaNpatch() {
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOnline()) {
			if (Float.isNaN(NMSUtils.getAbsAmount(p))) {
				if (!NMSUtils.setAbsAmount(p, 0.0f)) {
					Main.logger.warning("Unable to patch NaN for " + p.getName() + "!");
				}
			}
		}
	}

}
