package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.InvalidCondition;

public class CastIf extends SkillMechanic 
implements
INoTargetSkill,
ITargetedEntitySkill,
ITargetedLocationSkill {
	
	protected String meetAction,elseAction;
	protected List<String> tConditionLines = new ArrayList<>();
	protected List<String> cConditionLines = new ArrayList<>();
    protected HashSet<SkillCondition> targetConditions = new HashSet<>();
    protected HashSet<SkillCondition> casterConditions = new HashSet<>();
	
	protected boolean outcome;
    protected Optional<Skill> meetSkill = Optional.empty();
    protected Optional<Skill> elseSkill = Optional.empty();

	public CastIf(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		String ms =  mlc.getString(new String[]{"conditions","c"});
		if (ms!=null) {
			ms = ms.substring(1, ms.length()-1);
			ms = SkillString.parseMessageSpecialChars(ms);
			Bukkit.getLogger().info(ms);
			String[]parse=ms.split("\\&\\&");
			if (parse!=null && parse.length>0) {
				Bukkit.getLogger().info("got skill!");
				this.cConditionLines.addAll(Arrays.asList(parse));
			}
			for (String a : this.cConditionLines) {
				Bukkit.getLogger().info(a);
			}
		}
		ms =  mlc.getString(new String[]{"targetconditions","tc"});
		if (ms!=null) {
			ms = ms.substring(1, ms.length()-1);
			ms = SkillString.parseMessageSpecialChars(ms);
			Bukkit.getLogger().info(ms);
			String[]parse=ms.split("\\&\\&");
			if (parse!=null && parse.length>0) this.tConditionLines.addAll(Arrays.asList(parse));
			for (String a : this.tConditionLines) {
				Bukkit.getLogger().info(a);
			}
		}
		
		this.meetAction = mlc.getString(new String[]{"meet"});
		this.elseAction = mlc.getString(new String[]{"else"});
		if (this.meetAction!=null) {
			this.meetSkill = MythicMobs.inst().getSkillManager().getSkill(this.meetAction);
		}
		if (this.elseAction!=null) {
			this.elseSkill = MythicMobs.inst().getSkillManager().getSkill(this.elseAction);
		}
		if (this.cConditionLines!=null 
				&& !this.cConditionLines.isEmpty()) {
			this.casterConditions = this.getConditions(this.cConditionLines);
		}
		if (this.tConditionLines!=null
				&& !this.tConditionLines.isEmpty()) {
			this.targetConditions = this.getConditions(this.tConditionLines);
		}
	}
	
	@Override
	public boolean cast(SkillMetadata data) {
		if (this.handleConditions(data)) {
			if (this.meetSkill.isPresent()
					&& this.meetSkill.get().isUsable(data)) {
				this.meetSkill.get().execute(data);
			}
		} else {
			if (this.elseSkill.isPresent() 
					&& this.elseSkill.get().isUsable(data)) {
				this.elseSkill.get().execute(data);
			}
		}
		return true;
	}
	
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation location) {
		SkillMetadata sdata = data.deepClone();
		HashSet<AbstractLocation>targets = new HashSet<>();
		targets.add(location);
		sdata.setLocationTargets(targets);
		return this.cast(sdata);
	}
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		SkillMetadata sdata = data.deepClone();
		HashSet<AbstractEntity>targets = new HashSet<>();
		targets.add(entity);
		sdata.setEntityTargets(targets);
		return this.cast(sdata);
	}
	
	private boolean handleConditions(SkillMetadata data) {
		boolean cmeet = true;
		if (!this.casterConditions.isEmpty()) {
			cmeet = this.checkConditions(data, this.casterConditions, false);
		} 
		if (!this.targetConditions.isEmpty() && cmeet) {
			cmeet = this.checkConditions(data, this.targetConditions, true);
		}
		return cmeet;
	}
	
	private boolean checkConditions(SkillMetadata data, HashSet<SkillCondition>conditions, boolean isTarget) {
        for (SkillCondition condition : conditions) {
        	if (isTarget) {
                if (!condition.evaluateTargets(data)) return false;
        	} else {
            	if (!condition.evaluateCaster(data)) return false;
        	}
        }
        return true;
	}
	
	private HashSet<SkillCondition>getConditions(List<String>conditionList) {
		HashSet<SkillCondition>conditions = new HashSet<SkillCondition>();
        Iterator<String> it = conditionList.iterator();
        while (it.hasNext()) {
            SkillCondition sc;
            String s = (String)it.next();
            if (s.startsWith(" ")) s = s.substring(1);
            if ((sc = SkillCondition.getCondition(s)) instanceof InvalidCondition) continue;
            conditions.add(sc);
        }
		return conditions;
	}
}
