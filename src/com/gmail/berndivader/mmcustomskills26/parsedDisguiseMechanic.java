package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.compatibility.CompatibilityManager;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class parsedDisguiseMechanic extends SkillMechanic
implements
ITargetedEntitySkill {
    private String disguise;
    public parsedDisguiseMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.disguise=mlc.getString(new String[]{"disguise","d"},"Notch");
        this.ASYNC_SAFE=false;
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        if (CompatibilityManager.LibsDisguises != null) {
            String d=SkillString.unparseMessageSpecialChars(this.disguise);
            d=SkillString.parseMobVariables(d,data.getCaster(),target,data.getTrigger());
            CompatibilityManager.LibsDisguises.setDisguise((ActiveMob)data.getCaster(), d);
            return true;
        }
        return false;
    }
}
