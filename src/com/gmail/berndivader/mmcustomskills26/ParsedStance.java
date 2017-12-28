package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class ParsedStance extends SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill,
INoTargetSkill {
	
	protected String stance;
	protected MobManager mobmanager = Main.getPlugin().getMobManager();

	public ParsedStance(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		String s = mlc.getString(new String[]{"stance","s"});
		if (s.startsWith("\"") && s.endsWith("\"")) s=s.substring(1,s.length()-1);
		s=SkillString.parseMessageSpecialChars(s);
		this.stance=s;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castCast(data,data.getCaster().getEntity(),null);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return castCast(data,target,null);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return castCast(data,null,target);
	}
	
	private boolean castCast(SkillMetadata data,AbstractEntity e1,AbstractLocation l1) {
		if (mobmanager.isActiveMob(data.getCaster().getEntity())) {
			ActiveMob am = mobmanager.getMythicMobInstance(data.getCaster().getEntity());
			String s=CustomSkillStuff.parseMobVariables(this.stance,data,data.getCaster().getEntity(),e1,l1);
			am.setStance(s);
			return true;
		}
		return false;
	}
}
