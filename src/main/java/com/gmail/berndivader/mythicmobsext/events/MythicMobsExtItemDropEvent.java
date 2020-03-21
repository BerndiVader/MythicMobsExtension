package com.gmail.berndivader.mythicmobsext.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;

public class MythicMobsExtItemDropEvent 
extends
Event 
implements
Cancellable {
    private static final HandlerList handlers=new HandlerList();
    private final SkillCaster am;
    private final LivingEntity trigger;
    private DropTable stack;
    private boolean cancelled;
    
    public MythicMobsExtItemDropEvent(SkillCaster am,LivingEntity trigger,DropTable dt) {
    	this.am=am;
    	this.trigger=trigger;
    	this.stack=dt;
    	this.cancelled=false;
	}
    
    public SkillCaster getMob() {
        return this.am;
    }

    public Entity getEntity() {
        return this.am.getEntity().getBukkitEntity();
    }

    public Entity getMobType() {
        return this.am.getEntity().getBukkitEntity();
    }

    public double getMobLevel() {
        return this.am.getLevel();
    }

    public LivingEntity getTrigger() {
        return this.trigger;
    }

    public DropTable getDrops() {
        return this.stack;
    }

    public void setDrops(DropTable l1) {
        this.stack=l1;
    }    

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean bl1) {
		this.cancelled=bl1;
	}
}
