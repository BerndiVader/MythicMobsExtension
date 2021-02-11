package com.gmail.berndivader.mythicmobsext.compatibility.quests;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestCompleteCondition extends AbstractCustomCondition implements IEntityCondition {
	Quests quests = QuestsSupport.inst().quests();
	List<String> questList;

	public QuestCompleteCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String s1 = mlc.getString("quest", "any").toLowerCase();
		if (!s1.isEmpty() && s1.charAt(0) == '"')
			s1 = SkillString.parseMessageSpecialChars(s1.substring(1, s1.length() - 1));
		questList = Arrays.asList(s1.toLowerCase().split(","));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!entity.isPlayer())
			return false;
		Player p = (Player) entity.getBukkitEntity();
		Quester q = quests.getQuester(p.getUniqueId());
		boolean bl1 = false;
		if (q != null) {
			Iterator<Quest> quests = q.getCompletedQuests().iterator();
			while (quests.hasNext()) {
				String quest = quests.next().getName().toLowerCase();
			if (questList.contains(quest)) {
					return true;
				}
			}
		}
		return bl1;
	}

}
