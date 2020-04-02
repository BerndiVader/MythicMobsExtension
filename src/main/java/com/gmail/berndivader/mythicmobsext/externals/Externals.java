package com.gmail.berndivader.mythicmobsext.externals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class Externals implements Listener {
	public ExternalsLoader loader;
	public static HashMap<String, Class<? extends SkillMechanic>> mechanics;
	public static HashMap<String, Class<? extends SkillCondition>> conditions;
	public static HashMap<String, Class<? extends SkillTargeter>> targeters;
	public static int m, c, t, ml, cl, tl;

	static {
		Main.pluginmanager.getClass().getName();
		mechanics = new HashMap<>();
		conditions = new HashMap<>();
		targeters = new HashMap<>();
		m = c = t = ml = cl = tl = 0;
	}

	public Externals() {
		loader = new ExternalsLoader();
		Main.pluginmanager.registerEvents(this, Main.getPlugin());
	}

	class ExternalsLoader {
		public ExternalsLoader() {
			load();
		}

		public void load() {
			ml = cl = tl = 0;
			File f1;
			if ((f1 = new File(Utils.str_PLUGINPATH, "externals")).exists()) {
				File[] externals = f1.listFiles();
				for (File external : externals) {
					if (!external.isDirectory() && external.getName().endsWith(".jar")) {
						loadExt(external);
					}
				}
			} else {
				f1.mkdir();
			}
			m = mechanics.size();
			c = conditions.size();
			t = targeters.size();
			new BukkitRunnable() {
				@Override
				public void run() {
					Main.logger.info(m + " ext.mechanics loaded.");
					Main.logger.info(c + " ext.conditions loaded.");
					Main.logger.info(t + " ext.targeters loaded.");
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(), 20);
		}

		private void loadExt(File external) {
			try {
				URL[] urls = { new URL("jar:file:" + external.getPath() + "!/") };
				ClassLoader cl = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());
				JarFile jar = new JarFile(external);
				Enumeration<JarEntry> e = jar.entries();
				String cn1 = new String();
				while (e.hasMoreElements()) {
					try {
						JarEntry je = (JarEntry) e.nextElement();
						cn1 = je.getName();
						if (je.isDirectory() || !cn1.endsWith(".class"))
							continue;
						cn1 = cn1.substring(0, cn1.length() - 6).replace("/", ".");
						Class<?> c1 = Class.forName(cn1, true, cl);
						if (c1 == null)
							continue;
						if (SkillMechanic.class.isAssignableFrom(c1)) {
							Class<? extends SkillMechanic> extClass = c1.asSubclass(SkillMechanic.class);
							SkillAnnotation skill = extClass.getAnnotation(SkillAnnotation.class);
							if (skill != null && !mechanics.containsKey(skill.name())) {
								mechanics.put(skill.name(), extClass);
								mechanics.put(skill.name() + "_ext", extClass);
							}
						} else if (SkillCondition.class.isAssignableFrom(c1)) {
							Class<? extends SkillCondition> extClass = c1.asSubclass(SkillCondition.class);
							ConditionAnnotation cond = extClass.getAnnotation(ConditionAnnotation.class);
							if (cond != null && !conditions.containsKey(cond.name())) {
								conditions.put(cond.name(), extClass);
								conditions.put(cond.name() + "_ext", extClass);
							}
						} else if (SkillTargeter.class.isAssignableFrom(c1)) {
							Class<? extends SkillTargeter> extClass = c1.asSubclass(SkillTargeter.class);
							TargeterAnnotation tar = extClass.getAnnotation(TargeterAnnotation.class);
							if (tar != null && !targeters.containsKey(tar.name())) {
								targeters.put(tar.name(), extClass);
								targeters.put(tar.name() + "_ext", extClass);
							}
						}
					} catch (Exception ex) {
						Main.logger.info("Unable to load " + cn1 + " but continue.");
						continue;
					}
				}
				jar.close();
			} catch (IOException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onMythicMobsReload(MythicReloadedEvent e) {
		loader.load();
	}

}
