package com.gmail.berndivader.healthbar;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

public class Healthbar extends CraftHologram {
	protected Entity entity;
	protected UUID uuid;
	protected double offset;
	protected String line;
	
	public Healthbar(Entity entity) {
		this(entity, 0.0d, "");
	}
	public Healthbar(Entity entity, double offset) {
		this(entity,offset,"");
	}
	@SuppressWarnings("deprecation")
	public Healthbar(Entity entity, double offset, String line) {
		super(entity.getLocation().add(0, offset, 0));
		this.uuid = entity.getUniqueId();
		this.line = line;
		this.entity = entity;
		this.offset = offset;
		HealthbarHandler.healthbars.put(this.uuid, this);
		line = line + Double.toString(this.getHealth());
		this.addLine(line);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean update() {
		if (this.isDeleted()) return false;
		Location l = this.entity.getLocation();
		World w = l.getWorld();
		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
//		this.setLine(1, this.line+Double.toString(this.getHealth()));
		this.teleport(w, x, y+this.offset, z);
		return true;
	}
	
	public double getHealth() {
		double d = 0;
		if (this.entity instanceof LivingEntity) {
			LivingEntity le = (LivingEntity)this.entity;
			d = CustomSkillStuff.round(le.getHealth(), 0);
		}
		return d;
	}
	
	public void remove() {
		HealthbarHandler.healthbars.remove(this.uuid);
		this.delete();
	}
}
