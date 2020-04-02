package com.gmail.berndivader.mythicmobsext.backbags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.gmail.berndivader.mythicmobsext.Main;

public class InventoryViewer implements Listener {
	final Player victim;
	final Player caster;
	final Inventory inventory;
	final boolean view_only;
	final InventoryType type;

	public InventoryViewer(Player victim, Player caster) {
		this(victim, caster, true, InventoryType.PLAYER);
	}

	public InventoryViewer(Player victim, Player caster, boolean view_only, InventoryType type) {
		this.victim = victim;
		this.caster = caster;
		this.view_only = view_only;
		this.type = type;
		switch (type) {
		case PLAYER:
			this.inventory = victim.getInventory();
			caster.openInventory(inventory);
			break;
		case ENDER_CHEST:
			this.inventory = caster.getEnderChest();
			caster.openInventory(inventory);
			break;
		case WORKBENCH:
			caster.openWorkbench(caster.getLocation(), true);
			this.inventory = caster.getOpenInventory().getBottomInventory();
			break;
		case ENCHANTING:
			caster.openEnchanting(caster.getLocation(), true);
			this.inventory = caster.getOpenInventory().getBottomInventory();
			break;
		default:
			this.inventory = Bukkit.createInventory(null, type);
			caster.openInventory(this.inventory);
			break;
		}
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void victimQuit(PlayerQuitEvent e) {
		if (e.getPlayer() == victim)
			caster.closeInventory();
	}

	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e) {
		if (e.isCancelled() && e.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void interact(InventoryClickEvent e) {
		if (inventory != null && e.getWhoClicked() == caster && this.view_only) {
			if (e.getInventory().equals(inventory))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if (e.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

	@Override
	protected void finalize() throws Throwable {
		HandlerList.unregisterAll(this);
		super.finalize();
	}

}