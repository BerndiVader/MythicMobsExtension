package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.Iterator;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ISkillMetaCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="cmptargetvar,comparetargetvariables",author="BerndiVader")
public 
class
CompareVariables
extends 
AbstractCustomCondition
implements
ISkillMetaCondition,
IEntityComparisonCondition
{
	PlaceholderString compare;
	
	public CompareVariables(String line, MythicLineConfig mlc) {
		super(line, mlc);
		compare=mlc.getPlaceholderString(new String[]{"c","cmp","compare"}, "");
	}

	@Override
	public boolean check(SkillMetadata data) {
		if(data.getEntityTargets()!=null) {
			Iterator<AbstractEntity>iter=data.getEntityTargets().iterator();
			while(iter.hasNext()) {
				AbstractEntity target=iter.next();
			}
		}
		return false;
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		return false;
	}

}
