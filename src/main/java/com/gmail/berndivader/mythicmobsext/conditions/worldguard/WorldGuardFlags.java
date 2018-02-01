package main.java.com.gmail.berndivader.mythicmobsext.conditions.worldguard;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import main.java.com.gmail.berndivader.mythicmobsext.Main;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class WorldGuardFlags {
	private Set<EntityType> entitytypes;
	private WorldGuardPlugin worldguard = Main.getPlugin().wg;
	private FlagRegistry fr = worldguard.getFlagRegistry();

	@SuppressWarnings("deprecation")
	public boolean checkRegionStateFlagAtLocation(Location l, String f) {
		Flag<?> flag = DefaultFlag.fuzzyMatchFlag(fr, f);
		if (!(flag instanceof StateFlag))
			return false;
		RegionManager rm = this.worldguard.getRegionManager(l.getWorld());
		ApplicableRegionSet rs = rm.getApplicableRegions(l);
		if (rs.getFlag((StateFlag) flag) == null)
			return false;
		return rs.allows((StateFlag) flag);
	}

	@SuppressWarnings("deprecation")
	public boolean checkRegionDenySpawnFlagAtLocation(Location l, String[] entitylist) {
		EntityType chkType;
		RegionManager rm = this.worldguard.getRegionManager(l.getWorld());
		ApplicableRegionSet rs = rm.getApplicableRegions(l);
		this.entitytypes = rs.getFlag(DefaultFlag.DENY_SPAWN);
		if (this.entitytypes == null)
			return false;
		for (String s : Arrays.asList(entitylist)) {
			try {
				chkType = EntityType.valueOf(s);
			} catch (Exception ex) {
				continue;
			}
			if (chkType != null && this.entitytypes.contains(chkType)) {
				return true;
			}
		}
		return false;
	}
}
