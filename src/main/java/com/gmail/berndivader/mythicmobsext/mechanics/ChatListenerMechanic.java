package com.gmail.berndivader.mythicmobsext.mechanics;

import java.lang.reflect.Field;
import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
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
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

@ExternalAnnotation(name="chatlistener",author="BerndiVader")
public class ChatListenerMechanic 
extends 
BuffMechanic
implements
ITargetedEntitySkill {
	static String str;
	int period;
	boolean breakOnMatch,breakOnFalse,multi,cancelMatch,cancelFalse,removephrase;
	String storage;
	String[]phrases;
	RangedDouble radius;
	Optional<Skill>matchSkill=Optional.empty();
	Optional<Skill>falseSkill=Optional.empty();
	Optional<Skill>inuseSkill=Optional.empty();
	
	static {
		str="MME_CHATLISTENER";
	}
	
	public ChatListenerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String s1=mlc.getString("phrases","").toLowerCase();
		if (s1.startsWith("\"")&&s1.endsWith("\"")){
			s1=s1.substring(1,s1.length()-1);
		}
		phrases=SkillString.parseMessageSpecialChars(s1).split(",");
		period=mlc.getInteger("period",60);
		radius=new RangedDouble(mlc.getString("radius","<10"));
		breakOnMatch=mlc.getBoolean("breakonmatch",true);
		breakOnFalse=mlc.getBoolean("breakonfalse",false);
		cancelMatch=mlc.getBoolean("cancelmatch",false);
		cancelFalse=mlc.getBoolean("cancelfalse",false);
		removephrase=mlc.getBoolean("removephrase",false);
		multi=mlc.getBoolean("multi",false);
		storage=mlc.getString("meta",null);
		this.buffName=Optional.of("chatlistener");
		if ((s1=mlc.getString("matchskill"))!=null) matchSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1=mlc.getString("falseskill"))!=null) falseSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1=mlc.getString("inuseskill"))!=null) inuseSkill=Utils.mythicmobs.getSkillManager().getSkill(s1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata arg0, AbstractEntity arg1) {
		if (!arg1.isPlayer()) return false;
		if ((multi&&!arg1.getBukkitEntity().hasMetadata(str))||(!multi&&!arg0.getCaster().hasBuff(buffName.get()))) {
			try {
				BuffTracker ff=new ChatListener(this,arg0,arg1);
				Field f=ff.getClass().getSuperclass().getDeclaredField("task");
				f.setAccessible(true);
				((ChatListener)ff).task1=(Task)f.get(ff);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return true;
		}
		if (inuseSkill.isPresent()) {
			Skill sk=inuseSkill.get();
			SkillMetadata sd=arg0.deepClone();
			sk.execute(sd);
		}
		return false;
	}
	
	class ChatListener
	extends
	ChatListenerMechanic.BuffTracker
	implements
	Runnable,
	IParentSkill,
	Listener {
        final ChatListenerMechanic buff;
        Scheduler.Task task1;        
        int ticksRemaining;
        boolean hasEnded;
        AbstractEntity p;
        
		public ChatListener(ChatListenerMechanic buff,SkillMetadata data,AbstractEntity p) {
			super(data);
			this.buff=buff;
			this.data=data;
            this.ticksRemaining=buff.period;
			this.data.setCallingEvent(this);
			this.hasEnded=false;
			this.p=p;
			Main.pluginmanager.registerEvents(this,Main.getPlugin());
			p.getBukkitEntity().setMetadata(str,new FixedMetadataValue(Main.getPlugin(),true));
			this.start();
		}
		
        @Override
        public void run() {
            this.ticksRemaining--;
            if (data.getCaster().getEntity().isDead()||!this.hasEnded&&this.ticksRemaining<=0) {
                this.terminate();
            }
        }
        
		@EventHandler
        public void chatListener(AsyncPlayerChatEvent e) {
        	boolean bl1=phrases.length==0;
        	String s22=Utils.parseMobVariables(e.getMessage(),data,data.getCaster().getEntity(),p,null);
        	String s2=Utils.parseMobVariables(e.getMessage().toLowerCase(),data,data.getCaster().getEntity(),p,null);
        	Skill sk=null;
        	if(ChatListenerMechanic.this.radius.equals(
        			(double)Math.sqrt(Utils.distance3D(this.data.getCaster().getEntity().getBukkitEntity().getLocation().toVector(),
        			e.getPlayer().getLocation().toVector())))) {
        		for(int i1=0;i1<phrases.length;i1++) {
        			if ((bl1=s2.contains(Utils.parseMobVariables(phrases[i1],data,data.getCaster().getEntity(),p,null)))) {
        				if (removephrase) s22=s22.replace(phrases[i1],"");
        				break;
        			}
        		}
        		if (bl1) {
        			if (cancelMatch) e.setCancelled(true);
        			if (storage!=null) {
        				String s3=Utils.parseMobVariables(storage,data,data.getCaster().getEntity(),p,null);
        				data.getCaster().getEntity().getBukkitEntity().setMetadata(s3,new FixedMetadataValue(Main.getPlugin(),s22));
        			}
        			if (matchSkill.isPresent()) {
        				sk=matchSkill.get();
        				sk.execute(data.deepClone());
        			}
    				if (breakOnMatch) this.terminate();
        		} else {
        			if (cancelFalse) e.setCancelled(true);
        			if (falseSkill.isPresent()) {
        				sk=falseSkill.get();
        				sk.execute(data.deepClone());
        			}
    				if (breakOnFalse) this.terminate();
        		}
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
                if (ChatListenerMechanic.this.buffName.isPresent()) {
                    this.data.getCaster().unregisterBuff(ChatListenerMechanic.this.buffName.get(),this);
                }
                this.hasEnded = true;
            }
        	HandlerList.unregisterAll(this);
        	p.getBukkitEntity().removeMetadata(str,Main.getPlugin());
        	return task1.terminate();
        }
	}
	
}