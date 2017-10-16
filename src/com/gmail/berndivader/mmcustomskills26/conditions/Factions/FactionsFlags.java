package com.gmail.berndivader.mmcustomskills26.conditions.Factions;

import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

public class FactionsFlags {

	private FlagTypes flagtype;

	private enum FlagTypes {
		animals, monsters, peaceful, endergrief, explosions, firespread, friendlyfire, infpower, offlineexplosions, open, permanent, powergain, powerloss, pvp, zombiegrief
	}

	public boolean checkFlag(Location l, String flag) {
		Faction f = BoardColl.get().getFactionAt(PS.valueOf(l));
		if (f == null)
			return false;
		try {
			flagtype = FlagTypes.valueOf(flag);
		} catch (Exception ex) {
			return false;
		}
		return f.getFlag(flagtype.name());
	}
}
