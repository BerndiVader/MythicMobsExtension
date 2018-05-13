package com.gmail.berndivader.mythicmobsext.quests;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
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
		}
	}
	
	@EventHandler
	public void onQuestMechanicsLoad(MythicMechanicLoadEvent e) {
		String s1=e.getMechanicName().toLowerCase();
		if (s1.equals("completequest")) {
			e.register(new CompleteQuestMechanic(e.getContainer().getConfigLine(),e.getConfig()));
		} else if(s1.equals("takequest")) {
			e.register(new StartQuestMechanic(e.getContainer().getConfigLine(),e.getConfig()));
		}
	}

}
