package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="regex",author="BerndiVader")
public class RegexMechanic 
extends 
SkillMechanic
implements
INoTargetSkill,
ITargetedEntitySkill {
	String regex,format,var;
	Pattern p1;
	Matcher m1;

	public RegexMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		regex=mlc.getString("regex",null);
		format=mlc.getString("format",null).toLowerCase();
		var=mlc.getString("var",null).toLowerCase();
		if (!(var==null||(regex==null||regex!=null&&!regex.startsWith("\""))||format==null)) {
			p1=Pattern.compile(regex);
		} else {
			Main.logger.warning("There is some misconfiguration in regex at "+line);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e1) {
		String s1=null;
		if ((s1=Utils.parseMobVariables(var,data,data.getCaster().getEntity(),e1,null))!=null) {
			
		}
		return false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		castAtEntity(data, data.getCaster().getEntity());
		return true;
	}
}
