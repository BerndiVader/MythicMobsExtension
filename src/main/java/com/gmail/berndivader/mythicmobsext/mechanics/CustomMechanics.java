package com.gmail.berndivader.mythicmobsext.mechanics;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.Externals;
import com.gmail.berndivader.mythicmobsext.externals.SkillAnnotation;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class CustomMechanics implements Listener {
	static HashMap<String,Class<? extends SkillMechanic>>internals;
   	static String filename;
	
   	static {
   		filename=Main.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
   	}

	public CustomMechanics() {
		loadInternals();
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin());
	}
	
	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		String mech;
		SkillMechanic skill;
		mech=e.getMechanicName().toLowerCase();
		if(internals.containsKey(mech)) {
			try {
				if (internals.containsKey(mech)) {
					skill=internals.get(mech).getConstructor(String.class,MythicLineConfig.class).newInstance(e.getContainer().getConfigLine(),e.getConfig());
					if (skill!=null) e.register(skill);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
			return;
		}
		switch (mech) {
		case "advaipathfinder":
		case "custompathfinder": {
				skill = new AdvAIPathFinderMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "cure":
			case "removepotion": {
				skill = new RemovePotionEffectMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customrandomskill": 
			case "advrandomskill": {
				skill = new CustomRandomSkillMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "entitygoggle":
			case "entitygoggleat":
			case "entitylookin": {
				skill=new EntityGoogleMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "parsedstance":
		  	case "pstance": {
				skill = new ParsedStanceMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playergoggle":
			case "playergoggleat": {
				skill=new PlayerGoggleMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setspeed":
			case "randomspeed": {
				skill = new SetSpeedMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			}
			default:
			try {
				if (Externals.extMechanics.containsKey(mech)) {
					skill = Externals.extMechanics.get(mech).getConstructor(String.class,MythicLineConfig.class).newInstance(e.getContainer().getConfigLine(),e.getConfig());
					if (skill!=null) {
						e.register(skill);
						break;
					}
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	void loadInternals() {
		internals=new HashMap<>();
		try {
		   	JarInputStream jarFile=new JarInputStream(new FileInputStream(filename));
		   	JarEntry jarEntry;
		    while (true) {
		    	jarEntry=jarFile.getNextJarEntry();
		    	if (jarEntry==null) break;
		    	if (jarEntry.getName().endsWith(".class")) {
		    		String s1=jarEntry.getName();
		    		if (s1.startsWith("com/gmail/berndivader/mythicmobsext/mechanics")) {
						URL[]urls={new URL("jar:file:"+filename+"!/"+s1)};
						ClassLoader cl=URLClassLoader.newInstance(urls,this.getClass().getClassLoader());
						String cn1=s1.substring(0,s1.length()-6).replace("/",".");
						Class<?>c1=Class.forName(cn1,true,cl);
						if (c1!=null&&SkillMechanic.class.isAssignableFrom(c1)) {
							Class<? extends SkillMechanic>iclass=c1.asSubclass(SkillMechanic.class);
							SkillAnnotation skill=iclass.getAnnotation(SkillAnnotation.class);
							if (skill!=null&&!internals.containsKey(skill.name())) {
								internals.put(skill.name(),iclass);
							}
						}
	            	}
	            }
	        }
	        jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
