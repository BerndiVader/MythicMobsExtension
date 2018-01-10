package com.gmail.berndivader.mythicmobsext;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.*;

public class SetFactionMechanic
extends
SkillMechanic
implements
ITargetedEntitySkill,
INoTargetSkill {
	
	protected String faction;
	public SetFactionMechanic(String line, MythicLineConfig mlc) {
		
		super(line, mlc);
		String f=mlc.getString(new String[] { "faction", "f" }, null);
		if (f!=null) {
			this.faction=SkillString.unparseMessageSpecialChars(f);
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return this.castAtEntity(data, data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		ActiveMob am = Main.mythicmobs.getMobManager().getMythicMobInstance(target);
		String f=SkillString.parseMobVariables(this.faction,data.getCaster(),target,data.getTrigger());
		if (am!=null) am.setFaction(f);
		return true;
	}
}
