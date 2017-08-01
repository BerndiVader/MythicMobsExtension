package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.EntityManager;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class mmMobsInRadiusCondition extends mmCustomCondition implements ILocationCondition {
	protected EntityManager entitymanager;
	protected MobManager mobmanager;
	private String[] t;
	private RangedDouble a;
	private double r;
	private HashSet<MythicMob> mmT = new HashSet<MythicMob>();

	public mmMobsInRadiusCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.entitymanager = Main.getPlugin().mythicmobs.getEntityManager();
		this.mobmanager = Main.getPlugin().getMobManager();
		this.t = mlc.getString(new String[] { "mobtypes", "types", "mobs", "mob", "type", "t", "m" }, "ALL").split(",");
		if (this.t[0].toUpperCase().equals("ALL"))
			this.t[0] = "ALL";
		this.a = new RangedDouble(mlc.getString(new String[] { "amount", "a" }, "0"), false);
		this.r = mlc.getDouble(new String[] { "radius", "r" }, 5);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String s : t) {
					MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(s);
					if (mm != null) {
						mmT.add(mm);
					}
				}
			}
		}.runTaskLater(Main.getPlugin(), 1L);
	}

	@Override
	public boolean check(AbstractLocation l) {
		int count = 0;
		for (Iterator<AbstractEntity> it = this.entitymanager.getLivingEntities(l.getWorld()).iterator(); it
				.hasNext();) {
			AbstractEntity e = it.next();
			double diffsq = l.distanceSquared(e.getLocation());
			if (diffsq <= Math.pow(this.r, 2.0D)) {
				ActiveMob am = this.mobmanager.getMythicMobInstance(e);
				if (am != null) {
					if (mmT.contains(am.getType()) || this.t[0].equals("ALL")) {
						count++;
						am = null;
					}
				}
			}
		}
		return this.a.equals(count);
	}

}
