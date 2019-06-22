package com.gmail.berndivader.mythicmobsext.bossbars.mechanics;

import org.bukkit.boss.BossBar;

import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class
ProgressBossBar 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	String bar_name;
	double value;
	int delta;
	boolean set;
	
	public ProgressBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bar_name=mlc.getString("title","Bar");
		set=mlc.getBoolean("set",false);
		value=mlc.getDouble("value",0.05d);
		delta=value>0d?1:-1;
		value=Math.abs(value);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			if(BossBars.contains(abstract_entity.getUniqueId())) {
				BossBar bar=BossBars.getBar(abstract_entity.getUniqueId(),bar_name);
				if(bar!=null) {
					bar.setProgress(MathUtils.clamp(set?value:bar.getProgress()+value*delta,0d,1d));
				}
			}
		}
		return false;
	}
	
}
