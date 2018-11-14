package com.gmail.berndivader.mythicmobsext.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnEntityCollideEvent 
extends
Event {
    static final HandlerList handlers=new HandlerList();
    final Entity s,c;
    
    public OnEntityCollideEvent(Entity s,Entity t) {
    	this.s=s;
    	this.c=t;
	}
    
    public Entity getEntity() {
    	return this.s;
    }
    
    public Entity getCollider() {
    	return this.c;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
