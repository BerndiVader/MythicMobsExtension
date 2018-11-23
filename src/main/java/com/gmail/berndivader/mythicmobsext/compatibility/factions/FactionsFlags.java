package com.gmail.berndivader.mythicmobsext.compatibility.factions;

import org.bukkit.Location;

import com.gmail.berndivader.mythicmobsext.Main;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

public 
class 
FactionsFlags 
{
	enum FlagTypes {
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

	public static boolean checkFlag(Location l, String flag) {
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
}
