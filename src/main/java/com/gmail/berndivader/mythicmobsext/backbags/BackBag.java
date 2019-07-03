package com.gmail.berndivader.mythicmobsext.backbags;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

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

	public BackBag(Entity onwer,String name) {
		this(onwer,9,null,name,false);
	}

	public BackBag(Entity owner,int size) {
		this(owner,size,null);
	}
	
	public BackBag(Entity owner,int size,ItemStack[]default_content) {
		this(owner,size,default_content,null,false);
	}
	
	public BackBag(Entity owner,int size,ItemStack[]default_content,String name,boolean temporary) {
		this(owner,size,default_content,name,temporary,false);
	}

	public BackBag(Entity owner,int size,ItemStack[]default_content,String name,boolean temporary,boolean override) {
		if(name==null) name=BackBagHelper.str_name;
		size=size%9>0?size+(9-size%9):size;
		this.owner=owner;
		if((inventory=BackBagHelper.getInventory(owner.getUniqueId(),name))==null||override) {
			inventory=Bukkit.createInventory(null,size,name);
			BackBagHelper.addInventory(owner.getUniqueId(),new BackBagInventory(name,size,inventory,temporary));
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
		BackBagHelper.replace(owner.getUniqueId(),this.inventory);
	}
	
	void executeSkill(String skill_name,Entity owner,Player viewer) {
		Optional<Skill>maybe_skill=Utils.mythicmobs.getSkillManager().getSkill(skill_name);
		if(maybe_skill.isPresent()) {
			Skill skill=maybe_skill.get();
			AbstractEntity abstract_target=BukkitAdapter.adapt(viewer);
			SkillMetadata data=new SkillMetadata(SkillTrigger.API,new GenericCaster(BukkitAdapter.adapt(owner)),abstract_target);
			data.setEntityTarget(abstract_target);
			if(skill.isUsable(data)) skill.execute(data);
		}
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
		if(e.getWhoClicked()==viewer) {
			if(e.getClickedInventory().getName().equals(this.inventory.getName())) {
				owner.setMetadata(Utils.meta_LASTCLICKEDSLOT,new FixedMetadataValue(Main.getPlugin(),e.getSlot()));
				owner.setMetadata(Utils.meta_LASTCLICKEDBAG,new FixedMetadataValue(Main.getPlugin(),this.inventory.getName()));
				e.getWhoClicked().setMetadata(Utils.meta_LASTCLICKEDSLOT,new FixedMetadataValue(Main.getPlugin(),e.getSlot()));
				if(Utils.mobmanager.isActiveMob(owner.getUniqueId())) {
					ActiveMob am=Utils.mobmanager.getMythicMobInstance(owner);
					am.signalMob(BukkitAdapter.adapt(e.getWhoClicked()),Utils.signal_BACKBAGCLICK);
				}
			}
			if(only_view) e.setCancelled(true);
			ItemStack clicked_stack=e.getCurrentItem();
			if(NMSUtils.hasMeta(clicked_stack,Utils.meta_CLICKEDSKILL)) {
				this.executeSkill(NMSUtils.getMetaString(clicked_stack,Utils.meta_CLICKEDSKILL),owner,viewer);
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer()==viewer&&e.getInventory().equals(inventory)) HandlerList.unregisterAll(this);
	}
	
}