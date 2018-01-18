package com.gmail.berndivader.mythicmobsext;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IMetaSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CustomRandomSkillMechanic extends SkillMechanic 
implements
IMetaSkill {
	MythicMobs mythicmobs;
	SkillManager skillmanager;
	LinkedHashMap<Integer,SkillEntry> entrylist;
	boolean b1;
	
	public CustomRandomSkillMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
        	this.target_creative = true;
		this.mythicmobs = Main.getPlugin().getMythicMobs();
		this.skillmanager = this.mythicmobs.getSkillManager();
		this.entrylist = new LinkedHashMap<>();
		
		String parse[] = mlc.getString(new String[] { "skills", "s" }).split(",");
		b1(mlc.getBoolean(new String[] {"renewrandom","newrandom","rnr"},false));

		if (parse.length>0) {
			for (int a=0;a<parse.length;a++) {
				String s=null;
				double c=0;
				String par[] = parse[a].split(":");
				s = par[0];
				if (par.length>1) c=Double.parseDouble(par[1]);
				SkillEntry entry = new SkillEntry(a,c,s);
				if (entry.isSkillPresent()) entrylist.put(a,entry);
			}
		}
	}

	private void b1(boolean b1) {
		this.b1=b1;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		double r = ThreadLocalRandom.current().nextDouble();
		for (Entry<Integer,SkillEntry> entry:entrylist.entrySet()) {
			SkillEntry sentry = entry.getValue();
			if (b1) r=ThreadLocalRandom.current().nextDouble();
			if (r<=sentry.getChance()) {
				if (sentry.isSkillPresent()
						&& sentry.getSkill().isUsable(data)) {
					sentry.getSkill().execute(data);
					return true;
				}
			}
		}
		return false;
	}
	
	public class SkillEntry {
		protected double chance;
		protected int priority;
		protected Optional<Skill> skill = Optional.empty();
		
		public SkillEntry(int priority, double chance, String skill) {
			this.skill = CustomRandomSkillMechanic.this.skillmanager.getSkill(skill);
			this.chance = chance;
			this.priority = priority;
		}
		public boolean isSkillPresent() {
			return this.skill.isPresent();
		}
		public Skill getSkill() {
			return this.skill.get();
		}
		public double getChance() {
			return this.chance;
		}
		public int getPriority() {
			return this.priority;
		}
	}

}
