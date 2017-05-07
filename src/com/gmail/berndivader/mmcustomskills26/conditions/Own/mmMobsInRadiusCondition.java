package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.entities.MythicEntity;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class mmMobsInRadiusCondition extends SkillCondition implements IEntityCondition {
	private String[] t;
	private RangedDouble a;
	private double r;
	private  HashSet<MythicMob> mmT = new HashSet<MythicMob>();
	private  HashSet<MythicEntity> meT = new HashSet<MythicEntity>();

	public mmMobsInRadiusCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
		this.t = mlc.getString(new String[]{"mobtypes","types","mobs","mob","type","t","m"},"ALL").split(",");
		this.a = new RangedDouble(mlc.getString(new String[]{"amount","a"},"0"), false);
		this.r = mlc.getDouble(new String[]{"radius","r"},5);
	    new BukkitRunnable() {
	        public void run() {
	          for (String s : t) {
	            MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(s);
	            if (mm != null) {
	              mmT.add(mm);
	            } else {
	              MythicEntity me = MythicEntity.getMythicEntity(s);
	              if (me != null) {
	                meT.add(me);
	              }
	            }
	          }
	        }
	      }.runTaskLater(Main.getPlugin(), 1L);
	}

	@Override
	public boolean check(AbstractEntity ae) {
		int count = 0;
		for (Iterator<AbstractEntity>it = MythicMobs.inst().getEntityManager().getLivingEntities(ae.getWorld()).iterator();it.hasNext();) {
			AbstractEntity e = it.next();
			if (e.getBukkitEntity().getUniqueId().equals(ae.getBukkitEntity().getUniqueId())) continue;
			if (ae.getLocation().distanceSquared(e.getLocation())<Math.pow(this.r, 2.0D)) {
				ActiveMob am = MythicMobs.inst().getMobManager().getMythicMobInstance(e);
				if (am!=null) {
					if (mmT.contains(am.getType()) || Arrays.asList(this.t).contains("ALL")) {
						count++;
						am=null;
					}
				} else {
					for (MythicEntity me : this.meT) {
						if (me.compare(e.getBukkitEntity())) {
							count++;
							break;
						}
					}
				}
			}
		}
		return this.a.equals((int)count);
	}

}
