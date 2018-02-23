package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashSet;

import org.bukkit.Achievement;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@SuppressWarnings("deprecation")
@ExternalAnnotation(name="achievement,achievements,hasachievement,hasachievements",author="BerndiVader")
public class AchievementsCondition
extends
AbstractCustomCondition 
implements
IEntityCondition {
	HashSet<Achievement>achs;

	public AchievementsCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		achs=new HashSet<>();
		String[]arr1=mlc.getString(new String[] {"achievement","ach","achievements"},"").toLowerCase().split(",");
		for(int i1=0;i1<arr1.length;i1++) {
			for (Achievement s1:Achievement.values()) {
				if(s1.name().toLowerCase().equals(arr1[i1])) {
					achs.add(s1);
				}
			}
		}
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()) {
			Player p=(Player)entity.getBukkitEntity();
			for(Achievement ach:achs) {
				if (p.hasAchievement(ach)) return true;
			}
		}
		return false;
	}
}
