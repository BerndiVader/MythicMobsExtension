package com.gmail.berndivader.mythicmobsext.javascript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.config.Config;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public class Nashorn {
	static Nashorn nashorn;
	public static ScriptEngine engine;
	public static Invocable invocable;
	public static String scripts, filename = "Scripts.js", examples = "ExampleScripts.js", includes = "Includes.js";

	public Nashorn() {
		nashorn = this;
		Path p1;
		p1 = Paths.get(Utils.str_PLUGINPATH, examples);
		if (!p1.toFile().exists())
			Main.getPlugin().saveResource(examples, false);
		p1 = Paths.get(Utils.str_PLUGINPATH, includes);
		if (!p1.toFile().exists())
			Main.getPlugin().saveResource(includes, false);
		try {
			p1 = Paths.get(Utils.str_PLUGINPATH, filename);
			Main.getPlugin().saveResource(filename, true);
			scripts = new String(Files.readAllBytes(p1));
			ScriptEngineManager manager=new ScriptEngineManager(ClassLoader.getSystemClassLoader());
			if((engine=manager.getEngineByName(Config.javascriptengine))==null) {
                            engine=manager.getEngineByName("nashorn");
                        }
			if(engine!=null) {
                            Main.logger.info("Using scriptengine "+engine.getFactory().getEngineName());
                            engine.eval(scripts);
                            invocable=(Invocable)engine;
			} else {
                            Main.logger.warning("No scriptengine found!");
                        }
		} catch (IOException | ScriptException e1) {
			e1.printStackTrace();
		}
	}
}
