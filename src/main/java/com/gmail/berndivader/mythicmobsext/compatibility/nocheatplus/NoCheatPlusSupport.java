package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;

import fr.neatmonster.nocheatplus.NoCheatPlus;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;

public 
class 
NoCheatPlusSupport 
implements 
Listener {
	static Map<UUID,SimpleImmutableEntry<Boolean,CheckType[]>>map;
	static Optional<NoCheatPlus>ncp;
	
	static {
		if ((ncp=Optional.ofNullable((NoCheatPlus)Bukkit.getServer().getPluginManager().getPlugin("NoCheatPlus"))).isPresent()) map=new HashMap<>();
	}	
	
	Plugin plugin;
	
	public NoCheatPlusSupport(Plugin plugin) {
		this.plugin=plugin;
		Main.pluginmanager.registerEvents(this,plugin);
	}
	
	public static boolean isPresent() {
		return ncp.isPresent();
	}
	
	@EventHandler
	public void onMythicMechanic(MythicMechanicLoadEvent e) {
		String s1=e.getMechanicName().toLowerCase();
		if (s1.equals("exemptplayer")) {
			e.register(new ExemptPlayerMechanic(e.getConfig().getLine(),e.getConfig()));
		} else if (s1.equals("unexemptplayer")) {
			e.register(new UnExemptPlayerMechanic(e.getConfig().getLine(),e.getConfig()));
		}
	}
	
	@EventHandler	
	public void onMythicCondition(MythicConditionLoadEvent e) {
		String s1=e.getConditionName().toLowerCase();
		if (s1.equals("hasexemption")) e.register(new HasExemptionCondition(e.getConfig().getLine(),e.getConfig()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		c(e.getPlayer().getUniqueId());
	}
	@EventHandler
	public void onPlayerWorld(PlayerChangedWorldEvent e) {
		c(e.getPlayer().getUniqueId());
	}
	
	static void c(UUID uuid) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (map.containsKey(uuid)) {
					CheckType[]types=map.get(uuid).getValue();
					for(int i1=0;i1<types.length;i1++) {
						NCPExemptionManager.unexempt(uuid,types[i1]);
					}
					map.remove(uuid);
				}
			}
		}.runTaskLater(Main.getPlugin(),55L);
	}
	
	public static void zap() {
		for(Map.Entry<UUID,SimpleImmutableEntry<Boolean,CheckType[]>>e1:map.entrySet()) {
			UUID uuid=e1.getKey();
			for(CheckType c1:e1.getValue().getValue()) {
				NCPExemptionManager.unexempt(uuid,c1);
			}
		}
	}
	
	public static CheckType[] inc(CheckType[]arr1,CheckType[]arr2) {
		CheckType[]arr=new CheckType[arr1.length+arr2.length];
		System.arraycopy(arr1,0,arr,0,arr1.length);
		System.arraycopy(arr2,0,arr,arr1.length,arr2.length);
		return arr;
	}
	
	public static CheckType[] dec(CheckType[]arr1,int i1) {
		CheckType[]arr=new CheckType[arr1.length-1];
		if (i1>=0) {
			if(arr.length>0) {
	            System.arraycopy(arr1,0,arr,0,i1);
	            System.arraycopy(arr1,i1+1,arr,i1,arr.length-i1);								
			} else {
				arr=new CheckType[0];
			}
            return arr;
		}
		return arr1;
	}
	
	public static void dec(UUID key,CheckType[]value) {
		if(map.containsKey(key)) {
			CheckType[]arr1=map.get(key).getValue();
			for(int i1=0;i1<value.length;i1++) {
				CheckType c1=value[i1];
				for(int i2=0;i2<arr1.length;i2++) {
					if(!NCPExemptionManager.isExempted(key,arr1[i2])) {
						arr1=dec(arr1,i2);
					} else {
						if (c1.equals(arr1[i2])) {
							NCPExemptionManager.unexempt(key,c1);
							arr1=dec(arr1,i2);
							break;
						}
					}
				}
			}
			map.replace(key,new SimpleImmutableEntry<Boolean,CheckType[]>(map.get(key).getKey(),arr1));
		}
	}
	
	public static void inc(UUID key,CheckType[]value,boolean bl2) {
		if(map.containsKey(key)) {
			CheckType[]arr1=map.get(key).getValue();
			for(int i1=0;i1<value.length;i1++) {
				CheckType c1=value[i1];
				boolean bl1=true;
				for(int i2=0;i2<arr1.length;i2++){
					if(bl1=c1.equals(arr1[i2])) break;
				}
				if(!bl1) {
					NCPExemptionManager.exemptPermanently(key,c1);
					arr1=inc(arr1,new CheckType[]{c1});
				}
			}
			map.replace(key,new SimpleImmutableEntry<Boolean,CheckType[]>(bl2,arr1));
		} else {
			for(CheckType c1:value) {
				NCPExemptionManager.exemptPermanently(key,c1);
			}
			map.put(key,new SimpleImmutableEntry<Boolean,CheckType[]>(bl2,value));
		}
	}
	
}
