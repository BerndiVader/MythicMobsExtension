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
	public static String pathStr;
	public static String scripts;
	public static String filename="Scripts.js";

	static {
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();	
		nash=factory.getScriptEngine();
		invoc=(Invocable)nash;
		pathStr=Main.getPlugin().getDataFolder().toString();
		Main.getPlugin().saveResource(filename,false);
		Path p1=Paths.get(pathStr,filename);
		try {
			scripts=new String(Files.readAllBytes(p1));
			nash.eval(scripts);
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
		new MythicMobsReload();
	}
	
}
