package com.gmail.berndivader.mythicmobsext.externals;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class Internals
implements
Listener {
	public InternalsLoader loader;
	public static HashMap<String,String>mechanics;
	public static HashMap<String,String>conditions;
	public static HashMap<String,String>targeters;
   	public static String filename;
   	public static ClassLoader classLoader;
   	public static int m,c,t,ml,cl,tl;
   	
   	static {
   		try {
			filename=URLDecoder.decode(Main.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			try {
				filename=URLDecoder.decode(Main.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),StandardCharsets.ISO_8859_1.toString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
   		mechanics=new HashMap<>();
   		conditions=new HashMap<>();
   		targeters=new HashMap<>();
   		classLoader=Internals.class.getClassLoader();
   		m=c=t=ml=cl=tl=0;
   	}
   	
	public Internals() {
		loader=new InternalsLoader();
		Main.pluginmanager.registerEvents(this,Main.getPlugin());
	}
	
	public class InternalsLoader {
		public InternalsLoader() {
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		SimpleEntry<ExternalAnnotation,Class<?>> getClazz(String cn1) throws ClassNotFoundException{
			Class<?>c1=null;
			try {
				c1=Class.forName(cn1,false,classLoader);
			} catch (Throwable e) {
				return new SimpleEntry<>(null,null);
			}
			return new SimpleEntry<>(c1.getAnnotation(ExternalAnnotation.class),c1);
		}
		
		void load() throws Exception {
	   		ml=cl=tl=0;
			try {
			   	JarInputStream jarFile=new JarInputStream(new FileInputStream(filename));
			   	JarEntry jarEntry;
			    while (true) {
			    	jarEntry=jarFile.getNextJarEntry();
			    	if (jarEntry==null) break;
			    	if (jarEntry.getName().endsWith(".class")) {
			    		String s1=jarEntry.getName();
						String cn1=s1.substring(0,s1.length()-6).replace("/",".");
						try {
				    		if (s1.startsWith("com/gmail/berndivader/mythicmobsext/mechanics")) {
				    			SimpleEntry<ExternalAnnotation,Class<?>>entry=getClazz(cn1);
				    			Class<?>c1=entry.getValue();
				    			ExternalAnnotation skill=entry.getKey();
								if (skill!=null&&c1!=null&&SkillMechanic.class.isAssignableFrom(c1)) {
									String[]arr1=skill.name().split(",");
									for(int i1=0;i1<arr1.length;i1++) {
										if (!mechanics.containsKey(arr1[i1])) {
											mechanics.put(arr1[i1],cn1);
										}
									}
								}
			            	} else if (s1.startsWith("com/gmail/berndivader/mythicmobsext/conditions")) {
				    			SimpleEntry<ExternalAnnotation,Class<?>>entry=getClazz(cn1);
				    			Class<?>c1=entry.getValue();
				    			ExternalAnnotation anno=entry.getKey();
								if (anno!=null&&c1!=null&&SkillCondition.class.isAssignableFrom(c1)) {
									String[]arr1=anno.name().split(",");
									for(int i1=0;i1<arr1.length;i1++) {
										if (!conditions.containsKey(arr1[i1])) {
											conditions.put(arr1[i1],cn1);
										}
									}
								}
			            	} else if (s1.startsWith("com/gmail/berndivader/mythicmobsext/targeters")) {
				    			SimpleEntry<ExternalAnnotation,Class<?>>entry=getClazz(cn1);
				    			Class<?>c1=entry.getValue();
				    			ExternalAnnotation anno=entry.getKey();
			            		if (anno!=null&c1!=null&&SkillTargeter.class.isAssignableFrom(c1)) {
		            				String[]arr1=anno.name().split(",");
		            				for(int i1=0;i1<arr1.length;i1++) {
		            					if (!targeters.containsKey(arr1[i1])) {
		            						targeters.put(arr1[i1],cn1);
		            					}
		            				}
			            		}
			            	}
	            		} catch (Throwable ex) {
	            			Main.logger.info("Unable to load "+cn1+" but continue.");
	            			continue;
	            		}
			    	}
			    }
		        jarFile.close();
		    } catch (Exception e) {
				e.printStackTrace();
			}
			m=mechanics.size();
			c=conditions.size();
			t=targeters.size();
			new BukkitRunnable() {
				@Override
				public void run() {
					Main.logger.info(m+" mechanics loaded.");
					Main.logger.info(c+" conditions loaded.");
					Main.logger.info(t+" targeters loaded.");
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(),15);
		}
		
		public Class<? extends SkillMechanic> loadM(String s1) {
			try {
				Class<?>c1=null;
				try {
					c1=Class.forName(Internals.mechanics.get(s1));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Class<? extends SkillMechanic>iclass=c1.asSubclass(SkillMechanic.class);
				return iclass;
			} catch (IllegalArgumentException
					| SecurityException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		public Class<? extends SkillCondition> loadC(String s1) {
			try {
				Class<?>c1=null;
				try {
					c1=Class.forName(Internals.conditions.get(s1));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Class<? extends SkillCondition>iclass=c1.asSubclass(SkillCondition.class);
				return iclass;
			} catch (IllegalArgumentException
					| SecurityException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		public Class<? extends SkillTargeter> loadT(String s1) {
			try {
				Class<?>c1=null;
				try {
					c1=Class.forName(Internals.targeters.get(s1));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Class<? extends SkillTargeter>iclass=c1.asSubclass(SkillTargeter.class);
				return iclass;
			} catch (IllegalArgumentException
					| SecurityException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
	}
	
	@EventHandler
	public void onMythicMobsReload(MythicReloadedEvent e) {
		try {
			loader.load();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
