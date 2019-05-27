package com.gmail.berndivader.mythicmobsext.backbags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.CreateBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.ExpandBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.MoveToBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.OpenBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.RemoveBackBag;
import com.gmail.berndivader.mythicmobsext.backbags.mechanics.RestoreFromBackBag;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.items.ItemManager;

public 
class 
BackBagHelper
implements
Listener
{
	static ItemManager itemmananger=Utils.mythicmobs.getItemManager();
	static HashMap<UUID,Inventory>bags;
	static String str_name;
	static String url;

	static {
		url=Main.getPlugin().getDataFolder().getPath()+"/backbags";
		new File(url).mkdirs();
		bags=new HashMap<>();
		str_name="BackBag";
	}
	
	public BackBagHelper() {
		loadBags();
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
	
	public static void removeBackBag(UUID uuid) {
		if(bags.containsKey(uuid)) bags.remove(uuid);
	}
	
	public static void addInventory(UUID uuid, Inventory inventory) {
		if(bags.containsKey(uuid)) {
			bags.replace(uuid,inventory);
		} else {
			bags.put(uuid,inventory);
		}
	}
	
	public static boolean hasBackBag(UUID uuid) {
		return bags.containsKey(uuid);
	}
	
	public static Inventory getInventory(UUID uuid) {
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
				    Inventory inventory=Bukkit.createInventory(null,contents.length,str_name);
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

	public static void expandBackBag(Entity owner,int size) {
		size=size%9>0?size+(9-size%9):size;
		if(BackBagHelper.hasBackBag(owner.getUniqueId())) {
			BackBag bag=new BackBag(owner);
			if(bag.getSize()>size) {
				List<ItemStack>content=Arrays.asList(bag.inventory.getContents()).stream().filter(p->p!=null&&p.getType()!=Material.AIR).collect(Collectors.toList());
				if(content.size()>size) {
					for(int i1=content.size()-1;i1>=size;i1--) {
						content.remove(i1);
					}
				}
				Inventory new_inv=Bukkit.createInventory(null,size);
				new_inv.setContents(content.toArray(new ItemStack[content.size()]));
				bag.setInventory(new_inv);
			} else {
				Inventory new_inv=Bukkit.createInventory(null,size);
				new_inv.setContents(bag.inventory.getContents());
				bag.setInventory(new_inv);
			}
		}
	}

}
