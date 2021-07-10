package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import io.lumine.xikage.mythicmobs.skills.*;
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

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.mechanics.AuraMechanic;

@ExternalAnnotation(name = "clicklistener", author = "BerndiVader")
public class ClickListenerMechanic extends AuraMechanic implements ITargetedEntitySkill {
	static String str;
	int maxDelay;
	String metaString, matchString;
	boolean actionbar, crouch;
	Optional<Skill> matchSkill = Optional.empty();
	Optional<Skill> clickSkill = Optional.empty();
	Optional<Skill> startSkill = Optional.empty();
	Optional<Skill> failSkill = Optional.empty();

	static {
		str = "MME_CLICKLISTENER";
	}

	public ClickListenerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		this.auraName = Optional.of(mlc.getString("buffname", str));
		String s1;
		if ((s1 = mlc.getString("startskill")) != null)
			startSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("clickskill")) != null)
			clickSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("finishskill")) != null)
			matchSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		if ((s1 = mlc.getString("failskill")) != null)
			failSkill = Utils.mythicmobs.getSkillManager().getSkill(s1);
		maxDelay = mlc.getInteger("maxdelay", 10);
		actionbar = mlc.getBoolean("actionbar", true);
		crouch = mlc.getBoolean("crouch", true);
		metaString = mlc.getString("meta", "actionstring");
		matchString = mlc.getString("matchstring", new String());
	}

	@Override
	public boolean castAtEntity(SkillMetadata arg0, AbstractEntity arg1) {
		if (!arg1.isPlayer())
			return false;
		if (!arg1.getBukkitEntity().hasMetadata(str)) {
			new ClickTracker(this, arg0, (Player) arg1.getBukkitEntity());
			return true;
		}
		return false;
	}

	class ClickTracker extends ClickListenerMechanic.AuraTracker implements Runnable, IParentSkill, Listener {
		final ClickListenerMechanic buff;
		public int ticksRemaining;
		String actionString;
		boolean hasEnded, finish, crouch;
		Player p;

		public ClickTracker(ClickListenerMechanic buff, SkillMetadata data, Player p) {
			super(BukkitAdapter.adapt(p), data);
			this.buff = buff;
			this.ticksRemaining = buff.maxDelay;
			this.skillMetadata.setCallingEvent(this);
			this.hasEnded = finish = false;
			this.crouch = ClickListenerMechanic.this.crouch;
			this.p = p;
			this.actionString = new String();
			Main.pluginmanager.registerEvents(this, Main.getPlugin());
			p.setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
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
			this.ticksRemaining--;
			if (finish) {
				p.setMetadata(metaString, new FixedMetadataValue(Main.getPlugin(), actionString));
				if (matchSkill.isPresent()) {
					Skill sk = matchSkill.get();
					SkillMetadata sd = skillMetadata.deepClone();
					if (sk.isUsable(sd))
						sk.execute(sd);
				}
				this.terminate();
			}
			if (skillMetadata.getCaster().getEntity().isDead() || !this.hasEnded && this.ticksRemaining < 1) {
				if (failSkill.isPresent()) {
					Skill sk = failSkill.get();
					SkillMetadata sd = skillMetadata.deepClone();
					if (sk.isUsable(sd))
						sk.execute(sd);
				}
				this.terminate();
			}
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void clickListener(PlayerInteractEvent e) {
			if (e.getPlayer() == p && e.getHand() == EquipmentSlot.HAND) {
				e.setCancelled(true);
				String s1 = e.getAction().toString().split("_")[0];
				actionString += actionString.isEmpty() ? s1 : "+" + s1;
				if (clickSkill.isPresent()) {
					Skill sk = clickSkill.get();
					SkillMetadata sd = skillMetadata.deepClone();
					if (sk.isUsable(sd))
						sk.execute(sd);
				}
				if (actionbar)
					NMSUtils.sendActionBar(p, actionString);
				ticksRemaining = buff.maxDelay;
			}
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void shiftListener(PlayerToggleSneakEvent e) {
			if (!crouch)
				return;
			if (e.getPlayer() == p) {
				e.setCancelled(true);
				finish = true;
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
				if (ClickListenerMechanic.this.auraName.isPresent()) {
					this.skillMetadata.getCaster().unregisterAura(ClickListenerMechanic.this.auraName.get(), this);
				}
				this.hasEnded = true;
			}
			HandlerList.unregisterAll(this);
			p.removeMetadata(str, Main.getPlugin());
			this.close();
			return true;
		}
	}

}
