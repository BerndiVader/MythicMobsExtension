package com.gmail.berndivader.mythicmobsext.mechanics.customprojectiles;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

@ExternalAnnotation(name = "modifyprojectile", author = "BerndiVader")
public class ModifyProjectile extends SkillMechanic implements IParentSkill {

	public ModifyProjectile(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled() {
		// TODO Auto-generated method stub

	}

}
