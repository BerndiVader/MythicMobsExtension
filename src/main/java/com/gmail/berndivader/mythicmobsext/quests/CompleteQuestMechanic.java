package com.gmail.berndivader.mythicmobsext.quests;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class CompleteQuestMechanic
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	Quests quests=QuestsSupport.inst().quests();
	String s1;

	public CompleteQuestMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		s1=mlc.getString("quest","").toLowerCase();
		if(!s1.isEmpty()&&s1.charAt(0)=='"') s1=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if (e.isPlayer()) {
			Player p=(Player)e.getBukkitEntity();
			Quester q=quests.getQuester(p.getUniqueId());
			if(q!=null) {
				Iterator<Map.Entry<Quest,Integer>>entries=q.currentQuests.entrySet().iterator();
				while(entries.hasNext()) {
					Quest quest=entries.next().getKey();
					if((quest.getName().toLowerCase().equals(s1))) {
						quest.completeQuest(q);
						return true;
					}
				}
				
			}
		}
		return false;
	}

}
