package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IMetaSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="customrandomskill,advrandomskill",author="BerndiVader")
public class CustomRandomSkillMechanic extends SkillMechanic 
implements
IMetaSkill {
	SkillManager skillmanager;
	LinkedHashMap<Integer,SkillEntry> entrylist;
	boolean b1;
	
	public CustomRandomSkillMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
        	this.target_creative = true;
		this.skillmanager = Utils.mythicmobs.getSkillManager();
		this.entrylist = new LinkedHashMap<>();
		
		String parse[] = mlc.getString(new String[] { "skills", "s" }).split(",");
		b1(mlc.getBoolean(new String[] {"renewrandom","newrandom","rnr"},false));

		if (parse.length>0) {
			for (int a=0;a<parse.length;a++) {
				String s=null;
				String c="0";
				String par[] = parse[a].split(":");
				s = par[0];
				if (par.length>1) c=par[1];
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
			if (r<=sentry.getChance(data)) {
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
		String chance;
		int priority;
		Optional<Skill> skill = Optional.empty();
		
		public SkillEntry(int priority, String chance, String skill) {
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
		public double getChance(SkillMetadata data) {
			double c;
			if (Character.isDigit(this.chance.charAt(0))) return Double.parseDouble(this.chance);
			AbstractEntity caster=data.getCaster().getEntity();
			AbstractEntity t=data.getEntityTargets()!=null&&data.getEntityTargets().iterator().hasNext()?data.getEntityTargets().iterator().next():null;
			AbstractLocation l=data.getLocationTargets()!=null&&data.getLocationTargets().iterator().hasNext()?data.getLocationTargets().iterator().next():null;
			try {
				c=Double.parseDouble(Utils.parseMobVariables(this.chance,data,caster,t,l))/100;
			} catch (Exception e) {
				c=0d;
			}
			return c;
		}
		public int getPriority() {
			return this.priority;
		}
	}

}
