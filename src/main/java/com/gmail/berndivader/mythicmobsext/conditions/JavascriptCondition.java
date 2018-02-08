package com.gmail.berndivader.mythicmobsext.conditions;

import javax.script.ScriptException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.utils.Nashorn;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class JavascriptCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition,
ILocationCondition {
	boolean simple;
	String js="print('Hello World!');";
	MythicLineConfig mlc;
	
	public JavascriptCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.mlc=mlc;
		simple=mlc.getBoolean("simple",false);
		String s1=mlc.getString("js",js);
		js=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
	}

	@Override
	public boolean check(AbstractLocation arg0) {
		return eval(mlc,null,BukkitAdapter.adapt(arg0));
	}

	@Override
	public boolean check(AbstractEntity arg0) {
		return eval(mlc,arg0.getBukkitEntity(),null);
	}
	
	private boolean eval(MythicLineConfig mlc,Entity et,Location lt) {
		try {
			Nashorn.invoc.invokeFunction(js,null,et!=null?(Entity)et:lt!=null?(Location)lt:null,mlc);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		return true;
	}

}
