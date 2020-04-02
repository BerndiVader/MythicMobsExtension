package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.conditions.ISkillMetaCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "totaltarget,totaltargets", author = "BerndiVader")
public class TotalTargetAmount extends AbstractCustomCondition implements ISkillMetaCondition {
	PlaceholderString amount;

	public TotalTargetAmount(String line, MythicLineConfig mlc) {
		super(line, mlc);
		amount = mlc.getPlaceholderString(new String[] { "amount", "a", "targets" }, ">0");
	}

	@Override
	public boolean check(SkillMetadata data) {
		RangedDouble range = new RangedDouble(amount.get(data));
		if (data.getEntityTargets() != null) {
			return range.equals(data.getEntityTargets().size());
		} else if (data.getLocationTargets() != null) {
			return range.equals(data.getLocationTargets().size());
		}
		return false;
	}
}
