package com.gmail.berndivader.mythicmobsext.backbags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;

public 
class
BackBag
implements
Listener
{
	Entity owner;
	Player viewer;
	Inventory inventory;
	private int size;
	String name;
	boolean only_view;
	
	public BackBag(Entity onwer) {
		this(onwer,9);
	}

	public BackBag(Entity owner,int size) {
		this(owner,size,null);
	}
	
	public BackBag(Entity owner,int size,ItemStack[]default_content) {
		this(owner,size,default_content,null);
	}
	
	public BackBag(Entity owner,int size,ItemStack[]default_content,String name) {
		size=size%9>0?size+(9-size%9):size;
		this.owner=owner;
		if((inventory=BackBagHelper.getInventory(owner.getUniqueId()))==null) {
			inventory=Bukkit.createInventory(null,9+size,name==null?BackBagHelper.str_name:name);
			BackBagHelper.addInventory(owner.getUniqueId(),inventory);
		}
		this.size=inventory.getSize();
		if(default_content!=null&&default_content.length<=this.size) inventory.setContents(default_content);
	}
	
	
	public void viewBackBag(Player player) {
		viewBackBag(player,false);
	}
	
	public void viewBackBag(Player player,boolean bool) {
		this.only_view=bool;
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
		this.viewer=player;
		player.openInventory(inventory);
	}
	
	public boolean isPresent() {
		return this.inventory!=null;
	}
	
	public int getSize() {
		return this.inventory.getSize();
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	public void setInventory(Inventory new_inv) {
		this.inventory=new_inv;
		this.size=new_inv.getSize();
		BackBagHelper.bags.replace(owner.getUniqueId(),this.inventory);
	}
	
	@EventHandler
	public void ownerQuit(PlayerQuitEvent e) {
		if(e.getPlayer()==this.owner) viewer.closeInventory();
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e) {
		if(e.isCancelled()&&e.getInventory().equals(inventory)) HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void interact(InventoryClickEvent e) {
		if(only_view&&inventory!=null) {
			Inventory clicked_inventory=e.getClickedInventory();
			if(clicked_inventory!=null&&clicked_inventory.hashCode()!=inventory.hashCode()&&e.getAction()==InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				e.setCancelled(true);
			}
			if(clicked_inventory!=null&&clicked_inventory.hashCode()==inventory.hashCode()) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if(e.getInventory().equals(inventory)) HandlerList.unregisterAll(this);
	}
	
}