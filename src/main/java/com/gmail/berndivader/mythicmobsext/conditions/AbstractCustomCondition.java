package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class AbstractCustomCondition extends SkillCondition {
	boolean dba, debug;
	// Verifying uploading to github works
	public AbstractCustomCondition(String line, MythicLineConfig mlc) {
		super(line);

		debug = dba = mlc.getBoolean("debug", false);
		String a = mlc.getString("action", "");

		if (a.length() != 0) {
			String[] arr = a.split(" ");
			if (ConditionAction.isAction(arr[0])) {
				this.ACTION = ConditionAction.valueOf(arr[0].toUpperCase());
				if (arr.length > 1)
					this.actionVar = PlaceholderString.of(arr[1]);
			} else {
				this.conditionVar = arr[0];
				if (arr.length > 1 && ConditionAction.isAction(arr[1])) {
					this.ACTION = ConditionAction.valueOf(arr[1].toUpperCase());
					if (arr.length > 2)
						this.actionVar = PlaceholderString.of(arr[2]);
				}
			}
		}

		if (dba) {
			try {
				System.err.println(this.ACTION.toString() + ":" + Utils.action_var_field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}