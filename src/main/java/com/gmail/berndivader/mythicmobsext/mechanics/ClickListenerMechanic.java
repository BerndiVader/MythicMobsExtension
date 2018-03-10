package com.gmail.berndivader.mythicmobsext.mechanics;

import java.lang.reflect.Field;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.utils.tasks.Scheduler;
import io.lumine.utils.tasks.Scheduler.Task;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.BuffMechanic;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="clicklistener",author="BerndiVader")
public class ClickListenerMechanic 
extends 
BuffMechanic
implements
ITargetedEntitySkill {
	static String str;
	int maxDelay;
	String metaString;
	boolean actionbar;
	Optional<Skill>matchSkill=Optional.empty();
	Optional<Skill>clickSkill=Optional.empty();
	Optional<Skill>startSkill=Optional.empty();
	Optional<Skill>failSkill=Optional.empty();
	
	static {
		str="MME_CLICKLISTENER";
	}
	
	public ClickListenerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.buffName=Optional.of("clicklistener");
		String s1;
		if ((s1=mlc.getString("startskill"))!=null) startSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1=mlc.getString("clickskill"))!=null) clickSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1=mlc.getString("finishskill"))!=null) matchSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1=mlc.getString("failskill"))!=null) failSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		maxDelay=mlc.getInteger("maxdelay",10);
		actionbar=mlc.getBoolean("actionbar",true);
		metaString=mlc.getString("meta","actionstring");
	}

	@Override
	public boolean castAtEntity(SkillMetadata arg0, AbstractEntity arg1) {
		if (!arg1.isPlayer()) return false;
		if (!arg1.getBukkitEntity().hasMetadata(str)) {
			try {
				ClickTracker ct=new ClickTracker(this,arg0,(Player)arg1.getBukkitEntity());
				Field f=ct.getClass().getSuperclass().getDeclaredField("task");
				f.setAccessible(true);
				ct.task1=(Task)f.get(ct);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	class ClickTracker
	extends
	ClickListenerMechanic.BuffTracker
	implements
	Runnable,
	IParentSkill,
	Listener {
        final ClickListenerMechanic buff;
        Scheduler.Task task1;
        public int ticksRemaining;
        String actionString;
        boolean hasEnded,finish;
        Player p;
        
		public ClickTracker(ClickListenerMechanic buff,SkillMetadata data,Player p) {
			super(data);
			this.buff=buff;
			this.data=data;
            this.ticksRemaining=buff.maxDelay;
			this.data.setCallingEvent(this);
			this.hasEnded=finish=false;
			this.p=p;
			this.actionString="";
			Main.pluginmanager.registerEvents(this,Main.getPlugin());
			p.setMetadata(str,new FixedMetadataValue(Main.getPlugin(),true));
        	if (startSkill.isPresent()) {
    			Skill sk=startSkill.get();
    			SkillMetadata sd=data.deepClone();
    			sk.execute(sd);
        	}
			this.start();
		}
		
        @Override
        public void run() {
            this.ticksRemaining--;
            if (finish) {
            	p.setMetadata(metaString,new FixedMetadataValue(Main.getPlugin(),actionString));
            	if (matchSkill.isPresent()) {
        			Skill sk=matchSkill.get();
        			SkillMetadata sd=data.deepClone();
        			sk.execute(sd);
            	}
    			this.terminate();
            }
            if (data.getCaster().getEntity().isDead()||!this.hasEnded&&this.ticksRemaining<=0) {
            	if (failSkill.isPresent()) {
        			Skill sk=failSkill.get();
        			SkillMetadata sd=data.deepClone();
        			sk.execute(sd);
            	}
                this.terminate();
            }
        }
        
		@EventHandler(priority=EventPriority.LOWEST)
        public void clickListener(PlayerInteractEvent e) {
			if (e.getPlayer()==p&&e.getHand()==EquipmentSlot.HAND) {
				e.setCancelled(true);
				String s1=e.getAction().toString().split("_")[0];
				if (actionString.isEmpty()) {
					actionString+=s1;
				} else {
					actionString+="+"+s1;
				}
            	if (clickSkill.isPresent()) {
        			Skill sk=clickSkill.get();
        			SkillMetadata sd=data.deepClone();
        			sk.execute(sd);
            	}
				if (actionbar) NMSUtils.sendActionBar(p,actionString);
				ticksRemaining=buff.maxDelay;
			}
        }
		
		@EventHandler(priority=EventPriority.LOWEST)
		public void shiftListener(PlayerToggleSneakEvent e) {
			if (e.getPlayer()==p) {
				e.setCancelled(true);
				finish=true;
			}
		}
		
		@Override
		public boolean getCancelled() {
			return this.hasTerminated();
		}
		
		@Override
		public void setCancelled() {
			this.terminate();
		}
		
        @Override
        public boolean terminate() {
            if (!this.hasEnded) {
                if (ClickListenerMechanic.this.buffName.isPresent()) {
                    this.data.getCaster().unregisterBuff(ClickListenerMechanic.this.buffName.get(),this);
                }
                this.hasEnded = true;
            }
        	HandlerList.unregisterAll(this);
        	p.removeMetadata(str,Main.getPlugin());
        	return task1.terminate();
        }
	}
	
}
