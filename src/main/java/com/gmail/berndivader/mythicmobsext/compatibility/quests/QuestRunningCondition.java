package com.gmail.berndivader.mythicmobsext.compatibility.quests;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.blackvein.quests.Stage;

public class QuestRunningCondition 
extends
AbstractCustomCondition
implements
IEntityCondition {
	Quests quests=QuestsSupport.inst().quests();
	RangedDouble rd;
	String[]arr1;
	
	public QuestRunningCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String s1=mlc.getString("quest","ANY").toLowerCase();
		rd=new RangedDouble(mlc.getString("stage",">-1"));
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
			Iterator<Map.Entry<Quest,Integer>>entries=q.currentQuests.entrySet().iterator();
			while(entries.hasNext()&&!bl1) {
				Quest quest=entries.next().getKey();
				for(int i1=0;i1<arr1.length;i1++) {
					String s1=arr1[i1];
					if((s1.equals("any")||quest.getName().toLowerCase().equals(s1))&&rd.equals(getStageInt(quest,q.getCurrentStage(quest)))) {
						bl1=true;
						break;
					}
				}
			}
		}
		return bl1;
	}
	
	static int getStageInt(Quest quest,Stage stage) {
		Iterator<Stage>stages=quest.orderedStages.iterator();
		int i1=0;
		while(stages.hasNext()) {
			Stage ss=stages.next();
			if (ss.equals(stage)) return i1;
			i1++;
		}
		return -1;
	}

}
