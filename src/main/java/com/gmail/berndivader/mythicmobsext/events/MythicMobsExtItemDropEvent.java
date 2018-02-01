package main.java.com.gmail.berndivader.mythicmobsext.events;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MythicMobsExtItemDropEvent 
extends
Event 
implements
Cancellable {
    private static final HandlerList handlers=new HandlerList();
    private final ActiveMob am;
    private final LivingEntity trigger;
    private ArrayList<ItemStack>stack;
    private boolean cancelled;
    
    public MythicMobsExtItemDropEvent(ActiveMob am,LivingEntity trigger,ArrayList<ItemStack>stack) {
    	this.am=am;
    	this.trigger=trigger;
    	this.stack=stack;
    	this.cancelled=false;
	}
    
    public ActiveMob getMob() {
        return this.am;
    }

    public Entity getEntity() {
        return this.am.getEntity().getBukkitEntity();
    }

    public MythicMob getMobType() {
        return this.am.getType();
    }

    public int getMobLevel() {
        return this.am.getLevel();
    }

    public LivingEntity getTrigger() {
        return this.trigger;
    }

    public ArrayList<ItemStack> getDrops() {
        return this.stack;
    }

    public void setDrops(ArrayList<ItemStack>l1) {
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
