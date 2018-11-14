package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

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
	static String pluginName="NoCheatPlus";
	static Map<UUID,SimpleImmutableEntry<Boolean,CheckType[]>>map;
	static Optional<NoCheatPlus>ncp;
	
	static {
		if ((ncp=Optional.ofNullable((NoCheatPlus)Bukkit.getServer().getPluginManager().getPlugin(pluginName))).isPresent()) map=new HashMap<>();
	}	
	
	Plugin plugin;
	
	public NoCheatPlusSupport(Plugin plugin) {
		this.plugin=plugin;
		Main.pluginmanager.registerEvents(this,plugin);
		Main.logger.info("using "+pluginName);
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
		NCPExemptionManager.unexempt(e.getPlayer());
	}
	@EventHandler
	public void onPlayerWorld(PlayerChangedWorldEvent e) {
		NCPExemptionManager.unexempt(e.getPlayer());
	}
	
	public static CheckType[] mkarr(CheckType[]arr1,CheckType[]arr2) {
		CheckType[]arr=new CheckType[arr1.length+arr2.length];
		System.arraycopy(arr1,0,arr,0,arr1.length);
		System.arraycopy(arr2,0,arr,arr1.length,arr2.length);
		return arr;
	}

	public static void exempt(Player p,CheckType[]types) {
		for(int i1=0;i1<types.length;i1++) {
			NCPExemptionManager.exemptPermanently(p,types[i1]);
		}
	}
	public static void unexempt(Player p,CheckType[]types) {
		for(int i1=0;i1<types.length;i1++) {
			NCPExemptionManager.unexempt(p,types[i1]);
		}
	}
	
}
