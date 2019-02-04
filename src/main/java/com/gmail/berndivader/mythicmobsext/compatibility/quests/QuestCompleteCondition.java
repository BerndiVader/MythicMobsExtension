package com.gmail.berndivader.mythicmobsext.compatibility.quests;

import java.util.Iterator;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestCompleteCondition 
extends 
AbstractCustomCondition
implements
IEntityCondition {
	Quests quests=QuestsSupport.inst().quests();
	String[]arr1;

	public QuestCompleteCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String s1=mlc.getString("quest","any").toLowerCase();
		if(!s1.isEmpty()&&s1.charAt(0)=='"') s1=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
		arr1=s1.split(",");
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (!e.isPlayer()) return false;
		Player p=(Player)e.getBukkitEntity();
		Quester q=quests.getQuester(p.getUniqueId());
		boolean bl1=false;
		if(q!=null) {
			Iterator<String>quests=q.getCompletedQuests().iterator();
			while(quests.hasNext()&&!bl1) {
				String quest=quests.next().toLowerCase();
				for(int i1=0;i1<arr1.length;i1++) {
					String s1=arr1[i1];
					if((s1.equals("any")||quest.equals(s1))) {
						bl1=true;
						break;
					}
				}
			}
		}
		return bl1;
	}

}
