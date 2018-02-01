package main.java.com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;

import main.java.com.gmail.berndivader.mythicmobsext.jboolexpr.BooleanExpression;
import main.java.com.gmail.berndivader.mythicmobsext.jboolexpr.MalformedBooleanException;
import main.java.com.gmail.berndivader.mythicmobsext.Main;
import main.java.com.gmail.berndivader.mythicmobsext.targeters.CustomTargeters;
import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.conditions.InvalidCondition;
import io.lumine.xikage.mythicmobs.skills.targeters.CustomTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import io.lumine.xikage.mythicmobs.skills.targeters.MTTrigger;

public class CastIfMechanic
extends
SkillMechanic
implements
INoTargetSkill,
ITargetedEntitySkill,
ITargetedLocationSkill {

	private String meetAction;
	private String elseAction;
	private String cConditionLine;
	private String tConditionLine;
	private boolean breakOnMeet;
	private boolean breakOnElse;
	private boolean RTskill;
	private HashMap<Integer, String> tConditionLines = new HashMap<>();
	private HashMap<Integer, String> cConditionLines = new HashMap<>();
	private HashMap<Integer, SkillCondition> targetConditions = new HashMap<>();
	private HashMap<Integer, SkillCondition> casterConditions = new HashMap<>();
	private Optional<Skill> meetSkill = Optional.empty();
	private Optional<Skill> elseSkill = Optional.empty();
	private Optional<String> meetTargeter = Optional.empty();
	private Optional<String> elseTargeter = Optional.empty();

	public CastIfMechanic(String skill, MythicLineConfig mlc) {
		super(skill,mlc);
		this.ASYNC_SAFE=false;
		this.breakOnMeet = mlc.getBoolean(new String[] { "breakonmeet", "breakmeet" }, false);
		this.breakOnElse = mlc.getBoolean(new String[] { "breakonelse", "breakelse" }, false);
		this.RTskill = mlc.getBoolean(new String [] { "realtime", "rtskill", "rt"}, false);
		String ms = mlc.getString(new String[] { "conditions", "c" });
		this.parseConditionLines(ms, false);
		ms = mlc.getString(new String[] { "targetconditions", "tc" });
		this.parseConditionLines(ms, true);
		this.meetAction = mlc.getString(new String[] { "meet", "m" });
		this.elseAction = mlc.getString(new String[] { "else", "e" });
		this.meetTargeter = Optional.ofNullable(mlc.getString(new String[] { "meettargeter", "mt" }));
		this.elseTargeter = Optional.ofNullable(mlc.getString(new String[] { "elsetargeter", "et" }));
		if (!this.RTskill) {
			if (this.meetAction!=null) this.meetSkill = Utils.mythicmobs.getSkillManager().getSkill(this.meetAction);
			if (this.elseAction!=null) this.elseSkill = Utils.mythicmobs.getSkillManager().getSkill(this.elseAction);
		}
		if (this.cConditionLines!=null && !this.cConditionLines.isEmpty()) this.casterConditions = this.getConditions(this.cConditionLines);
		if (this.tConditionLines != null && !this.tConditionLines.isEmpty()) this.targetConditions = this.getConditions(this.tConditionLines);
		if (this.meetTargeter.isPresent()) {
			String mt = this.meetTargeter.get();
			mt=mt.substring(1, mt.length()-1);
			mt=SkillString.parseMessageSpecialChars(mt);
			this.meetTargeter = Optional.of(mt);
		}
		if (this.elseTargeter.isPresent()) {
			String mt = this.elseTargeter.get();
			mt=mt.substring(1, mt.length()-1);
			mt=SkillString.parseMessageSpecialChars(mt);
			this.elseTargeter = Optional.of(mt);
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		SkillMetadata sdata=data.deepClone();
		Optional<Skill> mSkill=Optional.empty();
		Optional<Skill> eSkill=Optional.empty();
		if (this.RTskill) {
			AbstractEntity at=null;
			AbstractLocation al=null;
			if (sdata.getEntityTargets()!=null&&sdata.getEntityTargets().size()>0) at=sdata.getEntityTargets().iterator().next();
			if (sdata.getLocationTargets()!=null&&sdata.getLocationTargets().size()>0) al=sdata.getLocationTargets().iterator().next();
			if (this.meetAction!=null) {
				mSkill=Utils.mythicmobs.getSkillManager().getSkill(Utils.parseMobVariables(SkillString.parseMessageSpecialChars(this.meetAction),sdata,sdata.getCaster().getEntity(),at,al));
			}
			if (this.elseAction!=null) {
				eSkill=Utils.mythicmobs.getSkillManager().getSkill(Utils.parseMobVariables(SkillString.parseMessageSpecialChars(this.elseAction),sdata,sdata.getCaster().getEntity(),at,al));
			}
		} else {
			mSkill=this.meetSkill;
			eSkill=this.elseSkill;
		}
		if (this.handleConditions(sdata)) {
			if (mSkill.isPresent()&&mSkill.get().isUsable(sdata)) {
				if(this.meetTargeter.isPresent())renewTargets(this.meetTargeter.get(),sdata);
				mSkill.get().execute(sdata);
			}
			if (this.breakOnMeet) {
			}
		} else {
			if (eSkill.isPresent()&&eSkill.get().isUsable(sdata)) {
				if(this.elseTargeter.isPresent())renewTargets(this.elseTargeter.get(),sdata);
				eSkill.get().execute(sdata);
			}
			if (this.breakOnElse) {
			}
		}
		return true;
	}

	private static void renewTargets(String ts, SkillMetadata data) {
		Optional<SkillTargeter> maybeTargeter = Optional.of(AbstractSkill.parseSkillTargeter(ts));
		if (maybeTargeter.isPresent()) {
			SkillTargeter st=maybeTargeter.get();
            if (st instanceof CustomTargeter) {
                String s1=ts.substring(1);
                MythicLineConfig mlc=new MythicLineConfig(s1);
                String s2=s1.contains("{")?s1.substring(0,s1.indexOf("{")):s1;
            	if ((st=CustomTargeters.getCustomTargeter(s2,mlc))==null) st=new MTTrigger(mlc);
            }
			if (st instanceof IEntitySelector) {
				if (data.getEntityTargets()==null) data.setEntityTargets(new HashSet<>());
	            ((IEntitySelector)st).filter(data, false);
	            data.setEntityTargets(((IEntitySelector)st).getEntities(data));
			} else if (st instanceof ILocationSelector) {
				if (data.getLocationTargets()==null) data.setLocationTargets(new HashSet<>());
	            ((ILocationSelector)st).filter(data);
	            data.setLocationTargets(((ILocationSelector)st).getLocations(data));
			}
		}
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
			SkillMetadata sdata;
			sdata = data.deepClone();
			SkillCondition condition = conditions.get(a);
			if (isTarget) {
				cline = cline.replaceFirst(Pattern.quote(this.tConditionLines.get(a)),
						Boolean.toString(condition.evaluateTargets(sdata)));
			} else {
				cline = cline.replaceFirst(Pattern.quote(this.cConditionLines.get(a)),
						Boolean.toString(condition.evaluateCaster(sdata)));
			}
		}
		BooleanExpression be;
		try {
			be = BooleanExpression.readLR(cline);
		} catch (MalformedBooleanException e) {
			Main.logger.warning("There was a problem parsing BoolExpr: "+this.getConfigLine());
			return false;
		}
		return be.booleanValue();
	}

	private HashMap<Integer, SkillCondition> getConditions(HashMap<Integer, String> conditionList) {
		HashMap<Integer, SkillCondition> conditions = new HashMap<Integer, SkillCondition>();
		for (int a = 0; a < conditionList.size(); a++) {
			SkillCondition sc;
			String s = conditionList.get(a);
			if (s.startsWith(" ")) s=s.substring(1);
			if ((sc=SkillCondition.getCondition(s)) instanceof InvalidCondition) continue;
			conditions.put(a,sc);
		}
		return conditions;
	}

	private void parseConditionLines(String ms, boolean istarget) {
		if (ms != null && (ms.startsWith("\"") && ms.endsWith("\""))) {
			ms = ms.substring(1, ms.length() - 1);
			ms = SkillString.parseMessageSpecialChars(ms);
			if (istarget) {
				this.tConditionLine=ms;
			} else {
				this.cConditionLine=ms;
			}
			ms = ms.replaceAll("\\(", "").replaceAll("\\)", "");
			String[] parse = ms.split("\\&\\&|\\|\\|");
			if (parse.length>0) {
				for (int i=0;i<parse.length;i++) {
					String p=parse[i];
					if (istarget) {
						this.tConditionLines.put(i,p);
					} else {
						this.cConditionLines.put(i,p);
					}
				}
			}
		}
	}
}