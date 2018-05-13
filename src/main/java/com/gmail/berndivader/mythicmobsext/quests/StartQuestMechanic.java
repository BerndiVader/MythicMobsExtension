package com.gmail.berndivader.mythicmobsext.quests;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class StartQuestMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	Quests quests=QuestsSupport.inst().quests();
	String s1;
	boolean bl1;

	public StartQuestMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=false;
		s1=mlc.getString("quest","").toLowerCase();
		bl1=mlc.getBoolean("bool",false);
		if(!s1.isEmpty()&&s1.charAt(0)=='"') s1=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if(e.isPlayer()) {
			Quest quest=null;
			if ((quest=quests.getQuest(s1))!=null) {
				Quester quester=quests.getQuester(e.getUniqueId());
				if (quester!=null) {
					quester.takeQuest(quest,true);
					return true;
				}
			} else {
				Main.logger.warning("Quest "+s1+" does not exists!");
			}
		}
		return false;
	}

}
