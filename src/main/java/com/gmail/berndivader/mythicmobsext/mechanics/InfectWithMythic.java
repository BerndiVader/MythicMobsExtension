package com.gmail.berndivader.mythicmobsext.mechanics;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

@ExternalAnnotation(name="infect",author="BerndiVader")
public 
class 
InfectWithMythic 
extends
SkillMechanic
implements
ITargetedEntitySkill {

	private MythicMob mob_type;
	private int level;

	public InfectWithMythic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		mob_type=Utils.mobmanager.getMythicMob(mlc.getString("mobtype",""));
		level=mlc.getInteger("level",1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(!target.isPlayer()) {
			ActiveMob am=infectEntity(target.getBukkitEntity(),data,mob_type,this.level);
			return am!=null;
		}
		return false;
	}
	
	
	static ActiveMob infectEntity(Entity entity,SkillMetadata data,MythicMob mob_type,int level) {
		ActiveMob am=new ActiveMob(entity.getUniqueId(),BukkitAdapter.adapt(entity),mob_type,level);
		if(am!=null) {
	        if (mob_type.hasFaction()) {
	            am.setFaction(mob_type.getFaction());
	            am.getEntity().getBukkitEntity().setMetadata("Faction",new FixedMetadataValue(Utils.mythicmobs,mob_type.getFaction()));
	        }
	    	Utils.mobmanager.registerActiveMob(am);
			new TriggeredSkill(SkillTrigger.SPAWN,am,data.getCaster().getEntity(),new Pair[0]);
		}
	    return am;
	}
	
}
