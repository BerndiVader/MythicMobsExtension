package com.gmail.berndivader.mmcustomskills26.conditions;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;

public class mmCustomCondition extends SkillCondition {

	public mmCustomCondition(String line, MythicLineConfig mlc) {
		super(line);
		String action="TRUE",actionVar="0";
		String a=mlc.getString("action","");
		for(int a1=0;a1<ConditionAction.values().length;a1++){
			String aa=ConditionAction.values()[a1].toString();
			if (aa.toUpperCase().equals("CAST")&&a.toUpperCase().startsWith("CASTINSTEAD")) continue;
			if (a.toUpperCase().startsWith(aa)) {
				action=aa;
				actionVar=a.substring(action.length(),a.length());
				break;
			}
		}
		try {
			this.ACTION = ConditionAction.valueOf(action.toUpperCase());
			this.actionVar=actionVar;
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
	}

}