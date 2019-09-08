package com.gmail.berndivader.mythicmobsext.conditions;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;

public 
class
AbstractCustomCondition
extends 
SkillCondition 
{
	boolean dba;

	public AbstractCustomCondition(String line, MythicLineConfig mlc) {
		super(line);
		String action="TRUE";
		String actionVar="0";
		String a=mlc.getString("action","");
		dba=mlc.getBoolean("debug",false);
		for(int i=0;i<ConditionAction.values().length;i++){
			String aa=ConditionAction.values()[i].toString();
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
		if (dba) System.err.println(this.ACTION.toString()+":"+this.actionVar);
	}

}