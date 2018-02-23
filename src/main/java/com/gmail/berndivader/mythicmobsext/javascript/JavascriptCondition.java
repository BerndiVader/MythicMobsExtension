package com.gmail.berndivader.mythicmobsext.javascript;

import javax.script.ScriptException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

@ExternalAnnotation(name="jscondition",author="BerndiVader")
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
		String s1=mlc.getString(new String[] {"js","eval","invok"},js);
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
	
	private boolean eval(MythicLineConfig mlc,Entity e1,Location l1) {
		try {
			return (boolean)Nashorn.get().invoc.invokeFunction(js,e1!=null?(Entity)e1:l1!=null?(Location)l1:null,mlc);
		} catch (NoSuchMethodException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
