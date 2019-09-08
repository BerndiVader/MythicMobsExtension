package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="parsedstance,pstance",author="BerndiVader")
public class ParsedStanceCondition
extends
AbstractCustomCondition
implements
IEntityComparisonCondition {
	private PlaceholderString stance;
	private boolean compareToSelf;

	public ParsedStanceCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String temp=mlc.getString(new String[]{"stance","s"});
		this.compareToSelf=mlc.getBoolean(new String[]{"compareself","cs"},false);
		if (temp!=null&&(temp.startsWith("\"")&&temp.endsWith("\""))) {
			temp=temp.substring(1,temp.length()-1);
		}
		this.stance=new PlaceholderString(temp);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity ae) {
		ActiveMob am=Utils.mobmanager.getMythicMobInstance(caster);
		SkillMetadata data=new SkillMetadata(SkillTrigger.API,am,ae);
		String stance=this.stance.get(data,ae);
		ActiveMob target=this.compareToSelf?am:Utils.mobmanager.getMythicMobInstance(ae);
		return target.getStance().contains(stance);
	}

}
