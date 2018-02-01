package main.java.com.gmail.berndivader.mythicmobsext.thiefs;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class Thiefs 
implements
Listener {
	public static ThiefHandler thiefhandler;
	
	static {
		thiefhandler=new ThiefHandler();
	}
	
	public Thiefs() {
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}
	
	public void onDisable() {
		if (thiefhandler!=null) thiefhandler.cancelTask();
	}
	
	@EventHandler
	public void onMechanicLoad(MythicMechanicLoadEvent e) {
		String s1;
		SkillMechanic sm;
		s1=e.getMechanicName().toLowerCase();
		switch (s1) {
		case "dropstolenitems":
			sm = new DropStolenItemsMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(sm);
			break;
		case "steal": 
			sm=new StealMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(sm);
			break;
		}
	}
}
