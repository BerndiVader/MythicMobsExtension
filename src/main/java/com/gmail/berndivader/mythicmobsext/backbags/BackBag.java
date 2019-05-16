package com.gmail.berndivader.mythicmobsext.backbags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;

public 
class
BackBag
{
	static HashMap<UUID,Inventory>bags;
	static String str_name;
	static String url;
	
	Entity owner;
	Inventory inventory;
	int size;
	
	static {
		url=Main.getPlugin().getDataFolder().getPath()+"/backbags";
		new File(url).mkdirs();
		bags=new HashMap<>();
		str_name="BackBag";
	}

	public BackBag(Entity owner,int size) {
		this.owner=owner;
		this.size=size;
		if((inventory=getInventory(owner.getUniqueId()))==null) {
			inventory=Bukkit.createInventory(null,9+size,str_name);
			addInventory(owner.getUniqueId(),inventory);
		}
	}
	
	public void viewBackBag(Player player) {
		player.openInventory(inventory);
	}
	
	public boolean isPresent() {
		return this.inventory!=null;
	}
	
	static void addInventory(UUID uuid, Inventory inventory) {
		if(bags.containsKey(uuid)) {
			bags.replace(uuid,inventory);
		} else {
			bags.put(uuid,inventory);
		}
	}
	
	static Inventory getInventory(UUID uuid) {
		return bags.get(uuid);
	}
	
	public static void loadBags() {
		File dir=new File(url);
		File[]files=dir.listFiles();
		int size=files.length;
		for(int i1=0;i1<size;i1++) {
			File file=files[i1];
			if(file.exists()) {
				try(Scanner scanner=new Scanner(file)) {
					UUID uuid=UUID.fromString(file.getName());
				    ItemStack[] contents=BukkitSerialization.itemStackArrayFromBase64(scanner.useDelimiter("\\A").next());
				    Inventory inventory=Bukkit.createInventory(null,contents.length);
				    inventory.setContents(contents);
				    addInventory(uuid,inventory);
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
		}
	}
	
	public static void saveBags() {
		Iterator<Entry<UUID,Inventory>>it=bags.entrySet().iterator();
		while(it.hasNext()) {
			Entry<UUID,Inventory>entry=it.next();
			UUID uuid=entry.getKey();
			if(Bukkit.getEntity(uuid) instanceof Player) {
				Inventory bag=entry.getValue();
				String str_uuid=uuid.toString();
				String base64=BukkitSerialization.itemStackArrayToBase64(bag.getContents());
				try (PrintStream out=new PrintStream(new FileOutputStream(url+"/"+str_uuid))) {
				    out.print(base64);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}			
			}
		}
	}
	
}