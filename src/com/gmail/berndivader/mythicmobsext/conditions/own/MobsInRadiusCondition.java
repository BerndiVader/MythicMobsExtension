package com.gmail.berndivader.mythicmobsext.conditions.own;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class MobsInRadiusCondition
extends 
AbstractCustomCondition
implements 
ILocationCondition {
	private MobManager mobmanager;
	private RangedDouble a;
	private double r;
	private boolean all=false;
	private String[]ml;

	public MobsInRadiusCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.mobmanager = Main.getPlugin().getMobManager();
		ml=mlc.getString(new String[] { "mobtypes", "types", "mobs", "mob", "type", "t", "m" }, "ALL").toUpperCase().split(",");
		if (ml.length>0&&ml[0].equals("ALL")) this.all=true;
		this.a=new RangedDouble(mlc.getString(new String[] { "amount","a" }, "0"),false);
		this.r=mlc.getDouble(new String[] { "radius", "r" },5);
	}

	@Override
	public boolean check(AbstractLocation location) {
		int count=0;
		Location l=BukkitAdapter.adapt(location);
		for (Iterator<LivingEntity> it=l.getWorld().getLivingEntities().iterator();it.hasNext();) {
			LivingEntity e=it.next();
			Location el=e.getLocation();
			if (!el.getWorld().equals(l.getWorld())) continue;
			double diffsq=l.distanceSquared(el);
			if (diffsq<=Math.pow(this.r,2.0D)) {
				ActiveMob am=this.mobmanager.getMythicMobInstance(e);
				if (am==null) continue;
				if (this.all) {
					count++;
				} else {
					for(String s1:ml) {
						if (s1.equals(am.getType().getInternalName().toUpperCase()))count++;
					}
				}
				am=null;
			}
		}
		return this.a.equals((double)count);
	}

}
