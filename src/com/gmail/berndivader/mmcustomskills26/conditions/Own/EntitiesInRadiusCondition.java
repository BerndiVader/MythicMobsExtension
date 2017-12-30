package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class EntitiesInRadiusCondition
extends
mmCustomCondition
implements
ILocationCondition {
	private RangedDouble a;
	private double r;
	private boolean all=false;
	private String testType;
	private HashSet<String> mList = new HashSet<String>();

	public EntitiesInRadiusCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.testType=line.substring(0,1).toUpperCase();
		String[]t = mlc.getString(new String[] { "entities","entity","types","type","t","e"}, "ALL").toUpperCase().split(",");
		if (t[0].toUpperCase().equals("ALL"))
			this.all=true;
		this.a = new RangedDouble(mlc.getString(new String[] { "amount","a" }, "0"), false);
		this.r = mlc.getDouble(new String[] { "radius", "r" }, 5);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String s : t) {
					mList.add(s);
				}
			}
		}.runTaskLater(Main.getPlugin(), 1L);
	}

	@Override
	public boolean check(AbstractLocation location) {
		switch(this.testType) {
		case "L":
			return checkLivingEntity(location);
		case "P":
			return checkPlayerEntity(location);
		default:
			return checkEntity(location);
		}
	}
	
	private boolean checkEntity(AbstractLocation location) {
		int count = 0;
		Location l = BukkitAdapter.adapt(location);
		for (Iterator<Entity> it = l.getWorld().getEntities().iterator(); it.hasNext();) {
			Entity e = it.next();
			if (!this.all && !mList.contains(e.getType().toString().toUpperCase())) continue;
			Location el = e.getLocation();
			if (!el.getWorld().equals(l.getWorld())) continue;
			double diffsq = l.distanceSquared(el);
			if (diffsq <= Math.pow(this.r, 2.0D)) count++;
		}
		return this.a.equals(count);
	}
	private boolean checkLivingEntity(AbstractLocation location) {
		int count = 0;
		Location l = BukkitAdapter.adapt(location);
		for (Iterator<LivingEntity> it = l.getWorld().getLivingEntities().iterator(); it.hasNext();) {
			LivingEntity e = it.next();
			if (!this.all && !mList.contains(e.getType().toString().toUpperCase())) continue;
			Location el = e.getLocation();
			if (!el.getWorld().equals(l.getWorld())) continue;
			double diffsq = l.distanceSquared(el);
			if (diffsq <= Math.pow(this.r, 2.0D)) count++;
		}
		return this.a.equals(count);
	}
	private boolean checkPlayerEntity(AbstractLocation location) {
		int count = 0;
		Location l = BukkitAdapter.adapt(location);
		for (Iterator<Player> it = l.getWorld().getPlayers().iterator(); it.hasNext();) {
			Player p = it.next();
			Location el = p.getLocation();
			if (!el.getWorld().equals(l.getWorld())) continue;
			double diffsq = l.distanceSquared(el);
			if (diffsq <= Math.pow(this.r, 2.0D)) count++;
		}
		return this.a.equals(count);
	}

}
