package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.ps.PS;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;

public
class
FactionsSupport
implements 
Listener
{
	static String str_pluginName;
	
	public static enum FlagTypes {
		animals, 
		monsters, 
		peaceful, 
		endergrief, 
		explosions, 
		firespread,
		friendlyfire,
		infpower,
		offlineexplosions,
		open, 
		permanent,
		powergain, 
		powerloss, 
		pvp, 
		zombiegrief
	}
	
	static {
		str_pluginName="Factions";
	}

	public FactionsSupport() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		Main.logger.info("using "+str_pluginName);
	}
	
	public static boolean checkRegionFlag(Location l, String flag) {
		Faction f = BoardColl.get().getFactionAt(PS.valueOf(l));
		if(f==null) return false;
		FlagTypes flagtype=null;
		try {
			flagtype=FlagTypes.valueOf(flag);
		} catch (Exception ex) {
			Main.logger.warning("The flag "+flag+" is no valid factions flag!");
		}
		return f.getFlag(flagtype.name());
	}	

	public static boolean inFaction(Location l,String[]regions) {
		Faction faction=BoardColl.get().getFactionAt(PS.valueOf(l));
		if(faction==null) return false;
		String factionName=faction.getComparisonName().toLowerCase();
		int size=regions.length;
		for(int i1=0;i1<size;i1++) {
			if(regions[i1].equals(factionName)) return true;
		}
		return false;
	}
	
	public static boolean playersFaction(Player player,String[]factions) {
		MPlayer mplayer=MPlayerColl.get().getByName(player.getName());
		int size=factions.length;
		String faction=mplayer.getFactionName().toLowerCase();
		for(int i1=0;i1<size;i1++) {
			if(faction.equals(factions[i1])) return true;
		}
		return false;
	}
	
	public static boolean playerInHomeFaction(Player player) {
		MPlayer mplayer=MPlayerColl.get().getByName(player.getName());
		if(mplayer==null) return false;
		return mplayer.getFactionName().toLowerCase().equals(BoardColl.get().getFactionAt(PS.valueOf(player.getLocation())).getComparisonName());
	}
	
	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		switch(e.getConditionName().toLowerCase()) {
		case "infactionsregion":
		case "infactionsregions":
		case "infactionsregion_ext":
		case "infactionsregions_ext":
			e.register(new FactionsRegionCondition(e.getConfig().getLine(),e.getConfig()));
			break;
		case "factionsflag":
		case "factionsflags":
		case "factionsflag_ext":
		case "factionsflags_ext":
			e.register(new FactionsFlagCondition(e.getConfig().getLine(),e.getConfig()));
			break;
		case "playerinfaction":
		case "playerinfactions":
		case "playerfaction":
		case "playersfaction":
		case "playerinfaction_ext":
		case "playerinfactions_ext":
		case "playerfaction_ext":
		case "playersfaction_ext":
			e.register(new PlayerInFactionsCondition(e.getConfig().getLine(), e.getConfig()));
			break;
		case "playershomefaction":
		case "playerinhomefaction":
		case "playershomefaction_ext":
		case "playerinhomefaction_ext":
			e.register(new PlayerInHomeFactionCondition(e.getConfig().getLine(), e.getConfig()));
			break;
		}
	}

}
