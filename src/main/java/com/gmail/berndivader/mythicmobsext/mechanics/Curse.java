package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.mechanics.AuraMechanic;

@ExternalAnnotation(name = "curse", author = "BerndiVader")
public class Curse extends AuraMechanic implements ITargetedEntitySkill {
	int duration;
	float ratio;
	boolean infinite,strict,cancel=false;
	List<RegainReason>reasons;
	
	Optional<Skill> matchSkill = Optional.empty();
	Optional<Skill> startSkill = Optional.empty();
	Optional<Skill> failSkill = Optional.empty();
	Optional<Skill> endSkill = Optional.empty();
	
	final static String str = "MME_CURSE";

	public Curse(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		this.auraName = Optional.of(str);
		reasons=new ArrayList<>();
		
		ratio=mlc.getFloat("ratio",1f);
		strict=mlc.getBoolean("strict",false);
		if(strict) {
			String temp=mlc.getString("reasons","eating,regen,satiated,magic,magic_regen").toUpperCase();
			if(!(strict=temp.contains("ANY"))) {
				String[]parse=temp.split(",");
				int size=parse.length;
				for(int i1=0;i1<size;i1++) {
					try {
						RegainReason rr=RegainReason.valueOf(parse[i1]);
						reasons.add(rr);
					} catch (Exception ex) {
						Main.logger.warning("There was no valid reason for "+parse[i1]+". Ignoring it.");
					}
				}
			}
		}
		
		infinite=mlc.getBoolean("infinite",false);
		duration=mlc.getInteger("period",120);
		
		String s1=mlc.getString("matchskill");
		if ((s1!=null)) matchSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("startskill")) != null)
			startSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("endskill")) != null)
			endSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("failskill")) != null)
			failSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
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
		final float ratio=Curse.this.ratio;
		final int uid;
		int ticksRemaining;
		boolean hasEnded = false;
		AbstractEntity abstractEntity;
		double prevHealth;
		RegainReason currentReason=null;

		public CurseTracker(Curse buff, SkillMetadata data, AbstractEntity abstractEntity) {
			super(abstractEntity,data);
			uid=abstractEntity.getBukkitEntity().getEntityId();
			this.buff = buff;
			this.ticksRemaining = buff.duration;
			this.skillMetadata.setCallingEvent(this);
			this.abstractEntity = abstractEntity;
			if(strict) {
				Main.pluginmanager.registerEvents(this, Main.getPlugin());
			}
			if (startSkill.isPresent()) {
				Skill sk = startSkill.get();
				SkillMetadata sd = data.deepClone();
				if (sk.isUsable(sd))
					sk.execute(sd);
			}
			prevHealth=abstractEntity.getHealth();
			this.start();
		}

		@Override
		public void run() {
			if (!buff.infinite) ticksRemaining--;
			if (abstractEntity==null||abstractEntity.isDead()||(!hasEnded && ticksRemaining <= 0)) {
				if (endSkill.isPresent() && endSkill.get().isUsable(skillMetadata)) endSkill.get().execute(skillMetadata.deepClone());
				terminate();
			} else {
				double health=abstractEntity.getHealth();
				if(prevHealth<health) {
					double amount=(health-prevHealth)*ratio;
					if(strict) {
						if(currentReason!=null&&reasons.contains(currentReason)) {
							doMatchSkill(health,amount);
							currentReason=null;
						} else if(failSkill.isPresent()) {
							Skill sk = failSkill.get();
							SkillMetadata sd = this.skillMetadata.deepClone();
							if (sk.isUsable(sd)) sk.execute(sd);
						}
					} else {
						doMatchSkill(health,amount);
					}
				}
				prevHealth=abstractEntity.getHealth();
			}
		}
		
		void doMatchSkill(double health, double amount) {
			if (matchSkill.isPresent()) {
				Skill sk = matchSkill.get();
				SkillMetadata sd = this.skillMetadata.deepClone();
				if (sk.isUsable(sd)) {
					sk.execute(sd);
					abstractEntity.setHealth(health-amount);
				}
			} else {
				abstractEntity.setHealth(health-amount);
			}
		}
		
		@EventHandler
		public void onRegainHealth(EntityRegainHealthEvent e) {
			if(e.getEntity().getEntityId()==uid) this.currentReason=e.getRegainReason();
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
			HandlerList.unregisterAll(this);
			if (!this.hasEnded) {
				if (Curse.this.auraName.isPresent()) {
					this.skillMetadata.getCaster().unregisterAura(Curse.this.auraName.get(), this);
				}
				this.hasEnded = true;
			}
			abstractEntity.getBukkitEntity().removeMetadata(str,Main.getPlugin());
			if (endSkill.isPresent()) {
				Skill sk = endSkill.get();
				SkillMetadata sd = this.skillMetadata.deepClone();
				if (sk.isUsable(sd)) sk.execute(sd);
			}
			this.close();
			return true;
		}
	}

}