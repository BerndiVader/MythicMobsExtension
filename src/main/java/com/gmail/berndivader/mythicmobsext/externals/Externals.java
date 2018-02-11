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

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class Externals
implements
Listener {
	ExternalsLoader loader;
	public static HashMap<String,Class<? extends SkillMechanic>>extMechanics;
	public static HashMap<String,Class<? extends AbstractCustomCondition>>extConditions;
	public static HashMap<String,Class<? extends SkillTargeter>>extTargeters;
	
	static {
		extMechanics=new HashMap<>();
		extConditions=new HashMap<>();
		extTargeters=new HashMap<>();
	}
	
	public Externals() {
		loader=new ExternalsLoader();
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
	}
	
	class ExternalsLoader {
		public ExternalsLoader() {
			load();
		}
		
		public void load() {
			File f1;
			if ((f1=new File(Utils.str_PLUGINPATH,"externals")).exists()) {
				File[]externals=f1.listFiles();
				for(File external:externals) {
					if(!external.isDirectory()&&external.getName().endsWith(".jar")) {
						loadExt(external);
					}
				}
				
			} else {
				f1.mkdir();
			}
			
			
		}
		private void loadExt(File external) {
			try {
				URL[]urls= {
						new URL("jar:file:"+external.getPath()+"!/")
				};
				ClassLoader cl=URLClassLoader.newInstance(urls,this.getClass().getClassLoader());
				JarFile jar=new JarFile(external);
				Enumeration<JarEntry>e=jar.entries();
				while(e.hasMoreElements()) {
					JarEntry je=(JarEntry)e.nextElement();
					String cn1=je.getName();
					if(je.isDirectory()||!cn1.endsWith(".class")) continue;
					cn1=cn1.substring(0,cn1.length()-6).replace("/",".");
					Class<?>c1=Class.forName(cn1,true,cl);
					if (c1!=null&&SkillMechanic.class.isAssignableFrom(c1)) {
						Class<? extends SkillMechanic>extClass=c1.asSubclass(SkillMechanic.class);
						SkillAnnotation skill=extClass.getAnnotation(SkillAnnotation.class);
						if (skill!=null&&!extMechanics.containsKey(skill.name())) {
							extMechanics.put(skill.name(),extClass);
						}
					}
				}
				jar.close();
			} catch (IOException | ClassNotFoundException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onMythicMobsReload(MythicReloadedEvent e) {
		loader.load();
	}

}
