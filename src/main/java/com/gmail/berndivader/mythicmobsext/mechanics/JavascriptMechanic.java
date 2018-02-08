package com.gmail.berndivader.mythicmobsext.mechanics;

import javax.script.ScriptException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.utils.Nashorn;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class JavascriptMechanic 
extends 
SkillMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill,
ITargetedLocationSkill {
	boolean simple;
	String js="print('Hello World!');";
	MythicLineConfig mlc;
	
	public JavascriptMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.mlc=mlc;
		simple=mlc.getBoolean("simple",false);
		String s1=mlc.getString("js",js);
		js=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return eval(data,null,BukkitAdapter.adapt(target));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return eval(data,target.getBukkitEntity(),null);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return eval(data,null,null);
	}

	private boolean eval(SkillMetadata data,Entity et,Location lt) {
		try {
			Nashorn.invoc.invokeFunction(js,data,et!=null?(Entity)et:lt!=null?(Location)lt:null,mlc);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		return true;
	}

}
