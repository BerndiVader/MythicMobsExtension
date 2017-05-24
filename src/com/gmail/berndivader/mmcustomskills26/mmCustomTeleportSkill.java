package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCustomTeleportSkill extends SkillMechanic
implements
ITargetedEntitySkill,
ITargetedLocationSkill {
	protected String targeter;
    protected boolean useEyeDirection;
    protected double inFrontOffset;

    public mmCustomTeleportSkill(String line, MythicLineConfig mlc) {
        super(line, mlc);
        this.ASYNC_SAFE = false;
        this.targeter = mlc.getString(new String[]{"target","t"},"@self").toLowerCase();
        
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        return true;
    }

	@Override
	public boolean castAtLocation(SkillMetadata var1, AbstractLocation var2) {
		// TODO Auto-generated method stub
		return false;
	}

}
