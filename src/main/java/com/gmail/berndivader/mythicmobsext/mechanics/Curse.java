package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.IParentSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.AuraMechanic;

@ExternalAnnotation(name = "curse", author = "BerndiVader")
public class Curse extends AuraMechanic implements ITargetedEntitySkill {
	int duration;
	boolean infinite,strict;
	
	Optional<Skill> matchSkill = Optional.empty();
	Optional<Skill> startSkill = Optional.empty();
	Optional<Skill> endSkill = Optional.empty();
	
	final static String str = "MME_CURSE";

	public Curse(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.auraName = Optional.of(str);
		
		strict=mlc.getBoolean("strict",false);
		infinite=mlc.getBoolean("infinite",false);
		duration=mlc.getInteger("period",120);
		
		String s1=mlc.getString("matchskill");
		if ((s1!=null)) matchSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("startskill")) != null)
			startSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("endskill")) != null)
			endSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstractEntity) {
		if (!abstractEntity.getBukkitEntity().hasMetadata(str)) {
			new CurseTracker(this, data, abstractEntity);
			return true;
		}
		
		return false;
	}

	class CurseTracker extends Curse.AuraTracker implements Runnable, IParentSkill, Listener {
		final Curse buff;
		final int uid;
		int ticksRemaining;
		boolean hasEnded = false, regained=false;
		AbstractEntity abstractEntity;
		double prevHealth;

		public CurseTracker(Curse buff, SkillMetadata data, AbstractEntity abstractEntity) {
			super(abstractEntity,data);
			uid=abstractEntity.getBukkitEntity().getEntityId();
			this.buff = buff;
			this.ticksRemaining = buff.duration;
			this.skillMetadata.setCallingEvent(this);
			this.abstractEntity = abstractEntity;
			Main.pluginmanager.registerEvents(this, Main.getPlugin());
			prevHealth=abstractEntity.getHealth();
			if (startSkill.isPresent()) {
				Skill sk = startSkill.get();
				SkillMetadata sd = data.deepClone();
				if (sk.isUsable(sd))
					sk.execute(sd);
			}
			
			this.start();
		}

		@Override
		public void run() {
			if (!buff.infinite) ticksRemaining--;
			if (abstractEntity==null||abstractEntity.isDead()||!hasEnded && ticksRemaining <= 0) {
				if (endSkill.isPresent() && endSkill.get().isUsable(skillMetadata)) endSkill.get().execute(skillMetadata.deepClone());
				terminate();
			} else {
				double health=abstractEntity.getHealth();
				if(prevHealth<health) {
					if(strict||(!strict&&!regained)) {
						if (matchSkill.isPresent()) {
							Skill sk = matchSkill.get();
							SkillMetadata sd = this.skillMetadata.deepClone();
							if (sk.isUsable(sd))
								sk.execute(sd);
						}
						abstractEntity.setHealth(prevHealth);
					}
					regained=false;
				}
				prevHealth=abstractEntity.getHealth();
			}
		}
		
		@EventHandler
		public void regeneration(EntityRegainHealthEvent e) {
			if(e.getEntity().getEntityId()==uid) {
				regained=true;
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
				if (Curse.this.auraName.isPresent()) {
					this.skillMetadata.getCaster().unregisterAura(Curse.this.auraName.get(), this);
				}
				this.hasEnded = true;
			}
			HandlerList.unregisterAll(this);
			abstractEntity.getBukkitEntity().removeMetadata(str,Main.getPlugin());
			if (endSkill.isPresent()) {
				Skill sk = endSkill.get();
				SkillMetadata sd = this.skillMetadata.deepClone();
				if (sk.isUsable(sd))
					sk.execute(sd);
			}
			this.close();
			return true;
		}
	}

}