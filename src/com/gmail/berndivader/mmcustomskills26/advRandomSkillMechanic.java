package com.gmail.berndivader.mmcustomskills26;

import java.util.HashSet;
import java.util.Optional;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class advRandomSkillMechanic extends SkillMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill,
ITargetedLocationSkill {
	
	protected MythicMobs mythicmobs;
	protected SkillManager skillmanager;
	protected HashSet<SkillEntry>entrylist;
	
	public advRandomSkillMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
        this.target_creative = true;
		this.mythicmobs = Main.getPlugin().getMythicMobs();
		this.skillmanager = this.mythicmobs.getSkillManager();
		this.entrylist = new HashSet<>();
		
		String parse[] = mlc.getString(new String[]{"skills","s"}).split(",");

		if (parse.length>0) {
			for (int a=0;a<parse.length;a++) {
				String s=null;
				int c=0;
				String par[] = parse[a].split(":");
				s = par[0];
				if (par.length>1) c=Integer.parseInt(par[1]);
				SkillEntry entry = new SkillEntry(c, s);
				if (entry.isSkillPresent()) entrylist.add(entry);
			}
		}
		
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return false;
	}
	
	public class SkillEntry {
		protected int chance;
		protected Optional<Skill> skill = Optional.empty();
		
		public SkillEntry(int chance, String skill) {
			this.skill = advRandomSkillMechanic.this.skillmanager.getSkill(skill);
			this.chance = chance;
		}
		public boolean isSkillPresent() {
			return this.skill.isPresent();
		}
		public Skill getSkill() {
			return this.skill.get();
		}
		public int getChance() {
			return this.chance;
		}
	}

}
