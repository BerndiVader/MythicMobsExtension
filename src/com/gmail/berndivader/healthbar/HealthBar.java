package com.gmail.berndivader.healthbar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class HealthBar implements Listener {
	private Scoreboard s;

	public HealthBar() {
		this.s = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        if (this.s.getObjective("health") != null) {
            this.s.getObjective("health").unregister();
        }
        
        Objective o = this.s.registerNewObjective("health", "health");
        o.setDisplayName((Object)ChatColor.RED + "\u2764");
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	public Scoreboard getScoreboard() {
		return this.s;
	}
}
