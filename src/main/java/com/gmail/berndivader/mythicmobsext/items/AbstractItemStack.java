package com.gmail.berndivader.mythicmobsext.items;

import org.bukkit.inventory.ItemStack;

public class AbstractItemStack extends ItemStack {
	
	public ItemStack itemStack;
	public int slot;
	
	public AbstractItemStack(ItemStack itemStack) {
		this.itemStack=itemStack;
	}

}
