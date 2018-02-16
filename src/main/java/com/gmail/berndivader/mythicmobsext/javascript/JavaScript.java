package com.gmail.berndivader.mythicmobsext.javascript;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;

public class JavaScript {
	public JavaScript() {
		new NashornMythicMobsEvents();
		new BukkitRunnable() {
			@Override
			public void run() {
				new Nashorn();
			}
		}.runTask(Main.getPlugin());
	}
	
}
