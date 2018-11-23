package com.gmail.berndivader.mythicmobsext.events;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public
class
MythicMobCollideEvent 
extends
Event {
    static final HandlerList handlers=new HandlerList();
    final Optional<ActiveMob>am;
    final Entity collider;
    
    public MythicMobCollideEvent(Entity entity,Entity collided_entity) {
    	this(null,entity,collided_entity);
	}
    
    public MythicMobCollideEvent(ActiveMob activemob,Entity entity,Entity collided_entity) {
    	this.am=Optional.ofNullable(activemob);
    	this.collider=collided_entity;
	}
    
    public ActiveMob getActiveMob() {
    	return am.get();
    }
    
    public Entity getCollider() {
    	return this.collider;
    }
    
    public boolean isActiveMob() {
    	return am.isPresent();
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
