package com.gmail.berndivader.mythicmobsext.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

public
class 
EntityCacheHandler
{
	private static HashMap<UUID,String>cached_entities;
	private int task_id;
	
	static {
		cached_entities=new HashMap<>();
	}
	
	public EntityCacheHandler() {
		
		task_id=new BukkitRunnable() {
			@Override
			public void run() {
				int size=EntityCacheHandler.cached_entities.size();
				Iterator<Map.Entry<UUID,String>>iteration=EntityCacheHandler.cached_entities.entrySet().iterator();
				for (int i1=0;i1<size;i1++) {
					Map.Entry<UUID,String>entry=iteration.next();
					if(NMSUtils.getEntity(Bukkit.getWorld(entry.getValue()),entry.getKey())==null) iteration.remove();
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(),600l,600l).getTaskId();
	}
	
	public static void add(Entity entity) {
		cached_entities.put(entity.getUniqueId(), entity.getWorld().getName());
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(task_id);
		cached_entities.clear();
	}
	
}
