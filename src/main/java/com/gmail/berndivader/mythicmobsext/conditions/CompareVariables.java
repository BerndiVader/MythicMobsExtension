package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="comparevariable,cmpvar",author="BerndiVader")
public 
class
CompareVariables
extends 
AbstractCustomCondition
implements
IEntityComparisonCondition
{
	PlaceholderString compare;
	Pattern pattern;
	
	public CompareVariables(String line, MythicLineConfig mlc) {
		super(line, mlc);
		compare=mlc.getPlaceholderString(new String[]{"c","cmp","compare"}, "");
		pattern=Pattern.compile("[=<>]+");
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		SkillMetadata data=new SkillMetadata(SkillTrigger.API,new GenericCaster(caster),target);
		String outcome=compare.get(data,target);
		Matcher matcher=pattern.matcher(outcome);
		if(matcher.find()) {
			String equal=matcher.group();
			String parsed[]=outcome.split("[=<>]+");
			if(parsed.length==2) {
				RangedDouble ranged=new RangedDouble(equal+parsed[1]);
				return ranged.equals(Double.parseDouble(parsed[0]));
			}
		}
		return false;
	}

}
