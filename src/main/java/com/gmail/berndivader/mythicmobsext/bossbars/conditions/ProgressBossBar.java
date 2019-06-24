package com.gmail.berndivader.mythicmobsext.bossbars.conditions;

import org.bukkit.boss.BossBar;

import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public 
class
ProgressBossBar 
extends 
AbstractCustomCondition 
implements 
IEntityCondition
{
	RangedDouble range;
	String title;

	public ProgressBossBar(String line, MythicLineConfig mlc) {
		super(line, mlc);
		range=new RangedDouble(mlc.getString(new String[] {"range","r","a","amount"},">0"));
		title=mlc.getString("title","Bar");
	}

	@Override
	public boolean check(AbstractEntity abstract_entity) {
		if (abstract_entity.isPlayer()&&BossBars.contains(abstract_entity.getUniqueId())) {
			BossBar bar=BossBars.getBar(abstract_entity.getUniqueId(),title);
			if(bar!=null) return range.equals(bar.getProgress());
		}
		return false;
	}
}
