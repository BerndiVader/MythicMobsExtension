package main.java.com.gmail.berndivader.mythicmobsext.volatilecode;

import org.bukkit.Bukkit;

import main.java.com.gmail.berndivader.mythicmobsext.Main;

public class Volatile {
	public static Handler handler;
	
	public Volatile() {
		String v,n;
		n=Bukkit.getServer().getClass().getPackage().getName();
        v=n.substring(n.lastIndexOf(46)+1);
        try {
            Class<?>c=Class.forName("main.java.com.gmail.berndivader.mythicmobsext.volatilecode."+v+".Core");
            if (Handler.class.isAssignableFrom(c)) handler=(Handler)c.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception ex) {
        	if (ex instanceof ClassNotFoundException) {
        		Main.logger.warning("Server version not supported!");
        	}
        	ex.printStackTrace();
        }
	}
}
