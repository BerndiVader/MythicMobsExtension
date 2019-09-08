package com.gmail.berndivader.mythicmobsext.bossbars.mechanics;

import org.bukkit.boss.BossBar;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public 
class
ProgressBossBar 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	PlaceholderString title,expr;
	boolean set;
	
	public ProgressBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		title=mlc.getPlaceholderString("title","Bar");
		set=mlc.getBoolean("set",false);
		expr=mlc.getPlaceholderString("value","0.05d");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			double value=0d;
			String parsed_expr=expr.get(data,abstract_entity);
			try {
				value=Double.parseDouble(parsed_expr);
			}catch (Exception e) {
				Main.logger.info(parsed_expr+" is not valid for double in "+this.line);
				value=0d;
			}
			int delta=value>0?1:-1;
			value=Math.abs(value);
			if(BossBars.contains(abstract_entity.getUniqueId())) {
				BossBar bar=BossBars.getBar(abstract_entity.getUniqueId(),title.get(data,abstract_entity));
				if(bar!=null) {
					bar.setProgress(MathUtils.clamp(set?value:bar.getProgress()+value*delta,0d,1d));
				}
			}
		}
		return false;
	}
	
}
