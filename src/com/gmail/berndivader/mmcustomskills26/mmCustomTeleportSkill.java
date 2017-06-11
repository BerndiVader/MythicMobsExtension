package com.gmail.berndivader.mmcustomskills26;

import java.util.HashSet;
import java.util.Optional;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.ConsoleTargeter;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import io.lumine.xikage.mythicmobs.skills.targeters.MTOrigin;
import io.lumine.xikage.mythicmobs.skills.targeters.MTTriggerLocation;

public class mmCustomTeleportSkill extends SkillMechanic
implements
ITargetedEntitySkill
{
	protected String destination;
	protected boolean inFrontOf;

    public mmCustomTeleportSkill(String line, MythicLineConfig mlc) {
        super(line, mlc);
        this.ASYNC_SAFE = false;
        this.destination = mlc.getString(new String[]{"destination","dest","d"},"@self").toLowerCase();
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        return true;
    }
    
	@SuppressWarnings("unchecked")
	private static HashSet<Object> getDestination(String destination, SkillMetadata data) {
    	String target;
    	Optional<SkillTargeter>maybeTargeter;
    	target = !destination.startsWith("@")?"@"+destination:destination;
    	maybeTargeter = Optional.of(AbstractSkill.parseSkillTargeter(target));
	    if (maybeTargeter.isPresent()) {
            SkillTargeter targeter = maybeTargeter.get();
            if (targeter instanceof IEntitySelector) {
                data.setEntityTargets(((IEntitySelector)targeter).getEntities(data));
                ((IEntitySelector)targeter).filter(data, false);
                return (HashSet<Object>) ((Object)data.getEntityTargets());
            }
            if (targeter instanceof ILocationSelector) {
                data.setLocationTargets(((ILocationSelector)targeter).getLocations(data));
                ((ILocationSelector)targeter).filter(data);
            } else if (targeter instanceof MTOrigin) {
                data.setLocationTargets(((MTOrigin)targeter).getLocation(data.getOrigin()));
            } else if (targeter instanceof MTTriggerLocation) {
                HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
                lTargets.add(data.getTrigger().getLocation());
                data.setLocationTargets(lTargets);
            }
            if (targeter instanceof ConsoleTargeter) {
                data.setEntityTargets(null);
                data.setLocationTargets(null);
            }
            return (HashSet<Object>) ((Object)data.getLocationTargets());
        }
	    return null;
    }
    
}
