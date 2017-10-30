package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class mmMobsInRadiusCondition extends mmCustomCondition implements ILocationCondition {
	protected MobManager mobmanager;
	private RangedDouble a;
	private double r;
	private boolean all=false;
	private HashSet<String> mList = new HashSet<String>();

	public mmMobsInRadiusCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.mobmanager = Main.getPlugin().getMobManager();
		String[]t = mlc.getString(new String[] { "mobtypes", "types", "mobs", "mob", "type", "t", "m" }, "ALL").toUpperCase().split(",");
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
		int count = 0;
		Location l = BukkitAdapter.adapt(location);
		for (Iterator<LivingEntity> it = l.getWorld().getLivingEntities().iterator(); it.hasNext();) {
			LivingEntity e = it.next();
			Location el = e.getLocation();
			if (!el.getWorld().equals(l.getWorld())) continue;
			double diffsq = l.distanceSquared(el);
			if (diffsq <= Math.pow(this.r, 2.0D)) {
				ActiveMob am = this.mobmanager.getMythicMobInstance(e);
				if (am==null) continue;
				if (mList.contains(am.getType().getInternalName().toUpperCase()) || this.all) {
					count++;
				}
				am = null;
			}
		}
		return this.a.equals(count);
	}

}
