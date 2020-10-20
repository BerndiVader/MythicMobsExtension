package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashSet;
import java.util.Optional;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.targeters.CustomTargeters;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitPlayer;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;
import io.lumine.xikage.mythicmobs.skills.targeters.ConsoleTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.CustomTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import io.lumine.xikage.mythicmobs.skills.targeters.OriginTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.TargetLocationTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.TriggerTargeter;

@ExternalAnnotation(name = "checktargeter,targetcondition", author = "Seyarada")
public class CheckTargeter extends AbstractCustomCondition implements IEntityCondition {
	
	private PlaceholderString stargeter;
	private SkillCondition condition;
	private Boolean forFalse;
	
	public CheckTargeter(String line, MythicLineConfig mlc) {
		super(line, mlc);

		String s = mlc.getString(new String[] { "targeter", "t"}, "@self");
		forFalse = mlc.getBoolean(new String[] { "false", "f"}, false);
		this.stargeter = new PlaceholderString(s);
		
		String conditionString = mlc.getString(new String[] { "condition", "cond", "c"});
		condition = SkillCondition.getCondition(conditionString);
		
	}

	@Override
	public boolean check(AbstractEntity caster) {
		SkillMetadata data = new SkillMetadata(SkillTrigger.API, new GenericCaster(caster), caster);
		
		String targeter = this.stargeter.get(data);
		if (caster.getClass().equals(BukkitEntity.class) || caster.getClass().equals(BukkitPlayer.class)) {
			targeter = this.stargeter.get(data, (AbstractEntity) caster);
		}
		
		HashSet<?> entries = getDestination(targeter, data);
		if(entries.size()==0) return false;
		for(Object i:getDestination(targeter, data)) {
			
			
			if (i instanceof BukkitEntity) {
				data.setEntityTarget((AbstractEntity) i);
				
			}
			else if (i instanceof AbstractLocation) {
				data.setLocationTarget((AbstractLocation) i);
			}
			
			// Returns false if any condition isn't meet
			boolean a = condition.evaluateTargets(data);
			if(forFalse) a=!a;
			if(!a) return a;
		}
		return true;
	}
	
	protected HashSet<?> getDestination(String target, SkillMetadata skilldata) {
		SkillMetadata data = new SkillMetadata(SkillTrigger.API, skilldata.getCaster(), skilldata.getTrigger(),
				skilldata.getOrigin(), null, null, 1.0f);
		Optional<SkillTargeter> maybeTargeter;
		maybeTargeter = Optional.of(Utils.parseSkillTargeter(target));
		if (maybeTargeter.isPresent()) {
			SkillTargeter targeter = maybeTargeter.get();
			if (targeter instanceof CustomTargeter) {
				String s1 = target.substring(1);
				MythicLineConfig mlc = new MythicLineConfig(s1);
				String s2 = s1.contains("{") ? s1.substring(0, s1.indexOf("{")) : s1;
				if ((targeter = CustomTargeters.getCustomTargeter(s2, mlc)) == null)
					targeter = new TriggerTargeter(mlc);
			}
			if (targeter instanceof IEntitySelector) {
				data.setEntityTargets(((IEntitySelector) targeter).getEntities(data));
				((IEntitySelector) targeter).filter(data, false);
				return data.getEntityTargets();
			}
			if (targeter instanceof ILocationSelector) {
				data.setLocationTargets(((ILocationSelector) targeter).getLocations(data));
				((ILocationSelector) targeter).filter(data);
			} else if (targeter instanceof OriginTargeter) {
				data.setLocationTargets(((OriginTargeter) targeter).getLocation(data.getOrigin()));
			} else if (targeter instanceof TargetLocationTargeter) {
				HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
				lTargets.add(data.getTrigger().getLocation());
				data.setLocationTargets(lTargets);
			}
			if (targeter instanceof ConsoleTargeter) {
				data.setEntityTargets(null);
				data.setLocationTargets(null);
			}
			return data.getLocationTargets();
		}
		return null;
	}

}
