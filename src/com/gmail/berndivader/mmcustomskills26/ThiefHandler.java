package com.gmail.berndivader.mmcustomskills26;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class ThiefHandler {
    private final Set<Thief> thiefs = new HashSet<>();

	public Set<Thief> getThiefs() {
		return thiefs;
	}
	public boolean addThief(UUID uuid, ItemStack item) {
		thiefs.add(new Thief(uuid, item));
		return true;
	}
	public Thief getThief(UUID uuid) {
		for (Thief thief : thiefs) {
			if (thief.getUuid().equals(uuid)) {return thief;}
		}
		return null;
	}
	public void removeThief(Thief thief) {
		thiefs.remove(thief);
	}
	public int Size() {
		return thiefs.size();
	}
}
