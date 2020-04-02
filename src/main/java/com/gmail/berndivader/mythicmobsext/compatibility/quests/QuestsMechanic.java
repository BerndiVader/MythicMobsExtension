package com.gmail.berndivader.mythicmobsext.compatibility.quests;

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
import me.blackvein.quests.exceptions.InvalidStageException;

public class QuestsMechanic extends SkillMechanic implements ITargetedEntitySkill {
	Quests quests = QuestsSupport.inst().quests();
	String s1, s2;
	int i1;

	public QuestsMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;
		s1 = mlc.getString("quest", "").toLowerCase();
		i1 = mlc.getInteger("stage", 0);
		if (!s1.isEmpty() && s1.charAt(0) == '"')
			s1 = SkillString.parseMessageSpecialChars(s1.substring(1, s1.length() - 1));
		s2 = skill.toUpperCase().split("QUEST\\{")[0];
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if (e.isPlayer()) {
			Player p = (Player) e.getBukkitEntity();
			Quester q = quests.getQuester(p.getUniqueId());
			Quest quest = QuestsSupport.getQuestFromCurrent(q, s1);
			switch (s2) {
			case "COMPLETE":
				if (quest != null)
					quest.completeQuest(q);
				break;
			case "TAKE":
				if (quest == null && (quest = quests.getQuest(s1)) != null)
					q.takeQuest(quest, true);
				break;
			case "FAIL":
				if (quest != null)
					quest.failQuest(q);
				break;
			case "NEXTSTAGE":
				if (quest != null) {
					quest.nextStage(q, false);
					quest.updateCompass(q, q.getCurrentStage(quest));
				}
				break;
			case "SETSTAGE":
				if (quest != null) {
					try {
						quest.setStage(q, i1);
					} catch (InvalidStageException e1) {
						e1.printStackTrace();
					}
					quest.updateCompass(q, q.getCurrentStage(quest));
				}
				break;
			}
		}
		return true;
	}

}
