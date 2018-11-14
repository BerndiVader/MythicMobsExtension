package com.gmail.berndivader.mythicmobsext.compatibility.quests;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

public class TestRequirementCondition 
extends
AbstractCustomCondition
implements
IEntityCondition {
	Quests quests=QuestsSupport.inst().quests();
	String s1;
	
	public TestRequirementCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		s1=mlc.getString("quest","").toLowerCase();
		if(!s1.isEmpty()&&s1.charAt(0)=='"') s1=SkillString.parseMessageSpecialChars(s1.substring(1,s1.length()-1));
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (!e.isPlayer()||s1.isEmpty()) return false;
		boolean bl1=false;
		Player p=(Player)e.getBukkitEntity();
		Quest quest=quests.getQuest(s1);
		if (quest!=null) bl1=quest.testRequirements(p);
		return bl1;
	}
}
