package com.gmail.berndivader.mythicmobsext.mechanics;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="parsedstance,pstance",author="BerndiVader")
public class ParsedStanceMechanic extends SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill,
INoTargetSkill {
	
	protected PlaceholderString stance;

	public ParsedStanceMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		String s = mlc.getString(new String[]{"stance","s"});
		if (s.startsWith("\"") && s.endsWith("\"")) s=s.substring(1,s.length()-1);
		s=SkillString.parseMessageSpecialChars(s);
		this.stance=new PlaceholderString(s);
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
		if (Utils.mobmanager.isActiveMob(data.getCaster().getEntity())) {
			ActiveMob am = Utils.mobmanager.getMythicMobInstance(data.getCaster().getEntity());
			String s=this.stance.get(data,e1);
			am.setStance(s);
			return true;
		}
		return false;
	}
}
