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

public class QuestsSupport implements Listener {
	static String pluginName = "Quests";
	private static QuestsSupport core;
	private Plugin plugin;
	private static Optional<Quests> quests;

	static {
		quests = Optional.ofNullable((Quests) Bukkit.getServer().getPluginManager().getPlugin(pluginName));
	}

	public QuestsSupport(Plugin plugin) {
		core = this;
		this.plugin = plugin;
		Main.pluginmanager.registerEvents(this, plugin);
		Main.logger.info("using " + pluginName);

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
		String s1 = e.getConditionName().toLowerCase();
		switch (s1) {
		case "activequest":
		case "activequest_ext": {
			e.register(new QuestRunningCondition(e.getConfig().getLine(), e.getConfig()));
		}
		case "completedquest":
		case "completedquest_ext": {
			e.register(new QuestCompleteCondition(e.getConfig().getLine(), e.getConfig()));
		}
		case "testrequirement":
		case "testrequirement_ext": {
			e.register(new TestRequirementCondition(e.getConfig().getLine(), e.getConfig()));
		}
		}
	}

	@EventHandler
	public void onQuestMechanicsLoad(MythicMechanicLoadEvent e) {
		String s1 = e.getMechanicName().toLowerCase();
		switch (s1) {
		case "completequest":
		case "takequest":
		case "failquest":
		case "completequest_ext":
		case "takequest_ext":
		case "failquest_ext": {
			e.register(new QuestsMechanic(e.getContainer().getConfigLine(), e.getConfig()));
		}
		}
	}

	static Quest getQuestFromCurrent(Quester quester, String questName) {
		Iterator<Map.Entry<Quest, Integer>> entries = quester.getCurrentQuests().entrySet().iterator();
		while (entries.hasNext()) {
			Quest quest = entries.next().getKey();
			if ((quest.getName().toLowerCase().equals(questName)))
				return quest;
		}
		return null;
	}

}
