package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class mmIsStunnedCondition extends SkillCondition implements IEntityCondition {

	public mmIsStunnedCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action","a"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
	}

	@Override
	public boolean check(AbstractEntity target) {
		return target.getBukkitEntity().hasMetadata("mmStunned");
	}

}
