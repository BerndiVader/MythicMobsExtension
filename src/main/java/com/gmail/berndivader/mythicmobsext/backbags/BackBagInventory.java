package com.gmail.berndivader.mythicmobsext.backbags;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;

public class BackBagInventory {
	private String content_base64;
	private String name;
	private int size;

	private transient Inventory inventory;
	private transient boolean temporary;

	public BackBagInventory(UUID owner, String name, int size, ItemStack[] default_content, boolean temporary,
			boolean override) {
		this.name = name;
		size = size % 9 > 0 ? size + (9 - size % 9) : size;
		this.size = size;
		this.temporary = temporary;
		if (override || (inventory = BackBagHelper.getInventory(owner, name)) == null) {
			inventory = Bukkit.createInventory(null, size, name);
			BackBagHelper.addInventory(owner, this);
		}
		if (default_content != null && default_content.length <= this.size)
			inventory.setContents(default_content);
	}

	public BackBagInventory(String name, int size, Inventory inventory) {
		this(name, size, inventory, false);
	}

	public BackBagInventory(String name, int size, Inventory inventory, boolean temporary) {
		this.name = name;
		this.size = size;

		this.temporary = temporary;
		this.inventory = inventory;
	}

	public ItemStack[] getContentBase64() {
		try {
			return BukkitSerialization.itemStackArrayFromBase64(this.content_base64);
		} catch (IOException e) {
			return new ItemStack[0];
		}
	}

	public void convert() {
		if (this.inventory.getContents() != null) {
			this.content_base64 = BukkitSerialization.itemStackArrayToBase64(this.inventory.getContents());
		} else {
			this.content_base64 = BukkitSerialization.itemStackArrayToBase64(new ItemStack[0]);
		}
		this.size = this.inventory.getSize();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String new_name) {
		this.name = new_name;
		Inventory new_inventory = Bukkit.createInventory(null, this.size, this.name);
		new_inventory.setContents(this.getInventory().getContents().clone());
		this.inventory = new_inventory;
	}

	public int getSize() {
		return this.size;
	}

	public void setInventory(String name, Inventory inventory) {
		this.name = name;
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public boolean isTemporary() {
		return this.temporary;
	}
}
