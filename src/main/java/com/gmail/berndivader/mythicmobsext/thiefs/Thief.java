package com.gmail.berndivader.mythicmobsext.thiefs;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class Thief {

	private long timestamp;
	private UUID uuid,victim;
	private ItemStack item;

	public Thief(UUID u,UUID victim,ItemStack i) {
		this.uuid = u;
		this.item = new ItemStack(i);
		this.victim=victim;
		this.timestamp = (System.currentTimeMillis() / 1000L) + 30;
	}

	public UUID getUuid() {
		return uuid;
	}

	public ItemStack getItem() {
		return item;
	}
	
	public UUID getVictimUUID() {
		return victim;
	}

	public long getTimeStamp() {
		return timestamp;
	}
}
