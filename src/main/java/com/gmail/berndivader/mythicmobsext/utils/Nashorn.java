package com.gmail.berndivader.mythicmobsext.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.gmail.berndivader.mythicmobsext.Main;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

@SuppressWarnings("restriction")
public class Nashorn {
	static ScriptEngine nash;
	public static Invocable invoc;
	public static String scripts;
	public static String filename="Scripts.js";
	
	static {
		Thread.currentThread().setContextClassLoader(Main.getPlugin().getClass().getClassLoader());
		NashornScriptEngineFactory factory=new NashornScriptEngineFactory();
		nash=factory.getScriptEngine();
		invoc=(Invocable)nash;
		Main.getPlugin().saveResource(filename,false);
	}
	
	public Nashorn() {
		new MythicMobsReload();
		try {
			Path p1=Paths.get(Utils.str_PLUGINPATH,filename);
			scripts=new String(Files.readAllBytes(p1));
			nash.eval(scripts);
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
}
