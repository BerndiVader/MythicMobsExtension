package com.gmail.berndivader.mmcustomskills26;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class Thief {

	private long timestamp;
	private UUID uuid;
	private ItemStack item;

	public Thief(UUID u, ItemStack i) {
		this.uuid = u;
		this.item = new ItemStack(i);
		this.timestamp = (System.currentTimeMillis() / 1000L) + 30;
	}

	public UUID getUuid() {
		return uuid;
	}

	public ItemStack getItem() {
		return item;
	}

	public long getTimeStamp() {
		return timestamp;
	}
}
