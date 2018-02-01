package main.java.com.gmail.berndivader.mythicmobsext.mechanics;

import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ModifyArrowsMechanic 
extends
SkillMechanic
implements
ITargetedEntitySkill {
	private int a;
	private char m;

	public ModifyArrowsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.m=mlc.getString(new String[] { "mode", "m" }, "c").toUpperCase().charAt(0);
		this.a=mlc.getInteger(new String[] { "amount", "a" }, 1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			Volatile.handler.modifyArrowsAtEntity(target.getBukkitEntity(),this.a,this.m);
			return true;
		}
		return false;
	}

}
