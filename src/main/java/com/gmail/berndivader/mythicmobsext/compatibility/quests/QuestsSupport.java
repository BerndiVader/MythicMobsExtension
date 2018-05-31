package com.gmail.berndivader.mythicmobsext.compatibility.quests;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestsSupport 
implements
Listener {
	
	private static QuestsSupport core;
	private Plugin plugin;
	private static Optional<Quests>quests;
	
	static {
		quests=Optional.ofNullable((Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests"));
	}
	
	public QuestsSupport(Plugin plugin) {
		core=this;
		this.plugin=plugin;
		Main.pluginmanager.registerEvents(this,plugin);
	}

	public static QuestsSupport inst() {
		return core;
	}
	
	public Plugin plugin() {
		return this.plugin;
	}
	public static boolean isPresent() {
		return quests.isPresent();
	}
	public Quests quests() {
		return QuestsSupport.quests.get();
	}
	
	@EventHandler
	public void onQuestConditionsLoad(MythicConditionLoadEvent e) {
		String s1=e.getConditionName().toLowerCase();
		if (s1.equals("activequest")) {
			e.register(new QuestRunningCondition(e.getConfig().getLine(),e.getConfig()));
		} else if(s1.equals("completedquest")) {
			e.register(new QuestCompleteCondition(e.getConfig().getLine(),e.getConfig()));
		} else if(s1.equals("testrequirement")) {
			e.register(new TestRequirementCondition(e.getConfig().getLine(),e.getConfig()));
		}
	}
	
	@EventHandler
	public void onQuestMechanicsLoad(MythicMechanicLoadEvent e) {
		String s1=e.getMechanicName().toLowerCase();
		if (s1.equals("completequest")||s1.equals("takequest")||s1.equals("failquest")) {
			e.register(new QuestsMechanic(e.getContainer().getConfigLine(),e.getConfig()));
		}
	}
	
	static Quest getQuestFromCurrent(Quester quester, String questName) {
		Iterator<Map.Entry<Quest,Integer>>entries=quester.currentQuests.entrySet().iterator();
		while(entries.hasNext()) {
			Quest quest=entries.next().getKey();
			if((quest.getName().toLowerCase().equals(questName))) return quest;
		}
		return null;
	}
	

}
