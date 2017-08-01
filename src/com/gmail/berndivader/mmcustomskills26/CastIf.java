package com.gmail.berndivader.mmcustomskills26;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;

import com.gmail.berndivader.jboolexpr.BooleanExpression;
import com.gmail.berndivader.jboolexpr.MalformedBooleanException;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.InvalidCondition;

public class CastIf extends SkillMechanic implements INoTargetSkill, ITargetedEntitySkill, ITargetedLocationSkill {

	protected MythicMobs mythicmobs;
	protected SkillManager skillmanager;
	protected String meetAction, elseAction;
	protected String cConditionLine, tConditionLine;
	protected HashMap<Integer, String> tConditionLines = new HashMap<>();
	protected HashMap<Integer, String> cConditionLines = new HashMap<>();
	protected HashMap<Integer, SkillCondition> targetConditions = new HashMap<>();
	protected HashMap<Integer, SkillCondition> casterConditions = new HashMap<>();
	protected Optional<Skill> meetSkill = Optional.empty();
	protected Optional<Skill> elseSkill = Optional.empty();

	public CastIf(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.mythicmobs = Main.getPlugin().getMythicMobs();
		this.skillmanager = this.mythicmobs.getSkillManager();
		String ms = mlc.getString(new String[] { "conditions", "c" });
		this.parseConditionLines(ms, false);
		ms = mlc.getString(new String[] { "targetconditions", "tc" });
		this.parseConditionLines(ms, true);
		this.meetAction = mlc.getString(new String[] { "meet" });
		this.elseAction = mlc.getString(new String[] { "else" });
		if (this.meetAction != null) {
			this.meetSkill = this.skillmanager.getSkill(this.meetAction);
		}
		if (this.elseAction != null) {
			this.elseSkill = this.skillmanager.getSkill(this.elseAction);
		}
		if (this.cConditionLines != null && !this.cConditionLines.isEmpty()) {
			this.casterConditions = this.getConditions(this.cConditionLines);
		}
		if (this.tConditionLines != null && !this.tConditionLines.isEmpty()) {
			this.targetConditions = this.getConditions(this.tConditionLines);
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (this.handleConditions(data)) {
			if (this.meetSkill.isPresent() && this.meetSkill.get().isUsable(data)) {
				this.meetSkill.get().execute(data);
			}
		} else {
			if (this.elseSkill.isPresent() && this.elseSkill.get().isUsable(data)) {
				this.elseSkill.get().execute(data);
			}
		}
		return true;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation location) {
		SkillMetadata sdata = data.deepClone();
		HashSet<AbstractLocation> targets = new HashSet<>();
		targets.add(location);
		sdata.setLocationTargets(targets);
		return this.cast(sdata);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity entity) {
		SkillMetadata sdata = data.deepClone();
		HashSet<AbstractEntity> targets = new HashSet<>();
		targets.add(entity);
		sdata.setEntityTargets(targets);
		return this.cast(sdata);
	}

	private boolean handleConditions(SkillMetadata data) {
		boolean meet = true;
		if (!this.casterConditions.isEmpty()) {
			meet = this.checkConditions(data, this.casterConditions, false);
		}
		if (!this.targetConditions.isEmpty() && meet) {
			meet = this.checkConditions(data, this.targetConditions, true);
		}
		return meet;
	}

	private boolean checkConditions(SkillMetadata data, HashMap<Integer, SkillCondition> conditions, boolean isTarget) {
		String cline = isTarget ? this.tConditionLine : this.cConditionLine;
		for (int a = 0; a < conditions.size(); a++) {
			SkillMetadata sdata = null;
			try {
				sdata = data.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			SkillCondition condition = conditions.get(a);
			if (isTarget) {
				cline = cline.replaceFirst(Pattern.quote(this.tConditionLines.get(a)),
						Boolean.toString(condition.evaluateTargets(sdata)));
			} else {
				cline = cline.replaceFirst(Pattern.quote(this.cConditionLines.get(a)),
						Boolean.toString(condition.evaluateCaster(sdata)));
			}
		}
		BooleanExpression be = null;
		try {
			be = BooleanExpression.readLR(cline);
		} catch (MalformedBooleanException e) {
			e.printStackTrace();
		}
		return be.booleanValue();
	}

	private HashMap<Integer, SkillCondition> getConditions(HashMap<Integer, String> conditionList) {
		HashMap<Integer, SkillCondition> conditions = new HashMap<Integer, SkillCondition>();
		for (int a = 0; a < conditionList.size(); a++) {
			SkillCondition sc;
			String s = conditionList.get(a);
			if (s.startsWith(" "))
				s = s.substring(1);
			if ((sc = SkillCondition.getCondition(s)) instanceof InvalidCondition)
				continue;
			conditions.put(a, sc);
		}
		return conditions;
	}

	private void parseConditionLines(String ms, boolean istarget) {
		if (ms != null && (ms.startsWith("\"") && ms.endsWith("\""))) {
			ms = ms.substring(1, ms.length() - 1);
			ms = SkillString.parseMessageSpecialChars(ms);
			if (istarget) {
				this.tConditionLine = ms;
			} else {
				this.cConditionLine = ms;
			}
			ms = ms.replaceAll("\\(", "").replaceAll("\\)", "");
			String[] parse = ms.split("\\&\\&|\\|\\|");
			if (parse != null && parse.length > 0) {
				for (int a = 0; a < Arrays.asList(parse).size(); a++) {
					String p = Arrays.asList(parse).get(a);
					if (istarget) {
						this.tConditionLines.put(a, p);
					} else {
						this.cConditionLines.put(a, p);
					}
				}
			}
		}
	}
}
