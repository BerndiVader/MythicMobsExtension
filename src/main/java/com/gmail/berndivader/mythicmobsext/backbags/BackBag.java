package com.gmail.berndivader.mythicmobsext.backbags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.mechanics.CreateItem;
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
	BackBagInventory inventory;
	private int size;
	String name;
	boolean only_view;
	List<Integer>excludes_slots;
	
	static int backbag_counter=0;
	
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
		this(owner,size,default_content,name,temporary,override,new ArrayList<>());
	}

	public BackBag(Entity owner,int size,ItemStack[]default_content,String name,boolean temporary,boolean override,List<Integer>excluded_slots) {
		backbag_counter++;
		if(name==null) name=BackBagHelper.str_name;
		size=size%9>0?size+(9-size%9):size;
		this.owner=owner;
		if((inventory=BackBagHelper.getBagInventory(owner.getUniqueId(),name))==null||override) {
			inventory=new BackBagInventory(name,size,Bukkit.createInventory(null,size,name),temporary);
			BackBagHelper.addInventory(owner.getUniqueId(),inventory);
		}
		this.size=inventory.getInventory().getSize();
		this.name=inventory.getName();
		if(default_content!=null&&default_content.length<=this.size) inventory.getInventory().setContents(default_content);
		this.excludes_slots=excluded_slots;
	}
	
	
	public void viewBackBag(Player player) {
		viewBackBag(player,this.only_view,this.excludes_slots);
	}
	
	public void viewBackBag(Player player,boolean bool) {
		viewBackBag(player,bool,this.excludes_slots);
	}
	
	public void viewBackBag(Player player,boolean bool,List<Integer> excludes_slots) {
		this.only_view=bool;
		this.excludes_slots=excludes_slots;
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
		this.viewer=player;
		player.openInventory(inventory.getInventory());
	}
	
	
	public boolean isPresent() {
		return this.inventory!=null;
	}
	
	public int getSize() {
		return this.inventory.getSize();
	}
	
	public Inventory getInventory() {
		return this.inventory.getInventory();
	}
	
	public void setInventory(String name,Inventory new_inv) {
		this.inventory.setInventory(name,new_inv);
		this.size=new_inv.getSize();
		BackBagHelper.replace(owner.getUniqueId(),new BackBagInventory(this.name,this.size,this.inventory.getInventory()));
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
	public void ownerDeath(EntityDeathEvent e) {
		Entity entity=e.getEntity();
		if(entity.getType()!=EntityType.PLAYER&&entity==this.owner) viewer.closeInventory();
	}
	
	@EventHandler
	public void ownerQuit(PlayerQuitEvent e) {
		if(e.getPlayer()==this.owner) viewer.closeInventory();
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e) {
		if(e.isCancelled()&&e.getInventory().equals(inventory.getInventory())) HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void interact(InventoryClickEvent e) {
		if(e.getWhoClicked()==viewer) {
			if(only_view&&!excludes_slots.contains(e.getRawSlot())) e.setCancelled(true);
			if(e.getClickedInventory()!=null&&e.getView().getTitle().equals(this.name)) {
				owner.setMetadata(Utils.meta_LASTCLICKEDSLOT,new FixedMetadataValue(Main.getPlugin(),e.getRawSlot()));
				owner.setMetadata(Utils.meta_LASTCLICKEDBAG,new FixedMetadataValue(Main.getPlugin(),this.name));
				e.getWhoClicked().setMetadata(Utils.meta_LASTCLICKEDSLOT,new FixedMetadataValue(Main.getPlugin(),e.getRawSlot()));
				if(Utils.mobmanager.isActiveMob(owner.getUniqueId())) {
					ActiveMob am=Utils.mobmanager.getMythicMobInstance(owner);
					am.signalMob(BukkitAdapter.adapt(e.getWhoClicked()),Utils.signal_BACKBAGCLICK);
				}
			}
			ItemStack clicked_stack=e.getCurrentItem();
			if(clicked_stack!=null) {
				if(NMSUtils.hasMeta(clicked_stack,Utils.meta_CLICKEDSKILL)) {
					this.executeSkill(NMSUtils.getMetaString(clicked_stack,Utils.meta_CLICKEDSKILL),owner,viewer);
				}
				if (NMSUtils.hasMeta(clicked_stack,CreateItem.str_viewonly)) {
					e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer()==viewer&&e.getInventory().equals(inventory.getInventory())) {
			HandlerList.unregisterAll(this);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		backbag_counter--;
		super.finalize();
	}
	
}