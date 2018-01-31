package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class HitBox {
	World world;
	double lowX;
	double lowY;
	double lowZ;
	double highX;
	double highY;
	double highZ;

	public HitBox(Location center, double radius) {
		this(center, radius, radius);
	}

	public HitBox(Location center, double horizRadius, double vertRadius) {
		this.world = center.getWorld();
		this.lowX = center.getX() - horizRadius;
		this.lowY = center.getY() - vertRadius;
		this.lowZ = center.getZ() - horizRadius;
		this.highX = center.getX() + horizRadius;
		this.highY = center.getY() + vertRadius;
		this.highZ = center.getZ() + horizRadius;
	}

	public boolean contains(Location location) {
		if (!location.getWorld().equals(this.world)) {
			return false;
		}
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		return this.lowX <= x && x <= this.highX && this.lowY <= y && y <= this.highY && this.lowZ <= z
				&& z <= this.highZ;
	}

	public boolean contains(Entity entity) {
		return this.contains(entity.getLocation());
	}
}
