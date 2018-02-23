package com.gmail.berndivader.mythicmobsext.targeters;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.Internals;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class CustomTargeters
implements
Listener {
	public CustomTargeters() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	@EventHandler
	public void onMythicTargetersLoad(MythicTargeterLoadEvent e) {
		SkillTargeter st;
		if ((st=getCustomTargeter(e.getTargeterName().toLowerCase(),e.getConfig()))!=null) {
			Internals.tl++;
			e.register(st);
		}
	}

	public static SkillTargeter getCustomTargeter(String s1,MythicLineConfig mlc) {
		SkillTargeter t=null;
		Class<? extends SkillTargeter>cl1=null;
		if (Internals.targeters.containsKey(s1)) {
			cl1=Main.getPlugin().internals.loader.loadT(s1);
		} else if (Externals.targeters.containsKey(s1)) {
			cl1=Externals.targeters.get(s1);
		}
		if (cl1!=null) {
			try {
				t=cl1.getConstructor(MythicLineConfig.class).newInstance(mlc);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
		return t;
	}
}
