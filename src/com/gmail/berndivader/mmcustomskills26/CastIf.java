package com.gmail.berndivader.mmcustomskills26;

import java.util.Optional;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class CastIf extends SkillMechanic 
implements INoTargetSkill {
	
	protected String condition,compare,cast,castinstead;
	protected boolean outcome;
    protected Optional<Skill> castSkill = Optional.empty();
    protected Optional<Skill> castInsteadSkill = Optional.empty();

	public CastIf(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.condition = mlc.getString(new String[]{"condition","c"});
		this.compare = mlc.getString(new String[]{"compare","co"});
		this.cast = mlc.getString(new String[]{"skill","s"});
		this.castinstead = mlc.getString(new String[]{"insteadskill","is"});
		
		if (this.cast!=null) {
			this.castSkill = MythicMobs.inst().getSkillManager().getSkill(this.cast);
		}
		if (this.castinstead!=null) {
			this.castInsteadSkill = MythicMobs.inst().getSkillManager().getSkill(this.castinstead);
		}
		
	}

	@Override
	public boolean cast(SkillMetadata data) {
		Bukkit.getLogger().info(data.getCallingEvent().getClass().getName());
		return false;
	}
	
}
