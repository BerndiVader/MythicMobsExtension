package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="cmptargetvar,comparetargetvariables",author="BerndiVader")
public 
class
CompareVariables
extends 
AbstractCustomCondition
implements
IEntityComparisonCondition
{
	PlaceholderString compare;
	
	public CompareVariables(String line, MythicLineConfig mlc) {
		super(line, mlc);
		compare=mlc.getPlaceholderString(new String[]{"c","cmp","compare"}, "");
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		return false;
	}

}
