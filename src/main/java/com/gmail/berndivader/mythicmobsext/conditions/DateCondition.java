package com.gmail.berndivader.mythicmobsext.conditions;

import java.text.SimpleDateFormat;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "datetime", author = "BerndiVader")
public class DateCondition extends AbstractCustomCondition implements IEntityCondition {
	SimpleDateFormat dateTime;
	RangedDouble range;

	public DateCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		dateTime = new SimpleDateFormat(mlc.getString("format", "yyyyMMdd"));
		range = new RangedDouble(mlc.getString("value", ">0"));
	}

	@Override
	public boolean check(AbstractEntity var1) {
		return range.equals(Integer.parseInt(dateTime.format(System.currentTimeMillis())));
	}

}
