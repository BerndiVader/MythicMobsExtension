package com.gmail.berndivader.mythicmobsext.javascript;

import javax.script.ScriptException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.externals.*;

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

@ExternalAnnotation(name = "jsmechanic", author = "BerndiVader")
public class JavascriptMechanic extends SkillMechanic
		implements INoTargetSkill, ITargetedEntitySkill, ITargetedLocationSkill {
	boolean simple;
	String js = "print('Hello World!');";
	MythicLineConfig mlc;

	public JavascriptMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.mlc = mlc;
		simple = mlc.getBoolean("simple", false);
		String s1 = mlc.getString(new String[] { "js", "eval", "invok" }, js);
		js = SkillString.parseMessageSpecialChars(s1.substring(1, s1.length() - 1));
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		return eval(data, null, BukkitAdapter.adapt(target));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		return eval(data, target.getBukkitEntity(), null);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return eval(data, null, null);
	}

	private boolean eval(SkillMetadata data, Entity e1, Location l1) {
		try {
			Nashorn.get().invoc.invokeFunction(js, data, e1 != null ? (Entity) e1 : l1 != null ? (Location) l1 : null,
					mlc);
		} catch (NoSuchMethodException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
