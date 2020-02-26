package com.gmail.berndivader.mythicmobsext.conditions;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public 
class
AbstractCustomCondition
extends 
SkillCondition 
{
	boolean dba,debug;
	static boolean use_placeholder_actionvar;
	
	static {
		use_placeholder_actionvar=Utils.action_var_field.getAnnotatedType().getType().getTypeName().endsWith("PlaceholderString");
	}
	
	public AbstractCustomCondition(String line, MythicLineConfig mlc) {
		super(line);
		
		String action="TRUE";
		String action_var="0";
		String a=mlc.getString("action","");
		debug=dba=mlc.getBoolean("debug",false);
		int size=ConditionAction.values().length;
		for(int i=0;i<size;i++){
			String aa=ConditionAction.values()[i].toString();
			if (aa.toUpperCase().equals("CAST")&&a.toUpperCase().startsWith("CASTINSTEAD")) continue;
			if (a.toUpperCase().startsWith(aa)) {
				action=aa;
				action_var=a.substring(action.length(),a.length());
				break;
			}
		}
		try {
			this.ACTION=ConditionAction.valueOf(action.toUpperCase());
			this.actionVar=new PlaceholderString(action_var);
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
		if (dba) {
			try {
				System.err.println(this.ACTION.toString()+":"+Utils.action_var_field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}