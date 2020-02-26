package com.gmail.berndivader.mythicmobsext.backbags;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.CreateBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.ExpandBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.MoveToBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.OpenBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.RemoveBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.RenameBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.RestoreFromBackBag;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent;
import io.lumine.xikage.mythicmobs.items.ItemManager;

public 
class 
BackBagHelper
implements
Listener
{
	final static ItemManager itemmananger=Utils.mythicmobs.getItemManager();
	final static HashMap<UUID,List<BackBagInventory>>bags;
	final public static String str_name;
	final static String url;

	static {
		url=Main.getPlugin().getDataFolder().getPath()+"/backbags";
		new File(url).mkdirs();
		bags=new HashMap<>();
		str_name="BackBag";
	}
	
	public BackBagHelper() {
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
	}
	
	@EventHandler
	public void loadMechanicsEvent(MythicMechanicLoadEvent e) {
		switch(e.getMechanicName().toLowerCase()) {
		case "openbackbag":
			e.register(new OpenBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "createbackbag":
			e.register(new CreateBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "removebackbag":
			e.register(new RemoveBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "expandbackbag":
			e.register(new ExpandBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "movetobackbag":
		case "savetobackbag":
			e.register(new MoveToBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "takefrombackbag":
		case "loadfrombackbag":
			e.register(new RestoreFromBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		case "renamebackbag":
			e.register(new RenameBackBag(e.getContainer().getConfigLine(),e.getConfig()));
			break;
		}
	}
	
	public static ItemStack[] createDefaultItemStack(String line) {
		if(line==null) return null;
		String[]line_parse=line.split(",");
		List<ItemStack>list=new ArrayList<ItemStack>();
		for(int i1=0;i1<line_parse.length;i1++) {
			String[]item_parse=line_parse[i1].split(":");
			String item_name=item_parse[0];
			int item_amount=item_parse.length>1?Integer.parseInt(item_parse[1]):1;
			ItemStack item=itemmananger.getItemStack(item_name);
			if(item!=null) {
				item.setAmount(item_amount);
				list.add(item.clone());
			}
		}
		return list.toArray(new ItemStack[list.size()]);
	}
	
	public static void remove(UUID uuid,String name) {
		if(bags.containsKey(uuid)) {
			Iterator<BackBagInventory>inventory_iter=bags.get(uuid).iterator();
			while(inventory_iter.hasNext()) {
				BackBagInventory stored_inventory=inventory_iter.next();
				if(stored_inventory.getName().equals(name)) inventory_iter.remove();
			}
		}
	}
	
	public static void removeAll(UUID uuid) {
		if(bags.containsKey(uuid)) bags.remove(uuid);
	}
	
	public static void addInventory(UUID uuid,BackBagInventory bag_inventory) {
		if(bags.containsKey(uuid)) {
			Iterator<BackBagInventory>inventory_iter=bags.get(uuid).iterator();
			while(inventory_iter.hasNext()) {
				BackBagInventory stored_inventory=inventory_iter.next();
				if(stored_inventory.getName().equals(bag_inventory.getName())) inventory_iter.remove();
			}
		} else {
			bags.put(uuid,new ArrayList<>());
		}
		bags.get(uuid).add(bag_inventory);
	}
	
	public static boolean hasBackBag(UUID uuid) {
		return bags.containsKey(uuid);
	}
	
	public static BackBagInventory getBagInventory(UUID uuid,String name) {
		if(bags.containsKey(uuid)) {
			Iterator<BackBagInventory>inventory_iter=bags.get(uuid).iterator();
			while(inventory_iter.hasNext()) {
				BackBagInventory stored_inventory=inventory_iter.next();
				if(stored_inventory.getName().equals(name)) {
					return stored_inventory;
				}
			}
		}
		return null;
	}
	
	public static Inventory getInventory(UUID uuid,String name) {
		if(bags.containsKey(uuid)) {
			Iterator<BackBagInventory>inventory_iter=bags.get(uuid).iterator();
			while(inventory_iter.hasNext()) {
				BackBagInventory stored_inventory=inventory_iter.next();
				if(stored_inventory.getName().equals(name)) {
					return stored_inventory.getInventory();
				}
			}
		}
		return null;
	}
	
	public static void replace(UUID uuid,BackBagInventory inventory) {
		addInventory(uuid,inventory);
	}
	
	public static void removeInventory(UUID uuid,String bag_name) {
		if(bags.containsKey(uuid)) {
			Iterator<BackBagInventory>inventory_iter=bags.get(uuid).iterator();
			while(inventory_iter.hasNext()) {
				BackBagInventory stored_inventory=inventory_iter.next();
				if(stored_inventory.getName().equals(bag_name)) inventory_iter.remove();
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				UUID uuid=e.getPlayer().getUniqueId();
				File file=new File(url+"/"+uuid.toString());
				if(file.exists()) loadBags(file);
			}
		}.runTaskAsynchronously(Main.getPlugin());
	}
	
	public static void loadBags(File file) {
		Gson gson=new Gson();
		BackBagInventory[]bag_inventories=null;
		try (FileReader reader=new FileReader(url+"/"+file.getName())) {
			bag_inventories=gson.fromJson(reader,BackBagInventory[].class);
		} catch (Exception ex) {
			Main.logger.info("Found illegal backbag save: "+file.getName()+". Try to load old format...");
			bag_inventories=loadOldSaveFile(file);
		}
		if(bag_inventories!=null) {
			UUID uuid=UUID.fromString(file.getName());
			for(int i2=0;i2<bag_inventories.length;i2++) {
				BackBagInventory bag_inventory=bag_inventories[i2];
				Inventory inventory=Bukkit.createInventory(null,bag_inventory.getSize(),bag_inventory.getName());
				inventory.setContents(bag_inventory.getContentBase64());
				bag_inventory.setInventory(bag_inventory.getName(),inventory);
				bag_inventory.setTemporary(false);
				BackBagHelper.addInventory(uuid,bag_inventory);
			}
		}
	}
	
	public static BackBagInventory[] loadOldSaveFile(File file) {
		BackBagInventory[]bags=new BackBagInventory[0];
		try(Scanner scanner=new Scanner(file)) {
			ItemStack[] contents=new ItemStack[0];
			String content=scanner.useDelimiter("\\A").next();
			if(!content.isEmpty()) contents=BukkitSerialization.itemStackArrayFromBase64(content);
		    Inventory inventory=Bukkit.createInventory(null,contents.length,str_name);
		    inventory.setContents(contents);
		    bags=new BackBagInventory[] {new BackBagInventory(str_name,inventory.getSize(),inventory)};
		} catch (IOException e) {
			Main.logger.info("Failed to load old backbag format return empty.");
		}
		return bags;
	}
	
	public static void loadBags() {
		File dir=new File(url);
		File[]files=dir.listFiles();
		int size=files.length;
		for(int i1=0;i1<size;i1++) {
			File file=files[i1];
			if(file.exists()) loadBags(file);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(!Main.server_running) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				UUID uuid=e.getPlayer().getUniqueId();
				if(hasBackBag(uuid)) {
					List<BackBagInventory>backbags=bags.get(uuid);
					if(backbags!=null&&!backbags.isEmpty()) saveBags(uuid,backbags);
				}
			}
		}.runTaskAsynchronously(Main.getPlugin());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntityType()!=EntityType.PLAYER) {
			final UUID uuid=e.getEntity().getUniqueId();
			if(BackBagHelper.hasBackBag(uuid)) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Iterator<UUID>bag_iter=bags.keySet().iterator();
						while(bag_iter.hasNext()) {
							if(uuid==bag_iter.next()) {
								bag_iter.remove();
							}
						}
					}
				}.runTaskAsynchronously(Main.getPlugin());
			}
		}
	}
	
	@EventHandler
	public void onEntityDespawn(MythicMobDespawnEvent e) {
		if(e.getEntity().getType()!=EntityType.PLAYER) {
			final UUID uuid=e.getEntity().getUniqueId();
			if(BackBagHelper.hasBackBag(uuid)) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Iterator<UUID>bag_iter=bags.keySet().iterator();
						while(bag_iter.hasNext()) {
							if(uuid==bag_iter.next()) {
								bag_iter.remove();
							}
						}
					}
				}.runTaskAsynchronously(Main.getPlugin());
			}
		}
	}
	
	public static void saveBags(UUID uuid,List<BackBagInventory>backbags) {
		Iterator<BackBagInventory>inventory_iter=backbags.iterator();
		if(inventory_iter==null) return;
		while(inventory_iter.hasNext()) {
			BackBagInventory bag=inventory_iter.next();
			if(bag.isTemporary()) {
				inventory_iter.remove();;
			} else {
				bag.convert();
			}
		}
		Gson gson=new GsonBuilder().setPrettyPrinting().create();
		File file=new File(url+"/"+uuid.toString());
		if(!backbags.isEmpty()) {
			try(FileWriter writer=new FileWriter(file)){
				BackBagInventory[]array=backbags.toArray(new BackBagInventory[backbags.size()]);
				gson.toJson(array,writer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		bags.remove(uuid);
	}
	
	public static void saveBags() {
		if(bags==null) return;
		Iterator<Entry<UUID,List<BackBagInventory>>>bag_iterator=bags.entrySet().iterator();
		while(bag_iterator!=null&&bag_iterator.hasNext()) {
			Map.Entry<UUID,List<BackBagInventory>>entry=bag_iterator.next();
			UUID uuid=entry.getKey();
			if(Bukkit.getOfflinePlayer(uuid)!=null) saveBags(uuid,entry.getValue());
		}
	}
	
	public static void expandBackBag(Entity owner,String bag_name,int size) {
		size=size%9>0?size+(9-size%9):size;
		if(BackBagHelper.hasBackBag(owner.getUniqueId())) {
			BackBagInventory bag=BackBagHelper.getBagInventory(owner.getUniqueId(),bag_name);
			if(bag.getSize()>size) {
				List<ItemStack>content=Arrays.asList(bag.getInventory().getContents()).stream().filter(p->p!=null&&p.getType()!=Material.AIR).collect(Collectors.toList());
				if(content.size()>size) {
					for(int i1=content.size()-1;i1>=size;i1--) {
						content.remove(i1);
					}
				}
				Inventory new_inv=Bukkit.createInventory(null,size);
				new_inv.setContents(content.toArray(new ItemStack[content.size()]));
				bag.setInventory(bag_name,new_inv);
			} else {
				Inventory new_inv=Bukkit.createInventory(null,size);
				new_inv.setContents(bag.getInventory().getContents());
				bag.setInventory(bag_name,new_inv);
			}
		}
	}

}
