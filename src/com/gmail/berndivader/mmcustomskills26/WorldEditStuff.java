package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class WorldEditStuff {
	
	public static void dosphere(Location l, int mp, Material material, String size, int undoticks, boolean mask) {
		Sphere sphere = new Sphere();
		Vector v = l.getDirection();
		Location fl = l.add(v.multiply(mp));
		sphere.drawsphere(fl, material, size, undoticks, mask);
	}
}
