package com.gmail.berndivader.mythicmobsext.bossbars;

import java.util.HashMap;
import java.util.List;

import org.bukkit.boss.BossBar;

public 
class 
BossBarHelper 
{
	static HashMap<String,List<BossBar>>bars;
	
	static {
		bars=new HashMap<>();
	}

	public static boolean contains(String id) {
		return bars.containsKey(id);
	}
	

}
