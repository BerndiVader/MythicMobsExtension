package main.java.com.gmail.berndivader.mythicmobsext.conditions.mobarena;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class MobArenaConditions implements Listener {

	public MobArenaConditions() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		String conditionName = e.getConditionName().toLowerCase();
		if (conditionName.equals("inmobarena")) {
			SkillCondition c = new InMobArenaCondition(e.getConfig().getLine(), e.getConfig());
			e.register(c);
		}
	}
}